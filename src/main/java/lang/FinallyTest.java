package lang;

public class FinallyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(fun());
	}

	private static String fun() {
		
		try {
			int m = 0;
			int n = 1/m;
			return "xx";
		} catch (Throwable e) {
			throw new RuntimeException("");
		} finally {
			return "oo";
		}
		
	}

}
