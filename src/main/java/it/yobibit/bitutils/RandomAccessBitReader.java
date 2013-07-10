package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.IOException;
import java.io.RandomAccessFile;


public class RandomAccessBitReader implements BitReader {

	private final RandomAccessFile file;
	private final int recordSize;
	private final int recordsInBuffer;
	private int buffer;
	private int bufferPos;
	private static final int BUFFER_SIZE = Integer.SIZE;
	
	
	public RandomAccessBitReader(RandomAccessFile in, BitListSize size) throws IOException {
		this.file = in;
		this.recordSize = size.get();
		this.recordsInBuffer = BUFFER_SIZE / recordSize;
		this.bufferPos = BUFFER_SIZE;
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