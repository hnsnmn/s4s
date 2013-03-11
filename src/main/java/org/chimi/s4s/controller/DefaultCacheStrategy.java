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

import javax.servlet.http.HttpServletResponse;

import org.chimi.s4s.fileservice.FileSource;

/**
 * 기본 CacheStragey.
 * 
 * 이미지인 경우 30일 캐시를 설정한다.
 * 
 * @author Choi Beom Kyun
 */
public class DefaultCacheStrategy implements CacheStrategy {

	@Override
	public void setCacheOption(FileSource source, HttpServletResponse response) {
		if (source.isImage()) {
			response.setHeader("Pragma", null);
			response.setHeader("Expires", null);
			response.setHeader("Cache-Control",
					"max-age=2592000, must-revalidate"); // 30일
			response.setDateHeader("Last-Modified", System.currentTimeMillis());
		}
	}

}
