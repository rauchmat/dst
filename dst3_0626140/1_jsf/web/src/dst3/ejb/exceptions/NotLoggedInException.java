package dst3.ejb.exceptions;

public class NotLoggedInException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotLoggedInException() {
	}

	public NotLoggedInException(String arg0) {
		super(arg0);
	}

	public NotLoggedInException(Throwable arg0) {
		super(arg0);

	}

	public NotLoggedInException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
