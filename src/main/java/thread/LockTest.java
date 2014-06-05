package thread;

import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
	public static void main(String[] args) throws InterruptedException {
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		System.out.println("fuck");;
		lock.unlock();
		Thread.sleep(1000);
		lock.unlock();
	}
}
