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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bson.types.ObjectId;
import org.chimi.s4s.storage.FileData;
import org.chimi.s4s.storage.FileId;

import com.mongodb.gridfs.GridFSDBFile;

/**
 * MongoFS의 GridFSDBFile로부터 데이터를 제공하는 FileData 구현 클래스
 * 
 * @author Choi Beom Kyun
 */
public class MongoFSFileData implements FileData {

	private ObjectId id;
	private GridFSDBFile gridFile;

	public MongoFSFileData(ObjectId id, GridFSDBFile gridFile) {
		this.id = id;
		this.gridFile = gridFile;
	}

	@Override
	public FileId getFileId() {
		return new FileId(id.toString());
	}

	@Override
	public void write(OutputStream out) throws IOException {
		gridFile.writeTo(out);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return gridFile.getInputStream();
	}

}
