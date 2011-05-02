package dst2.ejb.exceptions;

public class InvalidAssignmentException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidAssignmentException() {
	}

	public InvalidAssignmentException(String arg0) {
		super(arg0);
	}

	public InvalidAssignmentException(Throwable arg0) {
		super(arg0);

	}

	public InvalidAssignmentException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
