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
package org.chimi.s4s.metainfo.rdb;

import java.util.List;

import org.chimi.s4s.metainfo.FileMetadata;
import org.chimi.s4s.metainfo.MetaInfoDao;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Choi Beom Kyun
 */
@Repository
public class RdbMetaInfoDao implements MetaInfoDao {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	@Override
	public String insert(FileMetadata metadata) {
		// TODO metadata 값 검증 코드
		RdbFileMetadata mysqlMetadata = new RdbFileMetadata(metadata);
		sessionFactory.getCurrentSession().save(mysqlMetadata);
		return mysqlMetadata.getPhysicalId().toString();
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public FileMetadata selectByFileId(String fileId) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from RdbFileMetadata md where md.fileId = :fileId");
		query.setString("fileId", fileId);
		List list = query.list();
		if (list.isEmpty()) {
			return null;
		}
		return (FileMetadata) list.get(0);
	}

}
