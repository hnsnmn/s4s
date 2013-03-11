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

import org.apache.log4j.PropertyConfigurator;

/**
 * Log4j Configuration 실행.
 * 
 * @author Choi Beom Kyun
 */
public class Log4jConfigurator {

	public static final String CONFIG_PATH_SYSTEM_PROPERTY = "s4s.log4j.config";

	public static void configure() {
		String configFilePath = System.getProperty(CONFIG_PATH_SYSTEM_PROPERTY);

		System.out.println("s4s.log4j.config system property value is ["
				+ configFilePath + "]");
		if (configFilePath == null || configFilePath.isEmpty()) {
			return;
		}
		PropertyConfigurator.configure(configFilePath);
	}

}
