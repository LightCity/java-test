package disruptor;

public class LongEvent {
	private long value;

	public long get() {
		return value;
	}

	public void set(long vlaue) {
		this.value = vlaue;
	}
	
	@Override
	public String toString() {
		return "" + value;
	}
}
