package be.glever.ant.messagebus;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageBus<T> {
	private static final Logger LOG = LoggerFactory.getLogger(MessageBus.class);

	private LinkedBlockingQueue<QueueElement<T>> queue;
	private List<MessageBusListener<T>> listeners = new ArrayList<>();

	public MessageBus() {
		queue = new LinkedBlockingQueue<>(1000);
		new Thread(()-> {
		StreamSupport.stream(new MySpliterator<>(queue), false).forEach(item->{
		notifyListeners(item);	
		});	
		}).start();
	}

	private void notifyListeners(T item) {
		// todo treat listeners and remove if necessary
	}

	public void put(T t) {
		this.queue.offer(new QueueElement<T>(t, false));
	}

	private static class QueueElement<T> {
		private T t;
		private boolean poison;

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

}
