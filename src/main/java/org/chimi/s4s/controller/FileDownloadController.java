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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.chimi.s4s.fileservice.FileService;
import org.chimi.s4s.fileservice.FileSource;
import org.chimi.s4s.fileservice.Size;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 파일 다운로드 기능을 제공한다.
 * 
 * @author Choi Beom Kyun
 */
@Controller
public class FileDownloadController {

	private FileService fileService;
	private CacheStrategy cacheStrategy;

	/**
	 * 파일 다운로드 요청 처리
	 * 
	 * @param fileId
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/file/{fileId}", method = RequestMethod.GET)
	public void file(@PathVariable("fileId") String fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		searchAndSendFile(false, fileId, request, response);
	}

	/**
	 * 이미지 다운로드 요청 처리
	 * 
	 * @param fileId
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/image/o/{fileId}", method = RequestMethod.GET)
	public void image(@PathVariable("fileId") String fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		searchAndSendFile(true, fileId, request, response);
	}

	/**
	 * 썸네일 다운로드 요청 처리
	 * 
	 * @param fileId
	 * @param sizeString
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/image/t/{fileId}/{size}", method = RequestMethod.GET)
	public void thumbnailImage(@PathVariable("fileId") String fileId,
			@PathVariable("size") String sizeString,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Size size = parseSizeString(sizeString);
		FileSource file = fileService.getThumbnailImage(fileId, size);
		checkAndSend(true, request, response, file);
	}

	private Size parseSizeString(String sizeString) {
		int wIdx = sizeString.indexOf('w');
		int hIdx = sizeString.indexOf('h', wIdx + 1);

		String width = sizeString.substring(wIdx + 1, hIdx);
		String height = sizeString.substring(hIdx + 1);
		return new Size(Integer.parseInt(width), Integer.parseInt(height));
	}

	private void searchAndSendFile(boolean requestImageInline, String fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		FileSource file = fileService.getFile(fileId);
		checkAndSend(requestImageInline, request, response, file);
	}

	private void checkAndSend(boolean requestImageInline,
			HttpServletRequest request, HttpServletResponse response,
			FileSource file) throws IOException {
		if (file == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (requestImageInline && !file.isImage()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (checkModifiedRequest(request)) {
			response.setStatus(304);
			response.setContentType(file.getMimeType());
			cacheStrategy.setCacheOption(file, response);
			return;
		}
		sendFile(requestImageInline, file, request, response);
	}

	private boolean checkModifiedRequest(HttpServletRequest request) {
		String modifedSinceHeader = request.getHeader("If-Modified-Since");
		if (modifedSinceHeader != null) {
			return true;
		}
		return false;
	}

	private void sendFile(boolean imageInline, FileSource source,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType(source.getMimeType());
		cacheStrategy.setCacheOption(source, response);
		response.setHeader("Content-Disposition", (imageInline ? "inline"
				: "attachment")
				+ "; filename=\""
				+ getHeaderFileName(source.getFileName(), request) + "\";");
		response.setContentLength((int) source.getLength());

		ServletOutputStream out = response.getOutputStream();
		source.write(out);

		out.flush();
		out.close();
	}

	private String getHeaderFileName(String fileName, HttpServletRequest request)
			throws UnsupportedEncodingException {
		String userAgent = request.getHeader("User-Agent");
		boolean ie = userAgent != null && userAgent.indexOf("MSIE") > -1;
		if (ie) {
			return URLEncoder.encode(fileName, "UTF-8");
		}
		return MimeUtility.encodeWord(fileName, "UTF-8", "B");
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public void setCacheStrategy(CacheStrategy cacheStrategy) {
		this.cacheStrategy = cacheStrategy;
	}

}
