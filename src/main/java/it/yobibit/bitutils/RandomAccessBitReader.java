package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class RandomAccessBitReader extends AbstractBitReader {

	private final RandomAccessFile raf;
	
	
	public RandomAccessBitReader(File file, BitListSize size) throws IOException {
		super(size);
		this.raf = new RandomAccessFile(file, "r");
	}
	
	@Override
	public void reset() throws IOException {
		raf.seek(0);
	}
	
	@Override
	public void seek(long recordOffset) throws IOException {
		long intOffset = recordOffset / recordsInBuffer;
		long byteOffset = 4 * intOffset;
		raf.seek(byteOffset);
		buffer = raf.readInt();
		bufferPos = recordSize * ((int) recordOffset % recordsInBuffer);
	}
	
	@Override
	public int read() throws IOException {
		if (bufferPos == BUFFER_SIZE) {
			buffer = raf.readInt();
			bufferPos = 0;
		}
		
		int value = 0;
		for (int i = 0; i < recordSize; i++) {
			if (Bits.get(buffer, bufferPos++) != 0) {
				value = Bits.set(value, i);
			}
		}
		
		return value;
	}	
	
	@Override
	public void close() throws IOException {
		raf.close();
	}
}