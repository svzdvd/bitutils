package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel.MapMode;


public class BufferBitReader implements BitReader {

	private final RandomAccessFile file;
	private final IntBuffer mappedFile;
	private final int recordSize;
	private final int recordsInBuffer;
	private int buffer;
	private int bufferPos;
	private static final int BUFFER_SIZE = Integer.SIZE;
	
	
	public BufferBitReader(RandomAccessFile file, BitListSize size) throws IOException {
		this.file = file;
		this.mappedFile = file.getChannel().map(MapMode.READ_ONLY, 0, file.length()).asIntBuffer();
		this.recordSize = size.get();
		this.recordsInBuffer = BUFFER_SIZE / recordSize; 
		this.bufferPos = BUFFER_SIZE;
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