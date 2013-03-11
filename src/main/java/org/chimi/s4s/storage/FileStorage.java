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
package org.chimi.s4s.storage;

import java.io.File;

/**
 * 파일 스토리지 인터페이스
 * 
 * @author Choi Beom Kyun
 */
public interface FileStorage {

	/**
	 * 스토리지에 파일을 저장한다.
	 * 
	 * @param file
	 * @return
	 * @throws SaveFailureStorageException
	 */
	FileId save(File file) throws SaveFailureStorageException;

	/**
	 * 스토리지에서 지정한 ID에 해당하는 FileData를 구한다. 존재하지 않을 경우 null을 리턴한다.
	 * 
	 * @param fileId
	 * @return
	 */
	FileData find(FileId fileId);

}
