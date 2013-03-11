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
package org.chimi.s4s.storage.mongofs;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bson.types.ObjectId;
import org.chimi.s4s.util.Util;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * @author Choi Beom Kyun
 */
public class GridFSTest {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void saveAndFind() throws MongoException, IOException {
		Mongo mongo = new Mongo();
		GridFS gridfs = new GridFS(mongo.getDB("localfs"));

		Object fileId = saveFile(gridfs);
		File loadedFile = loadFile(gridfs, fileId);
		boolean same = testSame(loadedFile);
		assertTrue(same);
	}

	private boolean testSame(File loadedFile) throws FileNotFoundException,
			IOException {
		// 두 파일이 같은 지 비교
		InputStream source = null;
		InputStream copied = null;

		boolean same = true;
		try {
			int sourceData = -1;
			int copiedData = -1;

			source = new FileInputStream("src/test/resources/test.jpg");
			copied = new FileInputStream(loadedFile);
			do {
				sourceData = source.read();
				copiedData = copied.read();

				if (sourceData == -1 && copiedData != -1) {
					// 길이 다름
					same = false;
					break;
				}
				if (sourceData != -1 && copiedData == -1) {
					// 길이 다름
					same = false;
					break;
				}
				if (sourceData == -1 && copiedData == -1) {
					// 길이 같음
					break;
				}
				if (sourceData != copiedData) {
					// 데이터가 다름
					same = false;
					break;
				}
			} while (true);
		} finally {
			Util.close(source);
			Util.close(copied);
		}
		return same;
	}

	private File loadFile(GridFS gridfs, Object fileId) throws IOException,
			FileNotFoundException {
		ObjectId id = new ObjectId(fileId.toString());
		GridFSDBFile foundFile = gridfs.find(id);
		File tempFile = new File(Util.getTempDir(), "s4stest/found.jpg");
		FileCopyUtils.copy(foundFile.getInputStream(), new FileOutputStream(
				tempFile));
		logger.info(tempFile.getAbsolutePath() + " creaed");
		return tempFile;
	}

	private Object saveFile(GridFS gridfs) throws IOException {
		GridFSInputFile gridFile = gridfs.createFile(new File(
				"src/test/resources/test.jpg"));
		gridFile.save();
		Object fileId = gridFile.getId();
		return fileId;
	}
}
