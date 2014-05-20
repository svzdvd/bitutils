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