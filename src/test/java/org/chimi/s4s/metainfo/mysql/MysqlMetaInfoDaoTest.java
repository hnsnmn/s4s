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
package org.chimi.s4s.metainfo.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.chimi.s4s.metainfo.FileMetadata;
import org.chimi.s4s.metainfo.MetaInfoDao;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Choi Beom Kyun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-properties.xml",
		"classpath:metainfo/spring-metainfo-mysql.xml" })
public class MysqlMetaInfoDaoTest {

	@Autowired
	private MetaInfoDao metaInfoDao;

	@Autowired
	private DataSource dataSource;

	@BeforeClass
	public static void init() {
		System.setProperty("metainfo.db", "mysql");
	}

	@Test
	public void insertAndSelectByFileId() throws Throwable {
		FileMetadata metadata = createFileMetaData();
		String id = metaInfoDao.insert(metadata);

		assertNotNull(id);
		assertData(id, metadata);

		FileMetadata metadatabyFileId = metaInfoDao.selectByFileId(metadata
				.getFileId());
		assertNotNull(metadatabyFileId);

		assertData(metadatabyFileId, metadata);
	}

	private void assertData(FileMetadata md, FileMetadata metadata) {
		assertEquals(metadata.getServiceId(), md.getServiceId());
		assertEquals(metadata.getFileName(), md.getFileName());
		assertEquals(metadata.getLength(), md.getLength());
		assertEquals(metadata.getMimetype(), md.getMimetype());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		assertEquals(format.format(metadata.getUploadTime()), format.format(md
				.getUploadTime()));
		assertEquals(metadata.getFileId(), md.getFileId());
	}

	private void assertData(String id, FileMetadata metadata) throws Throwable {
		Connection conn = dataSource.getConnection();
		PreparedStatement pstmt = conn
				.prepareStatement("select * from FILE_METADATA where FILE_METADATA_ID = ?");
		int idValue = Integer.parseInt(id);
		pstmt.setInt(1, idValue);
		ResultSet rs = pstmt.executeQuery();

		assertTrue(rs.next());
		assertEquals(idValue, rs.getInt("FILE_METADATA_ID"));
		assertEquals(metadata.getServiceId(), rs.getString("SERVICE_ID"));
		assertEquals(metadata.getFileName(), rs.getString("FILE_NAME"));
		assertEquals(metadata.getLength(), rs.getLong("FILE_LENGTH"));
		assertEquals(metadata.getMimetype(), rs.getString("MIMETYPE"));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		assertEquals(format.format(metadata.getUploadTime()), format.format(rs
				.getTimestamp("UPLOAD_TIME")));
		assertEquals(metadata.getFileId(), rs.getString("FILE_ID"));

		rs.close();
		pstmt.close();
		conn.close();
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
