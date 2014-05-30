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