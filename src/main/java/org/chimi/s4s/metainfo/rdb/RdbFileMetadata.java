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

import org.chimi.s4s.metainfo.FileMetadata;

/**
 * @author Choi Beom Kyun
 */
public class RdbFileMetadata extends FileMetadata {

	private Integer physicalId;

	public RdbFileMetadata() {
	}

	RdbFileMetadata(FileMetadata fm) {
		super.setId(fm.getId());
		setServiceId(fm.getServiceId());
		setFileName(fm.getFileName());
		setLength(fm.getLength());
		setMimetype(fm.getMimetype());
		setUploadTime(fm.getUploadTime());
		setFileId(fm.getFileId());
	}

	public Integer getPhysicalId() {
		return physicalId;
	}

	public void setPhysicalId(Integer physicalId) {
		this.physicalId = physicalId;
	}

	@Override
	public String getId() {
		return physicalId.toString();
	}

	@Override
	public void setId(String id) {
		throw new UnsupportedOperationException();
	}

}
