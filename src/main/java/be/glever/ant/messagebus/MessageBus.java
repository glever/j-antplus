package be.glever.ant.messagebus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Small messagebus implementation using a blocking queue. Messages are
 * dispatched by a single dedicated thread meaning listeners run sequentially.
 *
 * @author glen
 *
 * @param <T>
 */
public class MessageBus<T> implements Closeable {
	private static final Logger LOG = LoggerFactory.getLogger(MessageBus.class);

	private LinkedBlockingQueue<QueueElement<T>> queue;
	private List<ListenerConfig<T>> listenerConfigs = new ArrayList<>();

	public MessageBus() {
		queue = new LinkedBlockingQueue<>(1000);
		new Thread(() -> StreamSupport.stream(new MySpliterator<>(queue), false).forEach(this::notifyListeners)).start();
	}

	private void notifyListeners(T item) {
		LOG.debug("Received {}", item);

		// removes listeners that are in timeout + sends message to other listeners and removes listeners which no longer want to process new messages
		listenerConfigs.removeAll(new ArrayList<>(this.listenerConfigs).stream()
				.filter(listener -> listener.timeoutOccurred() || listener.handledLastMessage(item))  // ... all in 1 hard to read line ...
				.collect(Collectors.toList()));
	}

	/**
	 * Adds an item to the queue after which it will be dispatched by the dispatcher
	 * {@link Thread}.
	 *
	 * @param t
	 */
	public void put(T t) {
		put(t, false);
	}

	private void put(T t, boolean poison) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("put({}), poison: {}", t, poison);
		}
		this.queue.offer(new QueueElement<>(t, poison));

	}

	/**
	 * Adds a listener which will be notified of messages between now and now +
	 * timeout, and for nrMessages. The listener will be removed:
	 * <ul>
	 * <li>once timeout occured, before dispatching any messages</li>
	 * <li>after treating a message and nrMessages have been treated (before timeout
	 * has occurred)</li>
	 * </ul>
	 *
	 * @param listener
	 *            The listener to notify of events.
	 * @param timeout
	 *            Time to wait for messages. Negative values mean indefinite.
	 * @param nrMessages
	 *            Number of messages to dispatch to this listener before discarting
	 *            the listener. Negative value means unlimited.
	 */
	public void addQueueListener(long timeout, long nrMessages, MessageBusListener<T> listener) {
		this.listenerConfigs.add(new ListenerConfig<>(listener, timeout, nrMessages));
	}

	private static class ListenerConfig<T> {
		private MessageBusListener<T> listener;
		private long timeoutTime;
		private long nrMessages;

		public ListenerConfig(MessageBusListener<T> listener, long timeout, long nrMessages) {
			this.listener = listener;
			this.timeoutTime = timeout > -1 ? System.currentTimeMillis() + timeout : -1;
			this.nrMessages = nrMessages;
		}

		public boolean timeoutOccurred() {
			return this.timeoutTime > -1 ? System.currentTimeMillis() > timeoutTime : false;
		}

		public boolean handledLastMessage(T item) {
			boolean handledMessage = listener.handle(item) && (nrMessages > 0 && --nrMessages == 0);
			if (LOG.isDebugEnabled()) {
				LOG.debug("listener {} handled the message {}: {}", listener, item, handledMessage);
			}
			return handledMessage;
		}
	}

	/**
	 * Helper class to allow to inject a "poison" message in the queue to indicate
	 * the queue stream must be closed. Used for clean shutdown of dispatcher
	 * thread.
	 *
	 * @author glen
	 *
	 * @param <T>
	 */
	private static class QueueElement<T> {
		private T t;
		private boolean poison;

		/**
		 * Constructor for message wrapper.
		 *
		 * @param t
		 *            The queue item to wrap.
		 * @param poison
		 */
		public QueueElement(T t, boolean poison) {
			this.t = t;
			this.poison = poison;
		}

		public T get() {
			return t;
		}

		public boolean isPoison() {
			return poison;
		}

	}

	/**
	 * Creates a stream of a blocking queue and indicates end of stream once a
	 * poison message is encountered.
	 *
	 * @author glen
	 *
	 * @param <T>
	 */
	private static class MySpliterator<T> implements Spliterator<T> {
		private LinkedBlockingQueue<QueueElement<T>> queue;

		public MySpliterator(LinkedBlockingQueue<QueueElement<T>> queue) {
			this.queue = queue;
		}

		@Override
		public boolean tryAdvance(Consumer<? super T> action) {
			try {
				QueueElement<T> t = this.queue.take();
				if (!t.isPoison()) {
					action.accept(t.get());
					return true;
				} else {
					LOG.info("received poison message, stopping");
				}
			} catch (InterruptedException e) {
				Thread.interrupted();
				LOG.info("Interrupted, only reason would be to stop consuming");
			}
			return false;
		}

		@Override
		public Spliterator<T> trySplit() {
			return null;
		}

		@Override
		public long estimateSize() {
			return Long.MAX_VALUE;
		}

		@Override
		public int characteristics() {
			return Spliterator.ORDERED;
		}

	}

	/**
	 * Shuts down the message dispatch thread.
	 */
	@Override
	public void close() {
		put(null, true);
	}

}
