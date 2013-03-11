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
package org.chimi.s4s.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * MIME 관련 유틸리티
 * 
 * @author Choi Beom Kyun
 */
public abstract class MimeUtil {

	private static final String DEFAULT_MIMETYPE = "application/octet-stream";
	private static Map<String, String> mimeMap = new HashMap<String, String>();

	static {
		mimeMap.put("gif", "image/gif");
		mimeMap.put("jpeg", "image/jpeg");
		mimeMap.put("jpe", "image/jpeg");
		mimeMap.put("jpg", "image/jpeg");
		mimeMap.put("png", "image/x-png");
		mimeMap.put("bmp", "image/x-ms-bmp");
	}

	public static String getMimeType(String fileName) {
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex == -1 || lastDotIndex == 0
				|| lastDotIndex == fileName.length() - 1) {
			return DEFAULT_MIMETYPE;
		}
		String extension = fileName.substring(lastDotIndex + 1).toLowerCase();
		String mimeType = mimeMap.get(extension);
		if (mimeType != null) {
			return mimeType;
		}
		return DEFAULT_MIMETYPE;
	}

}
