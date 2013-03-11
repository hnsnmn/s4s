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
package org.chimi.s4s.storage.localfs;

import org.chimi.s4s.storage.FileId;

/**
 * FileId로부터 파일이 저장될 디렉터리 경로를 구해준다.
 * 
 * @author Choi Beom Kyun
 */
public abstract class FilePathUtil {

	/**
	 * fileId로부터 해당 파일이 보관된 디렉터리 경로를 구한다.
	 * 
	 * @param fileId
	 * @return
	 */
	public static String getDirectoryPath(FileId fileId) {
		String idValue = fileId.toString();
		return idValue.substring(0, 6) + System.getProperty("file.separator")
				+ idValue.substring(6, 8);
	}
}
