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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.chimi.s4s.metainfo.FileMetadata;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Mongo File Metadata
 * 
 * @author Lee Kyu Hwa
 * @version 2011. 1. 10.
 */
public class MongoFileMetadata extends FileMetadata {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private Integer physicalId;

	public MongoFileMetadata() {
	}

	public MongoFileMetadata(FileMetadata fm) {
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
	
	public void setUploadTime(String uploadTimeString) {
		try {
			setUploadTime(dateFormat.parse(uploadTimeString));
		} catch (ParseException e) {
			e.printStackTrace();
		};
	}
	
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		try {
			json.put("SERVICE_ID", getServiceId());
			json.put("FILE_NAME", getFileName());
			json.put("FILE_LENGTH", getLength());
			json.put("MIMETYPE", getMimetype());
			json.put("FILE_ID", getFileId());
			json.put("UPLOAD_TIME", dateFormat.format(getUploadTime()));
			
			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return json.toString();
		} 
	}
}
