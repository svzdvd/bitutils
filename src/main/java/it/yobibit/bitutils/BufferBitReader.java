package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;


public class BufferBitReader extends AbstractBitReader {

	private RandomAccessFile raf;
	private MappedByteBuffer fileBuffer;
	protected final IntBuffer intBuffer;
	
	
	public BufferBitReader(File file, BitListSize size) throws IOException {
		super(size);
		this.raf = new RandomAccessFile(file, "r");
		this.fileBuffer = raf.getChannel().map(MapMode.READ_ONLY, 0, file.length());
		this.intBuffer = fileBuffer.asIntBuffer();
	}
	
	public BufferBitReader(IntBuffer mappedFile, BitListSize size) {
		super(size);
		this.intBuffer = mappedFile;
	}
	
	
	@Override
	public void reset() throws IOException {
		intBuffer.position(0);
	}
	
	@Override
	public void seek(long recordOffset) throws IOException {
		long intOffset = recordOffset / recordsInBuffer;
		intBuffer.position((int) intOffset);
		buffer = intBuffer.get();
		bufferPos = recordSize * ((int) recordOffset % recordsInBuffer);
	}
	
	@Override
	public int read() throws IOException {
		if (bufferPos == BUFFER_SIZE) {
			buffer = intBuffer.get();
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
		if (fileBuffer != null) {
			DirectBufferCleaner.cleanDirectBuffer(fileBuffer);
		}
		
		if (raf != null) {
			raf.close();
		}
	}
}