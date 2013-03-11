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

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.bson.types.ObjectId;
import org.chimi.s4s.storage.FileData;
import org.chimi.s4s.storage.FileId;
import org.chimi.s4s.storage.FileStorage;
import org.chimi.s4s.storage.SaveFailureStorageException;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * MongoDB의 GridFS를 스토리지로 사용한다.
 * 
 * @author Choi Beom Kyun
 */
public class MongoFSFileStorage implements FileStorage {

	private Mongo mongo;
	private String db;

	public MongoFSFileStorage(String mongoHost, int mongoPort, int poolSize)
			throws UnknownHostException, MongoException {
		MongoOptions options = new MongoOptions();
		options.connectionsPerHost = poolSize;
		mongo = new Mongo(new ServerAddress(mongoHost, mongoPort), options);
	}

	public void setDb(String db) {
		this.db = db;
	}

	@Override
	public FileData find(FileId fileId) {
		DB storageDb = mongo.getDB(db);
		GridFS gridFS = new GridFS(storageDb);
		ObjectId id = new ObjectId(fileId.toString());
		GridFSDBFile foundFile = gridFS.find(id);

		if (foundFile == null) {
			return null;
		}
		return new MongoFSFileData(id, foundFile);
	}

	@Override
	public FileId save(File file) throws SaveFailureStorageException {
		try {
			DB storageDb = mongo.getDB(db);
			GridFS gridFS = new GridFS(storageDb);
			GridFSInputFile gridFile = gridFS.createFile(file);
			gridFile.save();
			return new FileId(gridFile.getId().toString());
		} catch (IOException e) {
			throw new SaveFailureStorageException(e);
		}
	}

}
