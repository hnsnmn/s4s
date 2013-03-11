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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.chimi.s4s.fileservice.Size;
import org.chimi.s4s.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mortennobel.imagescaling.ResampleOp;

/**
 * Java API를 이용한 ImageProcessor 구현 클래스
 * 
 * @author Choi Beom Kyun
 */
public class JavaImageProcessor implements ImageProcessor {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * ImageIO와 Java Image Scaling 모듈의 ResampleOp를 이용해서 이미지 크기를 변경한다.
	 */
	@Override
	public File resize(InputStream in, File destFile, Size newSize)
			throws ImageProcessingException {
		BufferedImage image = readImage(in);
		BufferedImage rescaledImage = rescaleImage(newSize, image);
		return writeRescaledImage(in, destFile, rescaledImage);
	}

	private File writeRescaledImage(InputStream in, File destFile,
			BufferedImage rescaledImage) throws ImageProcessingException {
		File outFile = new File(destFile.getAbsolutePath());
		OutputStream os = null;
		try {
			os = new FileOutputStream(outFile);
			ImageIO.write(rescaledImage, "jpeg", os);
			if (logger.isDebugEnabled()) {
				logger.debug("Thumbnail Image {} created", destFile
						.getAbsolutePath());
			}
			return outFile;
		} catch (IOException e) {
			throw new ImageProcessingException("Fail to write rescaled image:"
					+ e.getMessage(), e);
		} finally {
			Util.close(os);
			Util.close(in);
		}
	}

	private BufferedImage rescaleImage(Size newSize, BufferedImage image) {
		ResampleOp op = new ResampleOp(newSize.getWidth(), newSize.getHeight());
		BufferedImage rescaledImage = op.filter(image, null);
		return rescaledImage;
	}

	private BufferedImage readImage(InputStream in)
			throws ImageProcessingException {
		BufferedImage image = null;
		try {
			image = ImageIO.read(in);
		} catch (IOException e) {
			throw new ImageProcessingException("Fail to image read:"
					+ e.getMessage(), e);
		}
		return image;
	}

}
