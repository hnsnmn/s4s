package org.chimi.s4s.storage.localfs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.chimi.s4s.storage.FileId;

public abstract class LocalFSFileIdUtil {
	private static String values = "abcdefghijklmnopqrstuvwxyz"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
	private static Random random = new Random();

	public static FileId generateFileId() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String tail = "";
		for (int i = 0; i < 4; i++) {
			int radomIdx = random.nextInt(values.length());
			tail += values.substring(radomIdx, radomIdx + 1);
		}
		return new FileId(dateFormat.format(new Date()) + tail);
	}

}
