package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;


public class Buffer1BitWriter extends BufferBitWriter {

	public Buffer1BitWriter(File file, int maxRecords) throws IOException {
		super(file, BitListSize.Size1, maxRecords);
	}
	
	@Override
	public BitReader getReader() {
		if (bufferPos != 0) {
			int pos = intBuffer.position();
			intBuffer.put(buffer);
			intBuffer.position(pos);
		}
		
		IntBuffer readOnlyBuffer = intBuffer.asReadOnlyBuffer();
		readOnlyBuffer.position(0);
		return new Buffer1BitReader(readOnlyBuffer);
	}
	
	@Override
	public void write(int value) throws IOException {
		if (recordCount + 1 > maxRecords) {
			throw new IOException("Already inserted all available records: " + maxRecords);
		}
		
		if (value != 0) {
			buffer = Bits.set(buffer, bufferPos);
		}
		bufferPos++;
		
		if (bufferPos == MAX_POS) {
			intBuffer.put(buffer);
			buffer = 0;
			bufferPos = 0;
		}
		
		recordCount++;
	}
}