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