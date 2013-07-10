package it.yobibit.bitutils;

import java.io.IOException;

public interface BitReader {

	void seek(long recordOffset) throws IOException;

	int read() throws IOException;

	void close() throws IOException;

}