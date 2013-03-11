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
 * @author Choi Beom Kyun
 */
public interface FileService {

	/**
	 * 파일을 스토로지에 저장하고 결과를 리턴한다.
	 * 
	 * @param uf
	 * @return
	 */
	UploadResult save(UploadFile uf);

	/**
	 * fileId에 해당하는 FileSource를 구한다. 존재하지 않을 경우 null을 리턴한다.
	 * 
	 * @param fileId
	 * @return
	 */
	FileSource getFile(String fileId);

	/**
	 * fileId에 해당하는 이미지 파일의 썸네일 이미지를 구한다.
	 * 
	 * @param fileId
	 * @param size
	 * @return
	 */
	FileSource getThumbnailImage(String fileId, Size size);
}
