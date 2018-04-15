package be.glever.ant;

import java.io.IOException;

public class AntException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public AntException(String message) {
		super(message);
	}

	public AntException(String message, IOException e) {
		super(message, e);
	}

	public AntException(Throwable t) {
		super(t);
	}

}
