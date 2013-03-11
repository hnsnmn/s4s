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
package org.chimi.s4s.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Choi Beom Kyun
 */
public class MimeUtilTest {

	@Test
	public void getMimeType() {
		assertEquals("application/octet-stream", MimeUtil.getMimeType("a.txt"));
		assertEquals("image/gif", MimeUtil.getMimeType("a.gif"));
		assertEquals("image/jpeg", MimeUtil.getMimeType("a.jpg"));
		assertEquals("image/jpeg", MimeUtil.getMimeType("a.jpeg"));
		assertEquals("image/x-png", MimeUtil.getMimeType("a.png"));
		assertEquals("application/octet-stream", MimeUtil.getMimeType("a.doc"));
		assertEquals("application/octet-stream", MimeUtil.getMimeType("a.zip"));
	}
}
