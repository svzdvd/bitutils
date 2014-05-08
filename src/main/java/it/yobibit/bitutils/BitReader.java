package it.yobibit.bitutils;

import java.io.Closeable;
import java.io.IOException;

public interface BitReader extends Closeable {

	void seek(long recordOffset) throws IOException;

	int read() throws IOException;

	int read(long recordOffset) throws IOException;

}