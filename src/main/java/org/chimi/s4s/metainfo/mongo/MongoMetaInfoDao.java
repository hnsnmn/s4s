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

import javax.annotation.PostConstruct;

import org.chimi.s4s.metainfo.FileMetadata;
import org.chimi.s4s.metainfo.MetaInfoDao;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

/**
 * MongoDB metainfo DAO
 *
 * @author Lee Kyu Hwa
 * @version 2011. 1. 10.
 */
public class MongoMetaInfoDao implements MetaInfoDao {

	private DB db;
	private DBCollection dbCollection;

	private Mongo mongo;
	private String dbName;
	private String collectionName;
	
	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@PostConstruct
	public void init() {
		db = mongo.getDB(dbName);
		dbCollection = db.getCollection(collectionName);
	}
	
	public void setDb(DB db) {
		this.db = db;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public void setDbCollection(DBCollection dbCollection) {
		this.dbCollection = dbCollection;
	}

	@Override
	public String insert(FileMetadata metadata) throws IllegalArgumentException {
		DBObject dbObject = getDbObject(new MongoFileMetadata(metadata));
		dbCollection.save(dbObject);
		return dbObject.get("_id").toString();
	}

	@Override
	public FileMetadata selectByFileId(String fileId) {
		DBObject q = BasicDBObjectBuilder.start().add("FILE_ID", fileId).get();
		DBObject doc = dbCollection.findOne(q);
		
		return convertMetaInfo(doc);
	}
	
	/**
	 * @param doc
	 * @return
	 */
	private FileMetadata convertMetaInfo(DBObject doc) {
		if (doc == null) { 
			return null;
		} else {
			MongoFileMetadata meta = new MongoFileMetadata();
			meta.setFileId((String) doc.get("FILE_ID"));
			meta.setFileName((String) doc.get("FILE_NAME"));
			meta.setId(doc.get("_id").toString());
			meta.setLength(Long.parseLong(doc.get("FILE_LENGTH").toString()));
			meta.setMimetype((String) doc.get("MIMETYPE"));
			meta.setServiceId((String) doc.get("SERVICE_ID"));
			meta.setUploadTime(doc.get("UPLOAD_TIME").toString());
			return meta;
		}
	}

	/**
	 * @param metadata
	 * @return
	 */
	private DBObject getDbObject(MongoFileMetadata metadata) {
		DBObject dbObject = (DBObject) JSON.parse(metadata.toString());
		return dbObject;
	}
}
