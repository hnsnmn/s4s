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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.chimi.s4s.util.Util;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Choi Beom Kyun
 */
public class IM4JavaScalingTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void resize() throws IOException, InterruptedException,
			IM4JavaException {
		File folder = new File(Util.getTempDir(), "s4stest");
		folder.mkdir();

		File outFilename = new File(folder, "test-im4java.jpg");

		IMOperation op = new IMOperation();
		op.format("jpeg");
		op.addImage("-"); // read from stdin
		op.resize(200, 100, "!");
		op.addImage(outFilename.getAbsolutePath());

		InputStream in = getClass().getResourceAsStream("/test.jpg");
		assertNotNull(in);
		Pipe pipeIn = new Pipe(in, null);

		// set up command
		ConvertCmd convert = new ConvertCmd(true);
		convert.setInputProvider(pipeIn);
		convert.run(op);
		in.close();

		logger.info(outFilename.getAbsolutePath() + " created.");
	}
}
