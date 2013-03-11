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

import java.io.File;

/**
 * 업로드 파일 정보
 * 
 * @author Choi Beom Kyun
 * 
 */
public class UploadFile {

	private String serviceId;
	private String filename;
	private String mimeType;
	private File file;
	private long length;

	public UploadFile(String serviceId, String filename, String mimeType,
			File file, long length) {
		this.serviceId = serviceId;
		this.filename = filename;
		this.mimeType = mimeType;
		this.file = file;
		this.length = length;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getFilename() {
		return filename;
	}

	public String getMimeType() {
		return mimeType;
	}

	public File getFile() {
		return file;
	}

	public long getLength() {
		return length;
	}

}
