package lang;

public class PlusPlusTest {
	public static void main(String[] args) {
		int n = 0;
		
		System.out.println(++n - n++);
		System.out.println(n++ - ++n);
		
		if (n++ == ++n) {
			System.out.println(true);
		} else {
			System.out.println(false);
		}
	}
}
