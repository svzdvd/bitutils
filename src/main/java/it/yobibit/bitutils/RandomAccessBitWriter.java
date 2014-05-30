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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class RandomAccessBitWriter implements BitWriter {

	private final RandomAccessFile raf;
	private final BitListSize size;
	private int buffer;
	private int bufferPos;
	private static final int MAX_POS = 32;
	
	
	public RandomAccessBitWriter(File file, BitListSize size) throws FileNotFoundException {
		this.raf = new RandomAccessFile(file, "rw");
		this.size = size;
		this.buffer = 0;
		this.bufferPos = 0;
	}
	
	
	@Override
	public void write(int value) throws IOException {
		for (int i = 0; i < size.get(); i++) {
			if (Bits.get(value, i) != 0) {
				buffer = Bits.set(buffer, bufferPos);
			}
			bufferPos++;
		}
		
		if (bufferPos == MAX_POS) {
			raf.writeInt(buffer);
			buffer = 0;
			bufferPos = 0;
		}
	}	
	
	@Override
	public void flush() throws IOException {
		raf.getChannel().force(false);
	}
	
	@Override
	public void close() throws IOException {
		if (bufferPos != 0) {
			raf.writeInt(buffer);
		}
		raf.close();
	}
}
