package it.yobibit.bitutils;

import it.yobibit.bitutils.BitReader;
import it.yobibit.bitutils.BufferBitReader;
import it.yobibit.bitutils.RandomAccessBitReader;
import it.yobibit.bitutils.BitWriter;
import it.yobibit.bitutils.Bits.BitListSize;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Assert;

import junit.framework.TestCase;

public class Bits2Test extends TestCase {
	
	public void testSize2() throws IOException {
		File file = new File("2bits.bin");
		file.delete();
		
		int[] values2 = new int[] { 0, 1, 2, 3, 2, 1, 0, 3, 1, 2 };
		BitWriter writer = new BitWriter(new RandomAccessFile(file, "rw"), BitListSize.Size2);
		try {
			for (int i = 0; i < values2.length; i++) {
				writer.write(values2[i]);
			}
		} finally {
			writer.close();
		}
		
		// file lenght should be 1 int = 4 bytes
		Assert.assertEquals(4, file.length());
		
		read("RandomAccessBitReader", new RandomAccessBitReader(new RandomAccessFile(file, "r"), BitListSize.Size2), values2);
		read("BufferBitReader", new BufferBitReader(new RandomAccessFile(file, "r"), BitListSize.Size2), values2);
		
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
