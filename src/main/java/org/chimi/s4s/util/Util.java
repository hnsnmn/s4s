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
package org.chimi.s4s.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 자주 사용되는 보조 기능 제공
 * 
 * @author Choi Beom Kyun
 */
public abstract class Util {

	/**
	 * 시스템 임시 디렉터리를 구한다.
	 * <p>
	 * System.getProperty("java.io.tmpdir")를 구한다.
	 */
	public static String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * S4S에서 사용할 임시 디렉터리를 구한다.
	 * 
	 * @return
	 */
	public static String getS4STempDir() {
		return getTempDir() + getFileSeparator() + "s4s_temp";
	}

	/**
	 * System.getProperty("file.separator")를 구한다.
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	public static final int BUFFER_SIZE = 4096;

	/**
	 * in 스트림에서 데이터를 읽어와 out 스트림에 출력한다. 작업이 완료된 뒤에 in 스트림과 out 스트림을 close하지 않기
	 * 때문에 이 메서드를 호출한 코드에서 알맞게 스트림을 종료해 주어야 한다.
	 * 
	 * @param in
	 * @param out
	 * @return
	 * @throws IOException
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
		int totalCount = 0;
		byte[] buff = new byte[BUFFER_SIZE];
		int length = -1;
		while ((length = in.read(buff)) != -1) {
			out.write(buff, 0, length);
			totalCount += length;
		}
		out.flush();
		return totalCount;
	}

	/**
	 * in 스트림을 종료한다. 예외가 발생하더라도 무시한다.
	 * 
	 * @param in
	 */
	public static void close(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	/**
	 * out 스트림을 종료한다. 예외가 발생하더라도 무시한다.
	 * 
	 * @param out
	 */
	public static void close(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
}
