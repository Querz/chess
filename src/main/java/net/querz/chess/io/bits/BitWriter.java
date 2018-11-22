package net.querz.chess.io.bits;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitWriter extends DataOutputStream {

	private int currentBit = 0;
	private byte buffer;

	public BitWriter(OutputStream out) {
		super(out);
	}

	public void writeBits(byte b, int bits) throws IOException {
		if (bits + currentBit > 8) {
			int rem = 8 - currentBit;
			buffer |= (b & 0xFF) >>> (bits - rem);
			write(buffer);
			buffer = (byte) (b << (8 - (currentBit = bits - rem)));
		} else {
			buffer |= b << (8 - (currentBit += bits));
		}
	}

	@Override
	public void flush() throws IOException {
		if (currentBit > 0) {
			write(buffer);
		}
	}
}
