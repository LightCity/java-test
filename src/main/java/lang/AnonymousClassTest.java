package lang;

public class AnonymousClassTest {
	public static void main(String[] args) {
		for (int n=0; n<10; ++n) {
			if (new Test() {
				{
					System.out.println("构造了一个新对象");
				}
				@Override
				public boolean test(int n) {
					return n % 2 == 0;
				}
			}.test(n)) {
				System.out.println("--");
			}
		}
	}
}

interface Test {
	public boolean test(int n);
}