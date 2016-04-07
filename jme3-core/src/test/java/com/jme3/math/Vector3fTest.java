/*
 * Copyright (c) 2009-2016 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.math;

import org.junit.Test;
import static org.junit.Assert.*;

public class Vector3fTest {

	@Test
	public void testEmptyArray() {
		double[] data = new double[0];
		Vector3f[] vectorArr = Vector3f.toVector3(data);
		assertEquals(0, vectorArr.length);
	}
	
	@Test(expected=AssertionError.class)
	public void testArrayWithWrongLength() {
		double[] data = {1.0, 5.3};
		Vector3f.toVector3(data);
	}
	
	@Test
	public void testArrayWithOneVector() {
		double[] data = {1.0, 5.3, 4.2};
		Vector3f[] vectorArr = Vector3f.toVector3(data);
		assertEquals(1, vectorArr.length);
		Vector3f expected = new Vector3f(1.0f, 5.3f, 4.2f);
		assertTrue(expected.equals(vectorArr[0]));
	}
	
	@Test
	public void testArrayWithMultipleVectors() {
		double[] data = {
			1.0, 1.1, 1.2,
			2.0, 2.1, 2.2,
			3.0, 3.1, 3.2,
		};
		Vector3f[] vectorArr = Vector3f.toVector3(data);
		assertEquals(3, vectorArr.length);
		Vector3f expected1 = new Vector3f(1.0f, 1.1f, 1.2f);
		Vector3f expected2 = new Vector3f(2.0f, 2.1f, 2.2f);
		Vector3f expected3 = new Vector3f(3.0f, 3.1f, 3.2f);

		assertTrue(expected1.equals(vectorArr[0]));
		assertTrue(expected2.equals(vectorArr[1]));
		assertTrue(expected3.equals(vectorArr[2]));
	}
}
