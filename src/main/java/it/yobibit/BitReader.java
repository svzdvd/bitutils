package it.yobibit;

import it.yobibit.Bits.BitListSize;

import java.io.IOException;
import java.io.RandomAccessFile;


public class BitReader {

	private final RandomAccessFile in;
	private final int recordSize;
	private int buffer;
	private int bufferPos;
	private static final int MAX_POS = Integer.SIZE;
	
	
	public BitReader(RandomAccessFile in, BitListSize size) throws IOException {
		this.in = in;
		this.recordSize = size.get();
		this.bufferPos = MAX_POS;
	}
	
	
	public void seek(long recordOffset) throws IOException {
		long intOffset = recordOffset / (MAX_POS / recordSize);
		long byteOffset = 4 * intOffset;
		in.seek(byteOffset);
		buffer = in.readInt();
		bufferPos = recordSize * ((int) recordOffset % (MAX_POS / recordSize));
	}
	
	public int read() throws IOException {
		if (bufferPos == MAX_POS) {
			buffer = in.readInt();
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
	
	public void close() throws IOException {
		in.close();
	}
}