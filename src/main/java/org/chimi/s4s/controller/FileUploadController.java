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

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.chimi.s4s.fileservice.FileService;
import org.chimi.s4s.fileservice.UploadFile;
import org.chimi.s4s.fileservice.UploadResult;
import org.chimi.s4s.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 API 제공 및 요청 처리
 * 
 * @author Choi Beom Kyun
 */
@Controller
public class FileUploadController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private FileService fileService;

	@RequestMapping(value = "/upload/{serviceId}", method = RequestMethod.GET)
	public String uploadForm(@PathVariable("serviceId") String serviceId,
			ModelMap model) {
		model.addAttribute("serviceId", serviceId);
		return "upload";
	}

	@RequestMapping(value = "/upload/{serviceId}/json", method = RequestMethod.POST)
	public String uploadJson(
			@RequestParam(value = "file", required = false) MultipartFile userUploadFile,
			@PathVariable("serviceId") String serviceId,
			HttpServletRequest request, ModelMap model) {
		String viewName = upload(userUploadFile, serviceId, request, model);
		return viewName + ".json";
	}

	@RequestMapping(value = "/upload/{serviceId}", method = RequestMethod.POST)
	public String upload(
			@RequestParam(value = "file", required = false) MultipartFile userUploadFile,
			@PathVariable("serviceId") String serviceId,
			HttpServletRequest request, ModelMap model) {
		String resultViewName = "uploadResult";
		if (userUploadFile == null || userUploadFile.isEmpty()) {
			model
					.put(resultViewName, UploadResultResponse
							.createNoFileResult());
			return resultViewName;
		}
		// 임시 파일 생성
		File tempFile = null;
		try {
			tempFile = getTemporaryFile(userUploadFile);
		} catch (Exception ex) {
			logger.error("fail to get temp file for "
					+ userUploadFile.getOriginalFilename(), ex);
			model.put(resultViewName, UploadResultResponse.createErrorResult());
			return resultViewName;
		}
		// 임시 파일을 실제 스토리지 서비스에 저장
		try {
			String fileName = extractFileName(userUploadFile
					.getOriginalFilename());
			String mimeType = getMimeType(fileName);

			UploadResult uploadResult = saveTemporaryFileToFileService(
					userUploadFile, serviceId, fileName, mimeType, tempFile);

			String[] urls = createUrl(uploadResult, request);

			model.put(resultViewName, UploadResultResponse.createSuccessResult(
					uploadResult, urls[0], urls[1]));
			return resultViewName;
		} catch (Exception ex) {
			logger.error("fail to save file to storage"
					+ userUploadFile.getOriginalFilename(), ex);
			model.put(resultViewName, UploadResultResponse.createErrorResult());
			return resultViewName;
		} finally {
			deleteTempFile(tempFile);
		}
	}

	private String[] createUrl(UploadResult uploadResult,
			HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		int hostEndIdx = url.indexOf('/', 7);
		String host = url.substring(0, hostEndIdx);
		String contextPath = request.getContextPath();
		String[] urls = new String[2];
		urls[0] = host + contextPath + "/file/" + uploadResult.getFileId();
		if (uploadResult.isImage()) {
			urls[1] = host + contextPath + "/image/o/"
					+ uploadResult.getFileId();
		}
		return urls;
	}

	private UploadResult saveTemporaryFileToFileService(
			MultipartFile userUploadFile, String serviceId, String fileName,
			String mimeType, File tempFile) {
		UploadFile uf = new UploadFile(serviceId, fileName, mimeType, tempFile,
				userUploadFile.getSize());
		UploadResult uploadResult = fileService.save(uf);
		return uploadResult;
	}

	private void deleteTempFile(File file) {
		if (file == null) {
			return;
		}
		boolean deleted = file.delete();
		if (!deleted) {
			if (logger.isWarnEnabled()) {
				logger.warn("Fail to delete temporary upload file {}.", file
						.getAbsolutePath());
			}
		}
	}

	private Random random = new Random();

	private File getTemporaryFile(MultipartFile userUploadFile)
			throws IllegalStateException, IOException {
		StringBuilder tempNameBuilder = new StringBuilder();
		tempNameBuilder.append(System.currentTimeMillis()).append(".").append(
				random.nextInt(1000));
		File tempFile = new File(Util.getS4STempDir(), tempNameBuilder
				.toString());
		userUploadFile.transferTo(tempFile);
		return tempFile;
	}

	private String getMimeType(String fileName) {
		return MimeUtil.getMimeType(fileName);
	}

	private String extractFileName(String originalFilename) {
		int lastPathSeparator = originalFilename.lastIndexOf('\\');
		if (lastPathSeparator == -1) {
			lastPathSeparator = originalFilename.lastIndexOf('/');
		}
		if (lastPathSeparator == -1) {
			return originalFilename;
		}
		return originalFilename.substring(lastPathSeparator + 1);
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

}
