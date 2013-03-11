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

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.chimi.s4s.storage.FileId;
import org.chimi.s4s.util.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Choi Beom Kyun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-properties.xml",
		"classpath:storage/spring-storage-localfs.xml" })
public class LocalFSFileStorageTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LocalFSFileStorage storage;

	@Test
	public void save() {
		File file = new File("src/test/resources/temp.sql");
		FileId fileId = storage.save(file);

		// 파일 존재 여부 테스트
		String separator = Util.getFileSeparator();
		String filePath = FilePathUtil.getDirectoryPath(fileId) + separator
				+ fileId.toString();
		File savedFile = new File("/s4s/storage", filePath);
		assertTrue(savedFile.exists());
		assertTrue(savedFile.isFile());
		assertTrue(savedFile.canRead());
		logger.info(savedFile.getAbsolutePath() + " created.");
	}
}
