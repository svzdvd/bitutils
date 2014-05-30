/**
 * Copyright 2013 Davide Savazzi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Assert;

public class Bits2Test extends TestCase {
	
	public void testSize2() throws IOException {
		File file = new File("2bits.bin");
		file.delete();
		
		int[] values2 = new int[] { 0, 1, 2, 3, 2, 1, 0, 3, 1, 2 };
		BitWriter writer = new RandomAccessBitWriter(file, BitListSize.Size2);
		try {
			for (int i = 0; i < values2.length; i++) {
				writer.write(values2[i]);
			}
		} finally {
			writer.close();
		}
		
		// file lenght should be 1 int = 4 bytes
		Assert.assertEquals(4, file.length());
		
		read("RandomAccessBitReader", new RandomAccessBitReader(file, BitListSize.Size2), values2);
		read("BufferBitReader", new BufferBitReader(file, BitListSize.Size2), values2);
		
		file.delete();
	}

	private void read(String readerType, BitReader reader, int[] values2) throws IOException {
		try {
			for (int i = 0; i < values2.length; i++) {
				Assert.assertEquals("Wrong value in position: " + i, values2[i], reader.read());
			}
		} finally {
			reader.close();
		}		
	}
}
