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
package org.chimi.s4s.fileservice;

/**
 * @author Choi Beom Kyun
 */
public class Size {

	private int width;
	private int height;

	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Size applyRatio(int originWidth, int originHeight) {
		if (width > 0 && height > 0) {
			return this;
		}
		double originRatio = (double) originWidth / (double) originHeight;
		if (width <= 0) {
			return new Size(getWidthByRatio(originRatio), height);
		}
		if (height <= 0) {
			return new Size(width, getHeightByRatio(originRatio));
		}
		return null;
	}

	private int getWidthByRatio(double originRatio) {
		return Double.valueOf(originRatio * height).intValue();
	}

	private int getHeightByRatio(double originRatio) {
		return Double.valueOf(width / originRatio).intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Size other = (Size) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

}
