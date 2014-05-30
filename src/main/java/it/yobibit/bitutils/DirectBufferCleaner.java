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

import java.nio.Buffer;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

public class DirectBufferCleaner {

	public static void cleanDirectBuffer(Buffer mappedFile) {
		if (mappedFile instanceof DirectBuffer) {
			Cleaner cleaner = ((DirectBuffer) mappedFile).cleaner();
			if (cleaner != null) {
				cleaner.clean(); 
			}
		}
	}
}