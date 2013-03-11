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
package org.chimi.s4s.spring;

import static org.junit.Assert.assertTrue;

import org.chimi.s4s.config.SystemPropertiesSetter;
import org.chimi.s4s.imageprocessor.IM4JavaImageProcessor;
import org.chimi.s4s.imageprocessor.JavaImageProcessor;
import org.chimi.s4s.metainfo.MetaInfoDao;
import org.chimi.s4s.metainfo.rdb.RdbMetaInfoDao;
import org.chimi.s4s.storage.localfs.LocalFSFileStorage;
import org.chimi.s4s.storage.mongofs.MongoFSFileStorage;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Choi Beom Kyun
 */
public class SpringConfigTest {

	@Test
	public void defaultConfig() {
		SystemPropertiesSetter setter = new SystemPropertiesSetter();
		setter.loadAndSet();

		createContextAndTestBeanType(RdbMetaInfoDao.class,
				LocalFSFileStorage.class, JavaImageProcessor.class);
	}

	private void createContextAndTestBeanType(Class<?> metaInfoDaoClass,
			Class<?> fileStorageClass, Class<?> imageProcessorClass) {
		String[] configLocations = { "classpath:spring-properties.xml",
				"classpath:spring-controller.xml",
				"classpath:spring-fileservice.xml",
				"classpath:spring-metainfo.xml",
				"classpath:spring-storage.xml",
				"classpath:spring-imageprocessor.xml" };

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				configLocations);

		MetaInfoDao metaInfoDao = context.getBean(MetaInfoDao.class);
		assertTrue(metaInfoDao.toString()
				.startsWith(metaInfoDaoClass.getName()));

		context.close();
	}

	@Test
	public void mongofsStorageAndRestDefaultConfig() {
		SystemPropertiesSetter setter = new SystemPropertiesSetter();
		setter.loadAndSet();
		System.setProperty("storage.fs", "mongofs");

		createContextAndTestBeanType(RdbMetaInfoDao.class,
				MongoFSFileStorage.class, JavaImageProcessor.class);
	}

	@Test
	public void im4javaAndRestDefaultConfig() {
		SystemPropertiesSetter setter = new SystemPropertiesSetter();
		setter.loadAndSet();
		System.setProperty("image.processor", "im4java");

		createContextAndTestBeanType(RdbMetaInfoDao.class,
				LocalFSFileStorage.class, IM4JavaImageProcessor.class);
	}
}
