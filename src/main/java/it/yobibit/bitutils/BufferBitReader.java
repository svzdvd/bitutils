package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel.MapMode;


public class BufferBitReader extends AbstractBitReader {

	private RandomAccessFile raf;
	protected final IntBuffer mappedFile;
	
	
	public BufferBitReader(File file, BitListSize size) throws IOException {
		super(size);
		this.raf = new RandomAccessFile(file, "r");
		this.mappedFile = raf.getChannel().map(MapMode.READ_ONLY, 0, file.length()).asIntBuffer();
	}
	
	public BufferBitReader(IntBuffer mappedFile, BitListSize size) {
		super(size);
		this.mappedFile = mappedFile;
	}
	
	
	@Override
	public void reset() throws IOException {
		mappedFile.position(0);
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
		if (raf != null) {
			raf.close();
		}
	}
}