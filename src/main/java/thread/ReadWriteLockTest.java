package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
	static ReadWriteLock lock = new ReentrantReadWriteLock();
	static Lock readLock = lock.readLock();
	static Lock writeLock = lock.writeLock();

	static volatile boolean finish = false;
	static volatile List<Integer> value = new ArrayList<Integer>(2);
	static {
		value.add(0);
		value.add(0);
	}

	public static void main(String[] args) {
		Thread writeThread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int n = 0; n < 1000; ++n) {
					writeLock.lock();
					try {
						value.set(0, n);
						value.set(1, 2*n);
					} finally {
						writeLock.unlock();
					}
				}
				finish = true;
			}
		};
		writeThread.start();
		
		final int readThreadCount = 10;
		Thread[] readThreadArray = new Thread[readThreadCount];
		for (int n=0; n<readThreadCount; ++n) {
			final int id = n;
			readThreadArray[n] = new Thread() {
				@Override
				public void run() {
					while (!finish) {
						readLock.lock();
						try {
							System.out.println("readThread-" + id + ": value=" + value);
							if (value.get(0) * 2  != value.get(1)) {
								try {
									throw new Exception("xxxxxxxxxxxxxxxxxxxxxxxxx");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} finally {
							readLock.unlock();
						}
					}
				};
			};
		}
		for (int n=0; n<readThreadCount; ++n) {
			readThreadArray[n].start();
		}
	}
}
