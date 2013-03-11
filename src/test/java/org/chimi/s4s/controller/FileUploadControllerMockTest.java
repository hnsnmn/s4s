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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.chimi.s4s.controller.UploadResultResponse.UploadResultCode;
import org.chimi.s4s.fileservice.FileService;
import org.chimi.s4s.fileservice.UploadFile;
import org.chimi.s4s.fileservice.UploadResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ModelMap;

/**
 * @author Choi Beom Kyun
 */
@RunWith(MockitoJUnitRunner.class)
public class FileUploadControllerMockTest {

	private static final String SUCCESS_VIEWNAME = "uploadResult";
	private static final String FILENAME = "파일 이름.txt";
	private static final String FILEID01 = "FILEID01";
	private static final String METAID01 = "METAID01";
	private static final String MIME_TYPE = "application/octet-stream";

	@Mock
	private FileService fileService;

	private FileUploadController controller = new FileUploadController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(fileService.save(Mockito.any(UploadFile.class)))
				.thenAnswer(new Answer<UploadResult>() {

					@Override
					public UploadResult answer(InvocationOnMock invocation)
							throws Throwable {
						UploadFile uf = (UploadFile) invocation.getArguments()[0];
						return new UploadResult(uf.getFilename(), FILEID01,
								METAID01, MIME_TYPE, 100);
					}
				});
		controller.setFileService(fileService);
	}

	@Test
	public void uploadSuccessfully() throws IllegalStateException, IOException {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		MockMultipartFile uploadFile = new MockMultipartFile("file",
				"C:\\temp\\" + FILENAME, null, new byte[100]);
		String serviceId = "SVC01";

		ModelMap modelMap = new ModelMap();
		String viewName = controller.upload(uploadFile, serviceId, mockRequest,
				modelMap);
		assertEquals(SUCCESS_VIEWNAME, viewName);
		UploadResultResponse result = (UploadResultResponse) modelMap
				.get("uploadResult");

		assertEquals(UploadResultCode.SUCCESS, result.getCode());
		assertTrue(result.isSuccess());
		assertFalse(result.isImage());
		assertEquals(FILENAME, result.getFileName());
		assertEquals(FILEID01, result.getFileId());
		assertEquals(METAID01, result.getMetadataId());
		assertEquals(MIME_TYPE, result.getMimeType());
		assertEquals(100L, result.getLength());
	}

}
