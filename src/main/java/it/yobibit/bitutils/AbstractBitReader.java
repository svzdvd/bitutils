/**
 * Copyright 2013 Davide Savazzi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.yobibit.bitutils;

import it.yobibit.bitutils.Bits.BitListSize;

import java.io.IOException;

public abstract class AbstractBitReader implements BitReader {
	
	protected final int recordSize;
	protected final int recordsInBuffer;
	protected int buffer;
	protected int intPos;
	private int previousValue = -1;
	private long previousRecordOffset = -1;	
	protected static final int BUFFER_SIZE = Integer.SIZE;

	
	public AbstractBitReader(BitListSize size) {
		recordSize = size.get();
		recordsInBuffer = BUFFER_SIZE / recordSize;

		// reset (but don't call reset(): it's overridden by subclasses)
		intPos = BUFFER_SIZE;
		previousValue = -1;
		previousRecordOffset = -1;	
	}

	
	/**
	 * @see it.yobibit.bitutils.BitReader#reset()
	 */
	@Override
	public void reset() throws IOException {
		intPos = BUFFER_SIZE;		
		previousValue = -1;
		previousRecordOffset = -1;	
	}

	/**
	 * @see it.yobibit.bitutils.BitReader#read(long)
	 */
	@Override
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