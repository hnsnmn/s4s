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
package org.chimi.s4s.config;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Choi Beom Kyun
 */
public class Log4jConfiguratorTest {

	@Test
	public void configure() {
		ByteArrayOutputStream baout = new ByteArrayOutputStream(200);
		PrintStream out = new PrintStream(baout);
		System.setOut(out);

		System
				.setProperty(Log4jConfigurator.CONFIG_PATH_SYSTEM_PROPERTY,
						"src/test/resources/org/chimi/s4s/config/test-log4j.properties");
		Log4jConfigurator.configure();

		baout.reset();

		// SLF4J 테스트
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.info("테스트");

		assertEquals("INFO - 테스트", new String(baout.toByteArray()).trim());

		baout.reset();

		// JCL 테스트
		Log log = LogFactory.getLog(getClass());
		log.warn("테스트");

		assertEquals("WARN - 테스트", new String(baout.toByteArray()).trim());
	}
}
