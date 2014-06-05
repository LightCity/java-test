package lang;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
public class Main {
	public static void main(String[] args) {
		HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
		hashMap.put(100, "arg1");
		
		Map<Integer, String> m = Collections.synchronizedMap(hashMap);
		
		Object nil = new HashMap();
		System.out.println(nil instanceof Map);
	}
}
