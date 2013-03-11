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
import java.io.IOException;

import org.chimi.s4s.storage.FileData;

/**
 * 썸네일 생성기 인터페이스
 * 
 * @author Choi Beom Kyun
 */
public interface ThumbnailCreator {

	/**
	 * fileData의 이미지 데이터를 이용해서 size 크기의 썸네일 파일을 생성한 뒤, 해당 파일 정보를 리턴한다.
	 * 
	 * @param fileData
	 * @param size
	 * @return
	 * @throws IOException
	 */
	File create(FileData fileData, Size newSize) throws IOException;

}
