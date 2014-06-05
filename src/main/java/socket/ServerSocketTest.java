package socket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ServerSocket ssc = new ServerSocket(8888);
		while (true) {
			Socket sc = ssc.accept();
			InputStream inputStream = sc.getInputStream();
			OutputStream outputStream = sc.getOutputStream();
			BufferedInputStream bif = new BufferedInputStream(inputStream);

			BufferedReader br = new BufferedReader(new InputStreamReader(bif));
			String line = null;
			do {
				line = br.readLine();
				if ("".equals(line)) {
					System.out.println("******* last *******");
				}
				if (null == line) {
					break;
				}
				System.out.println(line);
			} while (line != null);
			System.out.println("==================================");
			outputStream.write("fuck".getBytes());
			outputStream.flush();
			inputStream.close();
			outputStream.close();
			sc.close();
		}
	}

}
