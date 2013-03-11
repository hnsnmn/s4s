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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.chimi.s4s.util.Util;

/**
 * 썸네일 이미지 파일에 대한 FileSource
 * 
 * @author Choi Beom Kyun
 */
public class ThumbnailFileSource implements FileSource {

	private File thumFile;

	public ThumbnailFileSource(File thumbnailFile) {
		this.thumFile = thumbnailFile;
	}

	@Override
	public String getFileName() {
		return thumFile.getName();
	}

	@Override
	public long getLength() {
		return thumFile.length();
	}

	@Override
	public String getMimeType() {
		return "image/jpeg";
	}

	@Override
	public boolean isImage() {
		return true;
	}

	@Override
	public void write(OutputStream out) throws IOException {
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(thumFile));
			Util.copy(in, out);
		} finally {
			Util.close(in);
		}
	}

}
