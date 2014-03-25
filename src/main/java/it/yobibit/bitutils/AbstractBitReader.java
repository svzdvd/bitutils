package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.IOException;

public abstract class AbstractBitReader implements BitReader {
	
	protected final int recordSize;
	protected final int recordsInBuffer;
	protected int buffer;
	protected int bufferPos;
	private int previousValue = -1;
	private long previousRecordOffset = -1;	
	protected static final int BUFFER_SIZE = Integer.SIZE;

	
	public AbstractBitReader(BitListSize size) throws IOException {
		this.recordSize = size.get();
		this.recordsInBuffer = BUFFER_SIZE / recordSize;
		this.bufferPos = BUFFER_SIZE;
	}

	
	public int read(long recordOffset) throws IOException {
		if (previousRecordOffset == recordOffset) {
			return previousValue;
		} else if ((previousRecordOffset + 1) != recordOffset) {
			// seek
			seek(recordOffset);
		}
		
		previousValue = read();
		previousRecordOffset = recordOffset;
		return previousValue;
	}
}