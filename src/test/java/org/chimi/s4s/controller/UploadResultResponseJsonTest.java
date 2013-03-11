package org.chimi.s4s.controller;

import java.io.IOException;
import java.io.StringWriter;

import org.chimi.s4s.fileservice.UploadResult;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class UploadResultResponseJsonTest {

	@Test
	public void successResult() throws IOException {
		UploadResult result = new UploadResult("파일 이름.gif", "12345", "ox1234",
				"text/jpeg", 10000L);
		String fileUrl = "http://s4s/s4s/file/12345";
		String imageUrl = "http://s4s/s4s/image/o/12345";
		UploadResultResponse response = UploadResultResponse
				.createSuccessResult(result, fileUrl, imageUrl);
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		JsonGenerator generator = objectMapper.getJsonFactory()
				.createJsonGenerator(writer);
		objectMapper.writeValue(generator, response);
		System.out.println(writer.toString());
	}

	@Test
	public void nofileResult() throws IOException {
		UploadResultResponse response = UploadResultResponse
				.createNoFileResult();
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		JsonGenerator generator = objectMapper.getJsonFactory()
				.createJsonGenerator(writer);
		objectMapper.writeValue(generator, response);
		System.out.println(writer.toString());
	}
}
