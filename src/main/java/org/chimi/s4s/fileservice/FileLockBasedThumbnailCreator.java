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
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.Random;

import org.chimi.s4s.imageprocessor.ImageProcessor;
import org.chimi.s4s.storage.FileData;
import org.chimi.s4s.storage.FileId;
import org.chimi.s4s.util.Util;
import org.springframework.beans.factory.InitializingBean;

/**
 * 동일 파일에 대한 썸네일 생성 요청을 처리하기 위해 파일 락을 사용하는 썸네일 생성기
 * 
 * 실제 이미지 처리는 ImageProcessor에게 위임한다.
 * 
 * @author Choi Beom Kyun
 */
public class FileLockBasedThumbnailCreator implements ThumbnailCreator,
		InitializingBean {

	private ImageProcessor imageProcessor;
	private String tempStoragePath = Util.getS4STempDir();

	public void setImageProcessor(ImageProcessor imageProcessor) {
		this.imageProcessor = imageProcessor;
	}

	public void setTempStoragePath(String tempStoragePath) {
		this.tempStoragePath = tempStoragePath;
	}

	/**
	 * tempStoragePath 임시 디렉토리를 생성한다.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		File tempFolder = new File(tempStoragePath);
		tempFolder.mkdirs();
	}

	@Override
	public File create(FileData fileData, Size newSize) throws IOException {
		String thumbnailPath = getThumbnailPath(fileData.getFileId(), newSize);
		File thumbnailFile = new File(thumbnailPath);
		if (thumbnailFile.exists()) {
			// 이미지 썸네일 존재
			return thumbnailFile;
		}
		File lockFile = getLockFile(thumbnailPath);
		FileChannel channel = new RandomAccessFile(lockFile, "rw").getChannel();
		FileLock lock = null;
		try {
			lock = channel.tryLock();
		} catch (OverlappingFileLockException ex) {
		}

		if (lock == null) {
			// 락을 구하지 못했으므로, 1회용 사용 목적의 썸네일 생성 및 리턴
			return createTemporaryThumbnail(thumbnailPath, fileData, newSize);
		}
		try {
			createThumbnail(thumbnailPath, fileData, newSize);
			return thumbnailFile;
		} finally {
			lock.release();
			channel.close();

			// TODO 락 파일 제거 스킴 필요
		}
	}

	private File getLockFile(String thumbnailPath) {
		return new File(thumbnailPath + ".lock");
	}

	private String getThumbnailPath(FileId fileId, Size size) {
		String filename = fileId.toString() + ".w" + size.getWidth() + "h"
				+ size.getHeight() + ".jpg";
		return tempStoragePath + Util.getFileSeparator() + filename;
	}

	private File createThumbnail(String thumbnailPath, FileData fileData,
			Size newSize) throws IOException {
		File thumbnailFile = new File(thumbnailPath);
		if (thumbnailFile.exists()) {
			return thumbnailFile;
		}
		return imageProcessor.resize(fileData.getInputStream(), thumbnailFile,
				newSize);
	}

	private Random random = new Random();

	private File createTemporaryThumbnail(String thumbnailPath,
			FileData fileData, Size newSize) throws IOException {
		int tempId = random.nextInt(10000);
		String tempThumbnailPath = thumbnailPath + ".temp" + tempId + ".jpg";
		return imageProcessor.resize(fileData.getInputStream(), new File(
				tempThumbnailPath), newSize);
	}

}
