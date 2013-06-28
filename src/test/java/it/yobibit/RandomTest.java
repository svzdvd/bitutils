package it.yobibit;

import it.yobibit.Bits.BitListSize;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Assert;

public class RandomTest extends TestCase {

	public void testSize1() throws IOException {
		test(BitListSize.Size1);
	}
	
	public void testSize2() throws IOException {
		test(BitListSize.Size2);
	}	
	
	public void testSize4() throws IOException {
		test(BitListSize.Size4);
	}
	
	private void test(BitListSize size) throws IOException {
		File file = new File("bits.bin");
		file.delete();
		
		Random random = new Random();

		int recordCount = random.nextInt(1000000);
		System.out.println("using " + recordCount + " records with size " + size.get());
		
		int[] records = new int[recordCount];
		for (int i = 0; i < records.length; i++) {
			records[i] = random.nextInt(size.max() + 1);
		}
		
		BitWriter writer = new BitWriter(new RandomAccessFile(file, "rw"), size);
		try {
			for (int i = 0; i < records.length; i++) {
				writer.write(records[i]);
			}
		} finally {
			writer.close();
		}
		
		BitReader reader = new BitReader(new RandomAccessFile(file, "r"), size);
		try {
			for (int i = 0; i < records.length; i++) {
				Assert.assertEquals("Wrong value in position: " + i, records[i], reader.read());
			}
			
			for (int i = 0; i < records.length; i++) {
				reader.seek(i);
				Assert.assertEquals("Wrong value in position: " + i, records[i], reader.read());
			}
			
			for (int i = records.length; i > 0; i--) {
				reader.seek(i - 1);
				Assert.assertEquals("Wrong value in position: " + (i - 1), records[i - 1], reader.read());
			}
		} finally {
			reader.close();
		}
		
		file.delete();
	}
}
