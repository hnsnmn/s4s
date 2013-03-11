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
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.chimi.s4s.util.Util;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.MultiStepRescaleOp;
import com.mortennobel.imagescaling.ResampleOp;
import com.mortennobel.imagescaling.ThumpnailRescaleOp;

/**
 * @author Choi Beom Kyun
 */
public class JavaImageScalingTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void multiStepRescaleOp() throws IOException {
		resize(new MultiStepRescaleOp(200, 100), "test-t1.jpg");
	}

	@Test
	public void resampleOp() throws IOException {
		resize(new ResampleOp(200, 100), "test-t2.jpg");
	}

	@Test
	public void thumpnailRescaleOp() throws IOException {
		resize(new ThumpnailRescaleOp(200, 100), "test-t3.jpg");
	}

	private void resize(AdvancedResizeOp op, String filename)
			throws IOException {
		BufferedImage image = ImageIO.read(getClass().getResource("/test.jpg"));
		BufferedImage rescaledTomato = op.filter(image, null);

		File folder = new File(Util.getTempDir(), "s4stest");
		folder.mkdir();

		File outFilename = new File(folder, filename);
		OutputStream os = new FileOutputStream(outFilename);
		ImageIO.write(rescaledTomato, "jpeg", os);
		os.close();

		logger.info(outFilename.getAbsolutePath() + " created.");
	}
}
