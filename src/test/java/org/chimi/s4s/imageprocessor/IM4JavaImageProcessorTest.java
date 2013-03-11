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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.chimi.s4s.fileservice.Size;
import org.chimi.s4s.util.Util;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Choi Beom Kyun
 */
public class IM4JavaImageProcessorTest {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void emptyProgramPath() throws Exception {
		File resized = resize("");
		assertTrue(resized.exists());
		logger.info("Path of created thumbnail: {}", resized.getAbsolutePath());
	}

	private File resize(String programPath) throws Exception {
		IM4JavaImageProcessor processor = new IM4JavaImageProcessor();
		processor.setProgramPath(programPath);
		processor.afterPropertiesSet();
		File folder = new File(Util.getTempDir(), "s4stest");
		folder.mkdir();
		File outFile = new File(folder, "test-im4java-processor.jpg");
		Size newSize = new Size(200, 100);
		return processor.resize(getClass().getResourceAsStream("/test.jpg"),
				outFile, newSize);
	}

	@Test
	public void invalidProgramPath() throws Exception {
		try {
			resize("c:\\invalid\\execute");
			fail("ImageProcessingException must occured");
		} catch (ImageProcessingException ex) {
		}
	}
}
