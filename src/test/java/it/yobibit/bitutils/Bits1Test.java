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
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;

import junit.framework.TestCase;

import org.junit.Assert;


public class Bits1Test extends TestCase {
	
	public void testSize1() throws IOException {
		File file = new File("1bits.bin");
		file.delete();
		
		boolean[] values1 = new boolean[] { true, true, false, true, false, false, false, true, false, true };
		BitWriter writer = new RandomAccessBitWriter(file, BitListSize.Size1);
		try {
			for (int i = 0; i < values1.length; i++) {
				writer.write(values1[i] ? 1 : 0);
			}
		} finally {
			writer.close();
		}
		
		// file lenght should be 1 int = 4 bytes
		Assert.assertEquals(4, file.length());
		
		read("RandomAccessBitReader", new RandomAccessBitReader(file, BitListSize.Size1), values1);
		read("BufferBitReader", new BufferBitReader(file, BitListSize.Size1), values1);
		
		file.delete();
	}

	public void test4722954Buffer1BitWriter() throws IOException {
		File file = new File("1bits.bin");
		file.delete();
		
		RandomAccessFile raf = new RandomAccessFile(new File("bit1test.bin"), "r");
		
		Buffer1BitWriter writer = new Buffer1BitWriter(file, 4722954);
		for (int i = 0; i < 4722954; i++) {
			writer.write(raf.readBoolean() ? 1 : 0);
		}
		
		raf.seek(0);
		BitReader reader = writer.getReader();
		for (int i = 0; i < 4722954; i++) {
			try {
				Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read() == 1);
			} catch (BufferUnderflowException e) {
				e.printStackTrace();
				Assert.fail("Failure at Record " + i + ": " + e.getMessage());
			}
		}
		reader.close();		
		
		writer.close();
		
		raf.seek(0);
		reader = new Buffer1BitReader(file);
		for (int i = 0; i < 4722954; i++) {
			try {
				Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read() == 1);
			} catch (BufferUnderflowException e) {
				e.printStackTrace();
				Assert.fail("Failure at Record " + i + ": " + e.getMessage());
			}
		}
		reader.close();
				
		raf.close();
		file.delete();
	}
	
	public void test4722954BufferBitWriter() throws IOException {
		File file = new File("1bits.bin");
		file.delete();
		
		RandomAccessFile raf = new RandomAccessFile(new File("bit1test.bin"), "r");
		
		BufferBitWriter writer = new BufferBitWriter(file, BitListSize.Size1, 4722954);
		for (int i = 0; i < 4722954; i++) {
			writer.write(raf.readBoolean() ? 1 : 0);
		}

		raf.seek(0);
		BitReader reader = writer.getReader();
		for (int i = 0; i < 4722954; i++) {
			try {
				Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read() == 1);
			} catch (BufferUnderflowException e) {
				e.printStackTrace();
				Assert.fail("Failure at Record " + i + ": " + e.getMessage());
			}
		}
		reader.close();
		
		writer.close();

		raf.seek(0);
		reader = new BufferBitReader(file, BitListSize.Size1);
		for (int i = 0; i < 4722954; i++) {
			try {
				Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read() == 1);
			} catch (BufferUnderflowException e) {
				e.printStackTrace();
				Assert.fail("Failure at Record " + i + ": " + e.getMessage());
			}
		}
		reader.close();
		
		raf.seek(0);
		reader = new Buffer1BitReader(file);
		for (int i = 0; i < 4722954; i++) {
			try {
				Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read() == 1);
			} catch (BufferUnderflowException e) {
				e.printStackTrace();
				Assert.fail("Failure at Record " + i + ": " + e.getMessage());
			}
		}
		reader.close();
				
		raf.close();
		file.delete();
	}	
	
	public void test4722954RandomAccessWriter() throws IOException {
		File file = new File("1bits.bin");
		file.delete();
		
		RandomAccessFile raf = new RandomAccessFile(new File("bit1test.bin"), "r");
		
		BitWriter writer = new RandomAccessBitWriter(file, BitListSize.Size1);
		for (int i = 0; i < 4722954; i++) {
			writer.write(raf.readBoolean() ? 1 : 0);
		}
		writer.close();
		
		raf.seek(0);
		BitReader reader = new BufferBitReader(file, BitListSize.Size1);
		for (int i = 0; i < 4722954; i++) {
			Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read() == 1);
		}
		reader.close();
		
		raf.seek(0);
		reader = new RandomAccessBitReader(file, BitListSize.Size1);
		for (int i = 0; i < 4722954; i++) {
			Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read() == 1);
		}
		reader.close();
		
		raf.seek(0);
		reader = new Buffer1BitReader(file);
		for (int i = 0; i < 4722954; i++) {
			Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read() == 1);
		}
		reader.close();
		
		raf.seek(0);
		reader = new BufferBitReader(file, BitListSize.Size1);
		for (int i = 0; i < 4722954; i++) {
			Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read(i) == 1);
		}
		reader.close();
		
		raf.seek(0);
		reader = new RandomAccessBitReader(file, BitListSize.Size1);
		for (int i = 0; i < 4722954; i++) {
			Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read(i) == 1);
		}
		reader.close();
		
		raf.seek(0);
		reader = new Buffer1BitReader(file);
		for (int i = 0; i < 4722954; i++) {
			Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read(i) == 1);
		}
		reader.close();

		raf.seek(0);
		reader = new RandomAccessBitReader(file, BitListSize.Size1);
		for (int i = 0; i < 4722954; i = i + 2) {
			Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read(i) == 1);
			raf.readBoolean();
		}
		reader.close();
		
		raf.seek(0);
		reader = new BufferBitReader(file, BitListSize.Size1);
		for (int i = 0; i < 4722954; i = i + 2) {
			Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read(i) == 1);
			raf.readBoolean();
		}
		reader.close();
				
		raf.seek(0);
		reader = new Buffer1BitReader(file);
		for (int i = 0; i < 4722954; i = i + 2) {
			Assert.assertEquals("Failure at Record: " + i, raf.readBoolean(), reader.read(i) == 1);
			raf.readBoolean();
		}
		reader.close();
				
		raf.close();
		file.delete();
	}
	
	private void read(String readerType, BitReader reader, boolean[] values1) throws IOException {
		try {
			for (int i = 0; i < values1.length; i++) {
				boolean value = reader.read() != 0;
				Assert.assertEquals("Wrong value in position: " + i, values1[i], value);
			}
		} finally {
			reader.close();
		}		
	}
}
