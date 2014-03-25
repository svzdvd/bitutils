package it.yobibit.bitutils;

import java.io.IOException;

public interface BitReader {

	void seek(long recordOffset) throws IOException;

	int read() throws IOException;

	int read(long recordOffset) throws IOException;
	
	void close() throws IOException;

}