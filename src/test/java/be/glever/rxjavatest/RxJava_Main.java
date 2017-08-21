package be.glever.rxjavatest;

import java.util.concurrent.LinkedBlockingQueue;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class RxJava_Main {

	public static void main(String[] args) throws InterruptedException {
		LinkedBlockingQueue<String> lbq = new LinkedBlockingQueue<>();
		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				try {
					lbq.put("" + i);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.interrupted();
				}
			}
		}).start();

		Observable.create(subscriber -> {
			while (true) {
				subscriber.onNext(lbq.take());
			}
		}).subscribeOn(Schedulers.computation()).subscribe(it -> {
			System.out.println(Thread.currentThread() + " -- " + it);
		});
	}

}
