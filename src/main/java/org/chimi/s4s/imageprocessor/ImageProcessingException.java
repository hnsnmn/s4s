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
package org.chimi.s4s.imageprocessor;

/**
 * 이미지 처리 과정에서 문제가 있는 경우 발생
 * 
 * @author Choi Beom Kyun
 */
@SuppressWarnings("serial")
public class ImageProcessingException extends RuntimeException {

	public ImageProcessingException() {
		super();
	}

	public ImageProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImageProcessingException(String message) {
		super(message);
	}

	public ImageProcessingException(Throwable cause) {
		super(cause);
	}

}
