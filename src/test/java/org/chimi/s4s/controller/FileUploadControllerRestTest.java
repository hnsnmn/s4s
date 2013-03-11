package org.chimi.s4s.controller;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * 로컬에 웹 서버를 실행한 후에 이 테스트를 실행해야 함
 * 
 * @author Choi Beom Kyun
 */
public class FileUploadControllerRestTest {

	@Test
	public void uploadJson() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				"http://localhost:8080/s4s/upload/svc000/json");

		MultipartEntity reqEntity = new MultipartEntity();
		FileBody bin = new FileBody(new File("src/test/resources/test.jpg"));
		reqEntity.addPart("file", bin);
		httpPost.setEntity(reqEntity);

		HttpResponse response = client.execute(httpPost);
		HttpEntity resEntity = response.getEntity();

		System.out.println("----------------------------------------");
		System.out.println(response.getStatusLine());
		if (resEntity != null) {
			System.out.println("Response content length: "
					+ EntityUtils.toString(resEntity));
		}
		//EntityUtils.consume(resEntity);

		client.getConnectionManager().shutdown();
	}
}
