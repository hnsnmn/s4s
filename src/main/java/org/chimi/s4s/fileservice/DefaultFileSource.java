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

import java.io.IOException;
import java.io.OutputStream;

import org.chimi.s4s.metainfo.FileMetadata;
import org.chimi.s4s.storage.FileData;

/**
 * 기본 FileSource 구현 클래스
 * 
 * @author Choi Beom Kyun
 */
public class DefaultFileSource implements FileSource {

	private FileMetadata metadata;
	private FileData fileData;

	public DefaultFileSource(FileMetadata metadata, FileData fileData) {
		this.metadata = metadata;
		this.fileData = fileData;
	}

	@Override
	public String getFileName() {
		return metadata.getFileName();
	}

	@Override
	public long getLength() {
		return metadata.getLength();
	}

	@Override
	public String getMimeType() {
		return metadata.getMimetype();
	}

	@Override
	public boolean isImage() {
		return metadata.isImage();
	}

	@Override
	public void write(OutputStream out) throws IOException {
		fileData.write(out);
	}

}
