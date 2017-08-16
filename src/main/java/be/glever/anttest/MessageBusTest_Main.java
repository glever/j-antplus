package be.glever.anttest;

import java.util.Spliterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageBusTest_Main {
	private static final Logger LOG = LoggerFactory.getLogger(MessageBusTest_Main.class);

	public static void main(String[] args) {
		LinkedBlockingQueue<QueueElement<Integer>> bq = new LinkedBlockingQueue<>();
		
		new Thread(()-> {
			for(int i = 0 ; i < 10; i ++) {
				bq.add(new QueueElement<Integer>(i, false));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.interrupted();
				}
			}
			bq.add(new QueueElement<Integer>(-1, true));
			
		}).start();
		
		Stream<QueueElement<Integer>> readStream = StreamSupport.stream(new MySpliterator<>(bq), false);
		new Thread(()-> {
			readStream.forEach(item -> LOG.info("" + item.t));
		}).start();
	}

	public static class QueueElement<T> {
		private T t;
		private boolean poison;

		public QueueElement(T t, boolean poison) {
			this.t = t;
			this.poison = poison;
		}
	}
	
	public static class MySpliterator<T> implements Spliterator<QueueElement<T>>{
		private LinkedBlockingQueue<QueueElement<T>> queue;

		public MySpliterator(LinkedBlockingQueue<QueueElement<T>> queue) {
			this.queue = queue;			
		}

		@Override
		public boolean tryAdvance(Consumer<? super QueueElement<T>> action) {
			try {
				QueueElement<T> t = this.queue.take();
				if(! t.poison) {
					action.accept(t);
					return true;					
				}
			} catch (InterruptedException e) {
				Thread.interrupted();
			}
			return false;
		}

		@Override
		public Spliterator<QueueElement<T>> trySplit() {
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
