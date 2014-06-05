package thread;

import java.util.concurrent.TimeUnit;

public class JoinTest {
	public static void main(String[] args) {
		MyThread tt1 = new MyThread("thread-1", 10);
		MyThread tt2 = new MyThread("thread-2", 2);
		tt2.start();
		tt1.start();
		try {
			tt1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("current-thread-end");
	}
}

class MyThread extends Thread {
	private String name;
	private int sleepSecond;

	public MyThread(String name, int sleepSecond) {
		this.name = name;
		this.sleepSecond = sleepSecond;
	}

	public void run() {
		System.out.println("=== "+ name + " begin ===");
		for (int n = 0; n < sleepSecond; ++n) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		System.out.println("=== "+ name + " end ===");
	}
}