package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

public class Buffer1BitReader extends BufferBitReader {

	public Buffer1BitReader(File file) throws IOException {
		super(file, BitListSize.Size1);
	}

	public Buffer1BitReader(IntBuffer mappedFile) {
		super(mappedFile, BitListSize.Size1);
	}

	
	@Override
	public int read() throws IOException {
		if (bufferPos == BUFFER_SIZE) {
			buffer = intBuffer.get();
			bufferPos = 0;
		}
		
		if (Bits.get(buffer, bufferPos++) == 0) {
			return 0;
		} else {
			return 1;
		}
	}
}