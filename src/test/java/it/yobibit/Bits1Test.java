package it.yobibit;

import it.yobibit.BitReader;
import it.yobibit.BitWriter;
import it.yobibit.Bits.BitListSize;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Assert;

import junit.framework.TestCase;

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
		
		BitReader reader = new BitReader(new RandomAccessFile(file, "r"), BitListSize.Size1);
		try {
			for (int i = 0; i < values1.length; i++) {
				boolean value = reader.read() != 0;
				Assert.assertEquals("Wrong value in position: " + i, values1[i], value);
			}
		} finally {
			reader.close();
		}
		
		file.delete();
	}
}
