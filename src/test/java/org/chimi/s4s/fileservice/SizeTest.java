package org.chimi.s4s.fileservice;

import static org.junit.Assert.*;

import org.junit.Test;

public class SizeTest {

	@Test
	public void noApplyRatio() {
		Size size = new Size(100, 200);
		Size newSize = size.applyRatio(200, 300);
		assertEquals(size, newSize);
	}

	@Test
	public void applyRatioToWidth() {
		Size size = new Size(0, 200);
		Size newSize = size.applyRatio(200, 400);
		assertEquals(100, newSize.getWidth());
		assertEquals(200, newSize.getHeight());
	}

	@Test
	public void applyRatioToHeight() {
		Size size = new Size(100, 0);
		Size newSize = size.applyRatio(200, 400);
		assertEquals(100, newSize.getWidth());
		assertEquals(200, newSize.getHeight());
	}
}
