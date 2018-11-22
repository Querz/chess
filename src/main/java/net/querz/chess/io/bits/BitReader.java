package net.querz.chess.io.bits;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitReader extends DataInputStream {

	private int currentBit = 0;
	private byte buffer;

	public BitReader(InputStream in) {
		super(in);
	}

	public byte readBits(int bits) throws IOException {
		if (currentBit + bits > 8) {
			byte localBuffer = readByte();
			int right = currentBit + bits - 8;
			byte m = (byte) (buffer << 8 - bits + right);
			byte l = (byte) ((localBuffer & 0xFF) >>> 8 - right);
			currentBit = right;
			buffer = localBuffer;
			return (byte) (((m & 0xFF) >>> (8 - bits)) | l);
		} else {
			if (currentBit == 0) {
				buffer = readByte();
			}
			byte res = (byte) (((buffer << currentBit) & 0xFF) >>> 8 - bits);
			currentBit += bits;
			return res;
		}
	}
}
