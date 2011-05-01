package dst2.ejb.exceptions;

public class InvalidCredentialsException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidCredentialsException() {
	}

	public InvalidCredentialsException(String arg0) {
		super(arg0);
	}

	public InvalidCredentialsException(Throwable arg0) {
		super(arg0);

	}

	public InvalidCredentialsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
