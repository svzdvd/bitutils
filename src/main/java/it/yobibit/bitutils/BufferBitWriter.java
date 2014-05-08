package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;


public class BufferBitWriter implements BitWriter {

	private final BitListSize size;	
	protected final int maxRecords;
    private final RandomAccessFile raf;
    private final FileChannel fileChannel;
	private final MappedByteBuffer fileBuffer;
	protected final IntBuffer intBuffer;
	protected int recordCount = 0;
	protected int buffer;
	protected int bufferPos;
	protected static final int MAX_POS = 32;
	
	
	public BufferBitWriter(File file, BitListSize size, int maxRecords) throws IOException {
		this.size = size;
		this.maxRecords = maxRecords;

		raf = new RandomAccessFile(file, "rw");
		
		int sizeInBytes = getSizeInBytes(size, maxRecords);
		raf.setLength(sizeInBytes);
		
		fileChannel = raf.getChannel();
		fileBuffer = fileChannel.map(MapMode.READ_WRITE, 0, sizeInBytes);
		intBuffer = fileBuffer.asIntBuffer();
		
		buffer = 0;
		bufferPos = 0;		
	}
	
	
	public BitReader getReader() {
		if (bufferPos != 0) {
			int pos = intBuffer.position();
			intBuffer.put(buffer);
			intBuffer.position(pos);
		}

		IntBuffer readOnlyBuffer = intBuffer.asReadOnlyBuffer();
		readOnlyBuffer.position(0);
		return new BufferBitReader(readOnlyBuffer, size);
	}
	
	private static int getSizeInBytes(BitListSize recordSizeInBits, int recordCount) {
		double sizeInBits = ((double) recordCount) * recordSizeInBits.get();
		
		int sizeInInt = (int) Math.ceil(sizeInBits / Integer.SIZE);
		int sizeInBytes = sizeInInt * (Integer.SIZE / Byte.SIZE);
		
		return sizeInBytes;
	}
	
	@Override
	public void write(int value) throws IOException {
		if (recordCount + 1 > maxRecords) {
			throw new IOException("Already inserted all available records: " + maxRecords);
		}
		
		for (int i = 0; i < size.get(); i++) {
			if (Bits.get(value, i) != 0) {
				buffer = Bits.set(buffer, bufferPos);
			}
			bufferPos++;
		}
		
		if (bufferPos == MAX_POS) {
			intBuffer.put(buffer);
			buffer = 0;
			bufferPos = 0;
		}
		
		recordCount++;
	}
	
	@Override
	public void flush() throws IOException {
		fileBuffer.force();
	}
	
	@Override
	public void close() throws IOException {
		if (bufferPos != 0) {
			intBuffer.put(buffer);
		}
		fileBuffer.force();
		raf.setLength(intBuffer.position() * 4);
		raf.close();
	}
}