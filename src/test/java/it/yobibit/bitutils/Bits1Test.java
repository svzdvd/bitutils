package it.yobibit.bitutils;

import it.yobibit.bitutils.BitReader;
import it.yobibit.bitutils.BitWriter;
import it.yobibit.bitutils.Bits.BitListSize;
import it.yobibit.bitutils.BufferBitReader;
import it.yobibit.bitutils.RandomAccessBitReader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import junit.framework.TestCase;

import org.junit.Assert;

public class Bits1Test extends TestCase {
	
	public void testSize1() throws IOException {
		File file = new File("1bits.bin");
		file.delete();
		
		boolean[] values1 = new boolean[] { true, true, false, true, false, false, false, true, false, true };
		BitWriter writer = new BitWriter(new RandomAccessFile(file, "rw"), BitListSize.Size1);
		try {
			for (int i = 0; i < values1.length; i++) {
				writer.write(values1[i] ? 1 : 0);
			}
		} finally {
			writer.close();
		}
		
		// file lenght should be 1 int = 4 bytes
		Assert.assertEquals(4, file.length());
		
		read("RandomAccessBitReader", new RandomAccessBitReader(new RandomAccessFile(file, "r"), BitListSize.Size1), values1);
		read("BufferBitReader", new BufferBitReader(new RandomAccessFile(file, "r"), BitListSize.Size1), values1);
		
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
