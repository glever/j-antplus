package be.glever.ant.util;

import java.io.ByteArrayOutputStream;

public class ByteArrayBuilder {

	private ByteArrayOutputStream baos;

	public ByteArrayBuilder() {
		this.baos = new ByteArrayOutputStream();
	}
	
	public ByteArrayBuilder write(byte ... bytes) {
		for(byte b : bytes) {
			baos.write(b);			
		}
		return this;
	}
	
	public byte[] toByteArray() {
		return baos.toByteArray();
	}
	
}
