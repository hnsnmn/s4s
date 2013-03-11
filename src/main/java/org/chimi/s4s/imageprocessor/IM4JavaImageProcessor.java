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
import java.io.IOException;
import java.io.InputStream;

import org.chimi.s4s.fileservice.Size;
import org.chimi.s4s.util.Util;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.im4java.process.ProcessStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * IM4Java를 이용한 이미지 처리기
 * 
 * @author Choi Beom Kyun
 * 
 */
public class IM4JavaImageProcessor implements ImageProcessor, InitializingBean {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private String programPath;

	public void setProgramPath(String programPath) {
		this.programPath = programPath;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (programPath == null || programPath.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("IM4Java program path value is not set.");
			}
			return;
		}
		if (logger.isInfoEnabled()) {
			logger.info("IM4Java program path value is {}.", programPath);
		}
		ProcessStarter.setGlobalSearchPath(programPath);
	}

	@Override
	public File resize(InputStream in, File destFile, Size newSize)
			throws ImageProcessingException {
		try {
			IMOperation op = new IMOperation();
			op.format("jpeg");
			op.addImage("-"); // read from stdin
			op.resize(newSize.getWidth(), newSize.getHeight(), "!");
			op.addImage(destFile.getAbsolutePath());
			Pipe pipeIn = new Pipe(in, null);

			// set up command
			ConvertCmd convert = new ConvertCmd(true);
			convert.setInputProvider(pipeIn);
			try {
				convert.run(op);
				if (logger.isDebugEnabled()) {
					logger.debug("Thumbnail Image {} created", destFile
							.getAbsolutePath());
				}
				return destFile;
			} catch (InterruptedException e) {
				throw new ImageProcessingException("Fail to resize:"
						+ e.getMessage(), e);
			} catch (IM4JavaException e) {
				throw new ImageProcessingException("Fail to resize:"
						+ e.getMessage(), e);
			} catch (IOException e) {
				throw new ImageProcessingException("Fail to resize:"
						+ e.getMessage(), e);
			}
		} finally {
			Util.close(in);
		}
	}

}
