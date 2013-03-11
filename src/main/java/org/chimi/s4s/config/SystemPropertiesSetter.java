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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.chimi.s4s.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스프링 설정에 필요한 시스템 프로퍼티를 설정한다.
 * 
 * @author Choi Beom Kyun
 */
public class SystemPropertiesSetter {

	private static final String CONFIG_PATH_SYSTEM_PROPERTY = "s4s.config";
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * s4s.config 시스템 프로퍼티에 설정된 파일의 프로퍼티들을 시스템 프로퍼티로 설정한다.
	 */
	private void loadCustomConfigAndSet() {
		String configPath = System.getProperty(CONFIG_PATH_SYSTEM_PROPERTY);

		logger.info("s4s.config system property value is {}.", configPath);

		if (configPath == null) {
			return;
		}
		InputStream is = null;
		try {
			is = new FileInputStream(configPath);
			loadPropertiesAndSetSystemProperties(is);
			logger.info("Populate system properties from {}", configPath);
		} catch (IOException e) {
			logger.error("Error occured while populate system properties from "
					+ configPath, e);
		} finally {
			Util.close(is);
		}
	}

	/**
	 * is로부터 프로퍼티 정보를 읽어와 시스템 프로퍼티로 설정한다.
	 * 
	 * @param is
	 * @throws IOException
	 */
	private void loadPropertiesAndSetSystemProperties(InputStream is)
			throws IOException {
		Properties properties = new Properties();
		properties.load(is);
		Iterator<String> keyNames = properties.stringPropertyNames().iterator();
		while (keyNames.hasNext()) {
			String key = keyNames.next();
			String value = properties.getProperty(key);
			System.setProperty(key, value);
		}
	}

	/**
	 * default-config.properties를 시스템 프로퍼티로 설정한다.
	 */
	private void loadDefaultConfigAndSet() {
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream("/default-config.properties");
			loadPropertiesAndSetSystemProperties(is);
			logger
					.info("Populate system properties from default-config.properties");
		} catch (IOException e) {
			logger
					.error(
							"Error occured while populatint system properties from default-config.properties",
							e);
		} finally {
			Util.close(is);
		}
	}

	/**
	 * <ol>
	 * <li>default-config.properties 파일로부터 데이터를 읽어와 시스템 프로퍼티에 등록한다.</li>
	 * <li>s4s.config 시스템 프로퍼티가 존재하면, 해당 프로퍼티에 지정된 파일로부터 프로퍼티 정보를 읽어와 그 값을 시스템
	 * 프로퍼티로 설정한다.</li>
	 * </ol>
	 */
	public void loadAndSet() {
		loadDefaultConfigAndSet();
		loadCustomConfigAndSet();
	}

}
