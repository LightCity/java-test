package lang;

public class MyThreadLocalTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadLocal<Integer> holder = new ThreadLocal<Integer>() {
			protected Integer initialValue() {
				return 0;
			}
		};
		Integer integer = holder.get();
		System.out.println(integer);
	}

}
