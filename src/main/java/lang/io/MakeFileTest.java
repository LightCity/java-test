package lang.io;

import java.io.File;

public class MakeFileTest {
	public static void main(String[] args) {
		try {
			File baseDir = new File("/home/longli/tmpFile/");
			boolean mkdir = baseDir.mkdir();
			if (mkdir) {
				for (int n = 0; n < 5; ++n) {
					File newFile = null;
					try {
						newFile = new File(baseDir.getAbsolutePath() + "/tmp-fuck-" + n + ".txt");
						boolean createNewFile = newFile.createNewFile();
						System.out.println(createNewFile);
					} finally {
						if (newFile != null) {
							/// don't need to close
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
