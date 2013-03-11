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

import java.io.File;
import java.io.IOException;

import org.chimi.s4s.storage.FileData;
import org.chimi.s4s.storage.FileId;
import org.chimi.s4s.storage.FileStorage;
import org.chimi.s4s.storage.SaveFailureStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

/**
 * 로컬 파일 시스템을 파일 스토리지로 사용하는 FileStorage 구현체
 * 
 * @author 최범균
 */
public class LocalFSFileStorage implements FileStorage {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String basePath;

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public FileId save(File file) {
		try {
			FileId id = trySaveAndGetFileId(file);
			return id;
		} catch (IOException e) {
			throw new SaveFailureStorageException(e);
		}
	}

	private FileId trySaveAndGetFileId(File file) throws IOException {
		int maxTryCount = 3;
		int tryCount = 0;
		FileId id = null;
		while (tryCount < maxTryCount) {
			tryCount++;
			try {
				id = generateFileId();
				saveFile(id, file);
				logger.warn(String.format("save file with id[%s] tryCount[%d]",
						id.getId(), tryCount));
				break;
			} catch (AleadySameIDFileExists ex) {
				if (tryCount == maxTryCount) {
					throw new SaveFailureStorageException(ex);
				}
				logger.warn(String.format(
						"fail to save file with id[%s] tryCount[%d] and retry",
						id.getId(), tryCount));
			}
		}
		return id;
	}

	/**
	 * 아래 규칙에 따라 디렉터리를 생성하고 해당 디렉터리에 파일을 보관한다.
	 * <p>
	 * [기준디렉터리]/yyyyMM/dd/
	 * 
	 * @param id
	 * @param file
	 * @throws IOException
	 */
	private void saveFile(FileId id, File file) throws IOException {
		File dir = makeDirectory(id);
		File target = new File(dir, id.toString());
		if (target.isFile() && target.exists()) {
			throw new AleadySameIDFileExists(String.format(
					"Same ID[%s] file[%s] aleady exists.", id.getId(),
					target.getAbsoluteFile()));
		}
		copyFile(file, target);
	}

	private void copyFile(File file, File target) throws IOException {
		FileCopyUtils.copy(file, target);
	}

	private File makeDirectory(FileId id) {
		File dir = makeFileObjectForId(id);
		if (dir.exists()) {
			return dir;
		}
		boolean made = dir.mkdirs();
		if (!made) {
			// TODO 이미 생성되어 실패한 것인지 확인
			// TODO 저장 실패 스토리지 예외 발생 필요
		}
		return dir;
	}

	private File makeFileObjectForId(FileId id) {
		File dir = new File(basePath, FilePathUtil.getDirectoryPath(id));
		return dir;
	}

	/**
	 * 파일 ID는 다음과 같이 구성된다.
	 * <p>
	 * yyyyMMddHHmmssNNNNNNNN
	 * <p>
	 * yyyy-년도. MM-월. dd-날짜. HH-24시간. mm-분. ss-초. [NNNNNNNN]은 랜덤한 16진수 8자리 숫자.
	 * 
	 * @return
	 */
	private FileId generateFileId() {
		return LocalFSFileIdUtil.generateFileId();
	}

	@Override
	public FileData find(FileId id) {
		File dir = makeFileObjectForId(id);
		File file = new File(dir, id.getId());
		if (!file.exists()) {
			return null;
		}
		return new LocalFSFileData(id, file);
	}

}
