package thread;

import java.util.concurrent.atomic.AtomicBoolean;

public class YieldTest {
	public static final AtomicBoolean bool = new AtomicBoolean();
	public static void main(String[] args) {
		Thread t1 = new Thread() {
			@Override
			public void run() {
				synchronized (bool) {
					try {
						while (!bool.get()) {
							Thread.yield();
							bool.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("t1 yield end");
				}
			};
		};
		t1.start();
		
		Thread t2 = new Thread() {
			@Override
			public void run() {
				synchronized (bool) {
					System.out.println("t2 lock obj");
					bool.set(true);
					bool.notify();
				}
			}
		};
		t2.start();
		System.out.println("t2 start");
	}
}
