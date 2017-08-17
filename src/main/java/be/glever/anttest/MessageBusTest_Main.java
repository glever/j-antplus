package be.glever.anttest;

import java.util.Spliterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.glever.anttest.MessageBusTest_Main.QueueElement;

public class MessageBusTest_Main {
	private static final Logger LOG = LoggerFactory.getLogger(MessageBusTest_Main.class);

	public static void main(String[] args) {
		LinkedBlockingQueue<QueueElement<Integer>> bq = new LinkedBlockingQueue<>();

		MessageProducer messageProducer = new MessageProducer(bq);
		Thread producer = new Thread(messageProducer);
		producer.start();

		Stream<Integer> readStream = StreamSupport.stream(new MySpliterator<>(bq), false);
		new Thread(() -> {
			readStream.forEach(item -> LOG.info("" + item));
		}).start();
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			Thread.interrupted();
		}

		messageProducer.stop();
		producer.interrupt();

	}

	private static final class MessageProducer implements Runnable {
		private final LinkedBlockingQueue<QueueElement<Integer>> bq;
		private boolean stop;

		private MessageProducer(LinkedBlockingQueue<QueueElement<Integer>> bq) {
			this.bq = bq;
		}
		
		public void stop() {
			this.stop = true;
		}

		@Override
		public void run() {
			for (int i = 0; ; i++) {
				bq.add(new QueueElement<Integer>(i, false));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.interrupted();
					if(stop) {
						LOG.info("asked to stop processing");
						bq.add(new QueueElement<Integer>(-1, true));
						break;
					}
				}
			}

		}
	}

	public static class QueueElement<T> {
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

	public static class MySpliterator<T> implements Spliterator<T> {
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
				}else {
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
