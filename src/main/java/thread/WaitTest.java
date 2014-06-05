package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WaitTest {

	/**
	 * 容器的最大长度
	 */
	private static final int listSize = 3;
	/**
	 * 容器
	 */
	private static List<String> list = new ArrayList<String>(listSize);
	
	private static ReentrantLock lock = new ReentrantLock();
	private static Condition notFull = lock.newCondition();
	private static Condition notEmpty = lock.newCondition();

	static class ProducerThread extends Thread {
		/**
		 * 生产者的id
		 */
		private int proId;
		/**
		 * 当这个生产者结束后，这个值降1，表明生产者数量减少了一个
		 */
		private CountDownLatch proCount;

		public ProducerThread(int proId, CountDownLatch proCount) {
			this.proId = proId;
			this.proCount = proCount;
		}

		@Override
		public void run() {
			int time = 0;
			while (time < 5) {
				/// synchronized (list) {
				lock.lock();
				try {
					while (list.size() >= listSize) { // 标记A1
						try {
							System.out.println("**** producer proId=" + proId
									+ ": wait begin, time=" + time + " list.size=" + list.size());
							/// list.wait();
							notFull.await();
							System.out.println("**** producer proId=" + proId
									+ ": wait finish, time=" + time + " list.size=" + list.size());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					list.add(time++ + ":" + proId);
					notEmpty.signal();
					System.out.println("------------- pro " + proId + " add ----------------");
					/// list.notifyAll(); // 当有多个生产者时，这里会唤醒其他生产者，因此在标记A1处要用while，而不能是if
				} finally {
					lock.unlock();
				}
				/// }
			}
			System.out.println("producer proId=" + proId + ": end");
			proCount.countDown();

			// 确保那些还在阻塞的消费者被signal
			lock.lock();
			try {
				notEmpty.signalAll();
			} finally {
				lock.unlock();
			}
		}
	}

	static class ComsumerThread extends Thread {
		/**
		 * 消费者id
		 */
		private int comId;
		/**
		 * 当这个生产者结束后，这个值降1，表明生产者数量减少了一个
		 */
		private CountDownLatch proCount;

		public ComsumerThread(int comsumerId, CountDownLatch proCount) {
			this.comId = comsumerId;
			this.proCount = proCount;
		}

		@Override
		public void run() {
			while (true) {
				if (proCount.getCount() == 0) {
					return;
				}
				/// synchronized (list) {
				lock.lock();
				try {
					while (list.size() == 0) { // 标记B1
						if (proCount.getCount() == 0) {
							System.out.println("comsumer " + comId + " return.");
							return;
						}
						try {
							System.out.println("##### consumer comId=" + comId
									+ ", wait begin, list.size=" + list.size() + ", proCount=" + proCount.getCount());
							/// list.wait();
							notEmpty.await();
							System.out.println("##### consumer comId=" + comId
									+ ", wait end, list.size=" + list.size() + ", proCount=" + proCount.getCount());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					String firstItem = list.remove(0);
					System.out.println("消费一个：" + firstItem + ", list.size=" + list.size());
					notFull.signal();
					/// list.notifyAll(); // 当有多个消费者时，这个notify会唤醒其他消费者，因此在标记B1处要用while，而不能是if
				} finally {
					lock.unlock();
				}
				/// }
			}
		}
	}

	public static void main(String[] args) {
		final int proCount = 10; // 生产者的数量
		final int comCount = 20; // 消费者的数量
		CountDownLatch countDown = new CountDownLatch(proCount);
		System.out.println("========================================================");
		for (int nLoop = 0; nLoop < proCount; ++nLoop) {
			ProducerThread producerThread = new ProducerThread(nLoop, countDown);
			producerThread.setPriority(Thread.MIN_PRIORITY);
			producerThread.start();
		}
		for (int nLoop = 0; nLoop < comCount; ++nLoop) {
			ComsumerThread comsumerThread = new ComsumerThread(nLoop, countDown);
			comsumerThread.setPriority(Thread.MAX_PRIORITY);
			comsumerThread.start();
		}
	}
}
