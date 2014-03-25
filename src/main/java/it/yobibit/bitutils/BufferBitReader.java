package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel.MapMode;


public class BufferBitReader extends AbstractBitReader {

	private final RandomAccessFile file;
	protected final IntBuffer mappedFile;
	
	
	public BufferBitReader(RandomAccessFile file, BitListSize size) throws IOException {
		super(size);
		this.file = file;
		this.mappedFile = file.getChannel().map(MapMode.READ_ONLY, 0, file.length()).asIntBuffer();
	}
	
	
	@Override
	public void seek(long recordOffset) throws IOException {
		long intOffset = recordOffset / recordsInBuffer;
		mappedFile.position((int) intOffset);
		buffer = mappedFile.get();
		bufferPos = recordSize * ((int) recordOffset % recordsInBuffer);
	}
	
	@Override
	public int read() throws IOException {
		if (bufferPos == BUFFER_SIZE) {
			buffer = mappedFile.get();
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