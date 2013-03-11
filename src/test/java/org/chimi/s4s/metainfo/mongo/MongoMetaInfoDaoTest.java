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
package org.chimi.s4s.metainfo.mongo;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.chimi.s4s.metainfo.FileMetadata;
import org.chimi.s4s.metainfo.MetaInfoDao;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author Lee Kyu Hwa
 * @version 2011. 1. 10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-properties.xml",
		"classpath:metainfo/spring-metainfo-mongo.xml" })
public class MongoMetaInfoDaoTest {

	@Autowired
	private MetaInfoDao metaInfoDao;

	@BeforeClass
	public static void init() {
		System.setProperty("metainfo.db", "mongo");
	}

	@Test
	public void insertAndSelectByFileId() throws Throwable {
		FileMetadata metadata = createFileMetaData();
		String id = metaInfoDao.insert(metadata);
		assertNotNull(id);

		MongoFileMetadata metadatabyFileId = (MongoFileMetadata) metaInfoDao
				.selectByFileId(metadata.getFileId());
		assertNotNull(metadatabyFileId);
	}

	private FileMetadata createFileMetaData() {
		FileMetadata data = new FileMetadata();
		data.setServiceId("serviceId");
		data.setFileName("파일이름.txt");
		data.setLength(1000);
		data.setMimetype("text/plain");
		data.setUploadTime(new Date());
		data.setFileId("FILEID" + System.currentTimeMillis());
		return data;
	}
}
