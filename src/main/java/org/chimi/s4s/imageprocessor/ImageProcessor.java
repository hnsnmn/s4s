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
package org.chimi.s4s.imageprocessor;

import java.io.File;
import java.io.InputStream;

import org.chimi.s4s.fileservice.Size;

/**
 * 이미지 처리기 인터페이스
 * 
 * @author Choi Beom Kyun
 */
public interface ImageProcessor {

	/**
	 * inputStream의 이미지의 크기를 newSize로 변환하고 그 결과를 destFile로 저장한다.
	 * 
	 * 처리가 끝나면 in 스트림을 close 한다.
	 * 
	 * @param in
	 * @param destFile
	 * @param newSize
	 * @return
	 * @throws ImageProcessingException
	 */
	File resize(InputStream in, File destFile, Size newSize)
			throws ImageProcessingException;

}
