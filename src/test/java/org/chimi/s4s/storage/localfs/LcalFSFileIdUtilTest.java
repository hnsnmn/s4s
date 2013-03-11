package org.chimi.s4s.storage.localfs;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.chimi.s4s.storage.FileId;
import org.junit.Test;

public class LcalFSFileIdUtilTest {

	private Map<String, String> ids = new ConcurrentHashMap<String, String>();

	@Test
	public void randomInSingleThread() {
		for (int i = 0; i < 100; i++) {
			FileId id = LocalFSFileIdUtil.generateFileId();
			ids.put(id.getId(), id.getId());
		}
		assertEquals(100, ids.size());
	}

	@Test
	public void radomInMultiThreads() throws InterruptedException {
		int threadCount = 100;
		final int count = 1000;
		ExecutorService executorService = Executors
				.newFixedThreadPool(threadCount);
		for (int i = 0; i < threadCount; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < count; i++) {
						FileId id = LocalFSFileIdUtil.generateFileId();
						ids.put(id.getId(), id.getId());
					}
				}
			});
		}
		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.SECONDS);
		assertEquals(threadCount * count, ids.size());
	}
}
