package be.glever.reactor.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import reactor.core.publisher.Flux;

public class ReactorTest_Main {

	public static void main(String[] args) {
		Flux<String> flux = Flux.from(subscriber -> {
			new Thread(() -> {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try {
					for (String line = br.readLine(); !"exit".equals(line); line = br.readLine()) {
						if ("kapot".equals(line)) {
							throw new Exception("t is kapot");
						}
						subscriber.onNext(line);
					}
					subscriber.onComplete();
				} catch (Exception e) {
					subscriber.onError(e);
				}
			}).start();
		});

		flux.subscribe(ReactorTest_Main::printWithThreadInfo, ReactorTest_Main::printWithThreadInfo, () -> {
			printWithThreadInfo("all done");
			;
		});
	}

	public static void printWithThreadInfo(Object s) {
		System.out.println(Thread.currentThread().getName() + "-" + s);
	}
}
