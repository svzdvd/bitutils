package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Buffer1BitReader extends BufferBitReader {

	public Buffer1BitReader(RandomAccessFile file) throws IOException {
		super(file, BitListSize.Size1);
	}

	
	@Override
	public int read() throws IOException {
		if (bufferPos == BUFFER_SIZE) {
			buffer = mappedFile.get();
			bufferPos = 0;
		}
		
		if (Bits.get(buffer, bufferPos++) == 0) {
			return 0;
		} else {
			return 1;
		}
	}
}