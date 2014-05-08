package it.yobibit.bitutils;

import java.io.Closeable;
import java.io.IOException;

public interface BitWriter extends Closeable {

	void write(int value) throws IOException;
	
	void flush() throws IOException;
	
}