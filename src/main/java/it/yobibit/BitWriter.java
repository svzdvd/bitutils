package it.yobibit;

import it.yobibit.Bits.BitListSize;

import java.io.IOException;
import java.io.RandomAccessFile;


public class BitWriter {

	private final RandomAccessFile out;
	private final BitListSize size;
	private int buffer;
	private int bufferPos;
	private static final int MAX_POS = 32;
	
	
	public BitWriter(RandomAccessFile out, BitListSize size) {
		this.out = out;
		this.size = size;
		this.buffer = 0;
		this.bufferPos = 0;
	}
	
	
	public boolean inRange(int value) {
		return value >= 0 && value <= size.max;
	}
	
	public void write(int value) throws IOException {
		for (int i = 0; i < size.get(); i++) {
			if (Bits.get(value, i) != 0) {
				buffer = Bits.set(buffer, bufferPos);
			}
			bufferPos++;
		}
		
		if (bufferPos == MAX_POS) {
			out.writeInt(buffer);
			buffer = 0;
			bufferPos = 0;
		}
	}	
	
	public void close() throws IOException {
		if (bufferPos != 0) {
			out.writeInt(buffer);
		}
		out.close();
	}
}
