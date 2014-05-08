package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class RandomAccessBitWriter implements BitWriter {

	private final RandomAccessFile raf;
	private final BitListSize size;
	private int buffer;
	private int bufferPos;
	private static final int MAX_POS = 32;
	
	
	public RandomAccessBitWriter(File file, BitListSize size) throws FileNotFoundException {
		this.raf = new RandomAccessFile(file, "rw");
		this.size = size;
		this.buffer = 0;
		this.bufferPos = 0;
	}
	
	
	@Override
	public void write(int value) throws IOException {
		for (int i = 0; i < size.get(); i++) {
			if (Bits.get(value, i) != 0) {
				buffer = Bits.set(buffer, bufferPos);
			}
			bufferPos++;
		}
		
		if (bufferPos == MAX_POS) {
			raf.writeInt(buffer);
			buffer = 0;
			bufferPos = 0;
		}
	}	
	
	@Override
	public void flush() throws IOException {
		raf.getChannel().force(false);
	}
	
	@Override
	public void close() throws IOException {
		if (bufferPos != 0) {
			raf.writeInt(buffer);
		}
		raf.close();
	}
}
