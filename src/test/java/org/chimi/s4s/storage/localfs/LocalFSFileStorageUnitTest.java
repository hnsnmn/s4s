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
package org.chimi.s4s.storage.localfs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.chimi.s4s.storage.FileData;
import org.chimi.s4s.storage.FileId;
import org.chimi.s4s.util.Util;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

/**
 * LocalFSFileStorage 클래스에 대한 단위 테스트
 * 
 * @author Choi Beom Kyun
 */
public class LocalFSFileStorageUnitTest {

	private LocalFSFileStorage storage = new LocalFSFileStorage();
	private String basePath = Util.getTempDir();

	@Before
	public void setup() {
		storage.setBasePath(basePath);
	}

	@Test
	public void save() {
		File file = new File("src/test/resources/temp.sql");
		FileId fileId = storage.save(file);

		// 파일 존재 여부 테스트
		String separator = System.getProperty("file.separator");
		String filePath = FilePathUtil.getDirectoryPath(fileId) + separator
				+ fileId.toString();
		File savedFile = new File(basePath, filePath);
		assertTrue(savedFile.exists());
		assertTrue(savedFile.isFile());
		assertTrue(savedFile.canRead());
	}

	@Test
	public void find() throws IOException {
		// 테스트 준비
		File srcFile = new File("src/test/resources/temp.sql");
		FileId fileId = new FileId(new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date())
				+ ".000");
		File destDir = new File(basePath, FilePathUtil.getDirectoryPath(fileId));
		destDir.mkdirs();

		File destFile = new File(destDir, fileId.toString());
		FileCopyUtils.copy(srcFile, destFile);

		// 테스트
		FileData found = storage.find(fileId);
		assertNotNull(found);
		assertEquals(fileId, found.getFileId());

		assertEquals(srcFile.length(), destFile.length());

		InputStream src = null;
		InputStream dest = null;
		try {
			src = new FileInputStream(srcFile);
			dest = new FileInputStream(destFile);
			int srcData = -1;
			int destData = -1;
			while ((srcData = src.read()) != -1) {
				destData = dest.read();
				assertEquals(srcData, destData);
			}
		} finally {
			if (src != null)
				src.close();
			if (dest != null)
				dest.close();

		}
	}
}
