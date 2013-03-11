/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chimi.s4s.fileservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.chimi.s4s.imageprocessor.ImageProcessor;
import org.chimi.s4s.storage.FileData;
import org.chimi.s4s.storage.FileId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * FileLockBasedThumbnailCreator 클래스의 락 테스트
 * 
 * @author Choi Beom Kyun
 */
@RunWith(MockitoJUnitRunner.class)
public class FileLockBasedThumbnailCreatorLockTest {

	@Mock
	private ImageProcessor imageProcessor;

	@Test
	public void lockTest() throws Exception {
		final AtomicInteger counter = new AtomicInteger(1);
		Mockito.when(
				imageProcessor.resize(Mockito.any(InputStream.class), Mockito
						.any(File.class), Mockito.any(Size.class))).thenAnswer(
				new Answer<File>() {

					@Override
					public File answer(InvocationOnMock invocation)
							throws Throwable {
						// 1회 호출에 대해서 5초간 슬립 후 처리
						if (counter.getAndIncrement() == 1) {
							Thread.sleep(3000);
						}
						// 2회 이후 호출에 대해선 바로 처리
						File destFile = (File) invocation.getArguments()[1];
						return destFile;
					}
				});

		final FileLockBasedThumbnailCreator creator = new FileLockBasedThumbnailCreator();
		creator.setImageProcessor(imageProcessor);
		creator.afterPropertiesSet();

		// 30개의 쓰레드를 이용해서 creator.create() 호출
		// 단, 내부적으로 3초 후 create()를 호출하므로
		// 아래 1회 호출 이후에 create()를 호출하게 된다.
		// 따라서, 락 차지 실패하고, 임시 파일 생성 로직을 수행함

		final FileData fileData = Mockito.mock(FileData.class);
		Mockito.when(fileData.getFileId()).thenReturn(new FileId("test"));
		Mockito.when(fileData.getInputStream()).thenReturn(
				new ByteArrayInputStream(new byte[4]));
		final Size newSize = new Size(10, 10);

		final List<File> tempThumbnailFiles = new ArrayList<File>();
		ExecutorService service = Executors.newFixedThreadPool(30);
		for (int i = 1; i <= 30; i++) {
			service.submit(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					try {
						tempThumbnailFiles.add(creator
								.create(fileData, newSize));
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

			});
		}

		// 1회 호출 (이 쓰레드가 락 차지)
		File firstFile = creator.create(fileData, newSize);
		assertEquals("test.w10h10.jpg", firstFile.getName());

		service.awaitTermination(4, TimeUnit.SECONDS);
		// 2회 31회 호출
		assertEquals(30, tempThumbnailFiles.size());
		for (File tempFile : tempThumbnailFiles) {
			assertTrue(tempFile.getName().contains("test.w10h10.jpg.temp"));
		}
	}
}
