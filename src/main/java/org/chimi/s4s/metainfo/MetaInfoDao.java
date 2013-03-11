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
package org.chimi.s4s.metainfo;

/**
 * @author Choi Beom Kyun
 */
public interface MetaInfoDao {

	/**
	 * 메타 정보를 DB에 삽입하고 생성된 키 값을 리턴한다.
	 * 
	 * @param metadata
	 * @return 메타 정보 식별 값
	 * @throws IllegalArgumentException
	 *             metadata 값이 잘못된 경우 에러를 발생시킨다.
	 */
	public String insert(FileMetadata metadata) throws IllegalArgumentException;

	/**
	 * 지정한 fileId에 매칭되는 FileMetadata를 구한다. 존재하지 않을 경우 null을 리턴한다.
	 * 
	 * @param fileId
	 * @return
	 */
	public FileMetadata selectByFileId(String fileId);

}
