package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.IOException;
import java.io.RandomAccessFile;


public class RandomAccessBitReader extends AbstractBitReader {

	private final RandomAccessFile file;
	
	
	public RandomAccessBitReader(RandomAccessFile in, BitListSize size) throws IOException {
		super(size);
		this.file = in;
	}
	
	
	@Override
	public void seek(long recordOffset) throws IOException {
		long intOffset = recordOffset / recordsInBuffer;
		long byteOffset = 4 * intOffset;
		file.seek(byteOffset);
		buffer = file.readInt();
		bufferPos = recordSize * ((int) recordOffset % recordsInBuffer);
	}
	
	@Override
	public int read() throws IOException {
		if (bufferPos == BUFFER_SIZE) {
			buffer = file.readInt();
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
		file.close();
	}
}