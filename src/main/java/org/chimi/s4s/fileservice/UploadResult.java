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


/**
 * FileService.save()의 결과 값
 * 
 * @author Choi Beom Kyun
 */
public class UploadResult {

	private String fileName;
	private String fileId;
	private String metadataId;
	private String mimeType;
	private long length;

	public UploadResult(String fileName, String fileId, String metadataId,
			String mimeType, long length) {
		this.fileName = fileName;
		this.fileId = fileId;
		this.metadataId = metadataId;
		this.mimeType = mimeType;
		this.length = length;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileId() {
		return fileId;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public String getMimeType() {
		return mimeType;
	}

	public long getLength() {
		return length;
	}

	public boolean isImage() {
		return mimeType == null ? false : mimeType.startsWith("image/");
	}
}
