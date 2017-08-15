package be.glever.ant;

import java.io.IOException;

public class AntException extends Exception{
	private static final long serialVersionUID = 1L;

	public AntException(String message) {
		super(message);
	}

	public AntException(String message, IOException e) {
		super(message, e);
	}

}
