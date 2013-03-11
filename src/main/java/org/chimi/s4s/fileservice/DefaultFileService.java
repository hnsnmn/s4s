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
import java.util.Date;

import org.chimi.s4s.metainfo.FileMetadata;
import org.chimi.s4s.metainfo.MetaInfoDao;
import org.chimi.s4s.storage.FileData;
import org.chimi.s4s.storage.FileId;
import org.chimi.s4s.storage.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileService의 기본 구현 클래스
 * 
 * @author Choi Beom Kyun
 */
public class DefaultFileService implements FileService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private FileStorage fileStorage;
	private MetaInfoDao metaInfoDao;
	private ThumbnailCreator thumbnailCreator;

	public void setFileStorage(FileStorage fileStorage) {
		this.fileStorage = fileStorage;
	}

	public void setMetaInfoDao(MetaInfoDao metaInfoDao) {
		this.metaInfoDao = metaInfoDao;
	}

	public void setThumbnailCreator(ThumbnailCreator thumbnailCreator) {
		this.thumbnailCreator = thumbnailCreator;
	}

	@Override
	public UploadResult save(UploadFile uf) {
		FileId fileId = fileStorage.save(uf.getFile());
		String metadataId = insertMetadata(uf, fileId);

		if (logger.isDebugEnabled()) {
			logger.debug("{} file saved. [fileId={}, metadataId={}]",
					new Object[] { uf.getFilename(), fileId.toString(),
							metadataId });
		}

		return new UploadResult(uf.getFilename(), fileId.toString(),
				metadataId, uf.getMimeType(), uf.getLength());
	}

	private String insertMetadata(UploadFile uf, FileId fileId) {
		FileMetadata metadata = new FileMetadata(uf.getServiceId(), uf
				.getFilename(), uf.getLength(), uf.getMimeType(), new Date(),
				fileId.toString());
		return metaInfoDao.insert(metadata);
	}

	@Override
	public FileSource getFile(String fileId) {
		FileInfo fileInfo = getFileInfo(fileId);
		if (fileInfo == null) {
			return null;
		}
		return new DefaultFileSource(fileInfo.metadata, fileInfo.fileData);
	}

	@Override
	public FileSource getThumbnailImage(String fileId, Size size) {
		FileInfo fileInfo = getFileInfo(fileId);
		if (fileInfo == null) {
			return null;
		}
		if (!fileInfo.metadata.isImage()) {
			return null;
		}
		try {
			File thumbnailFile = thumbnailCreator.create(fileInfo.fileData,
					size);

			if (logger.isDebugEnabled()) {
				logger.debug("Thumbnail image of FileId {} created in {}]", fileId,
						thumbnailFile.getAbsolutePath());
			}

			return new ThumbnailFileSource(thumbnailFile);
		} catch (Exception e) {
			logger.error("Fail to create thumbnail[w:" + size.getWidth()
					+ ", h:" + size.getHeight() + "] for FileID[" + fileId
					+ "].", e);
			// TODO 에러에 대한 알맞은 예외를 던저야 할 지 결정 필요
			throw new RuntimeException(e);
		}
	}

	private FileInfo getFileInfo(String fileId) {
		FileMetadata metadata = metaInfoDao.selectByFileId(fileId);
		if (metadata == null) {
			return null;
		}
		FileData fileData = fileStorage.find(new FileId(fileId));
		if (fileData == null) {
			if (logger.isWarnEnabled()) {
				logger
						.warn(
								"FileMetadata for fileId {} exists but FileData is not found.",
								fileId);
			}
			return null;
		}
		return new FileInfo(metadata, fileData);
	}

	private class FileInfo {
		FileMetadata metadata;
		FileData fileData;

		FileInfo(FileMetadata metadata, FileData fileData) {
			this.metadata = metadata;
			this.fileData = fileData;
		}
	}

}
