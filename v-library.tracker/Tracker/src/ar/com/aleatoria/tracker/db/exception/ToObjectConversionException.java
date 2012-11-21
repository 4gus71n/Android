package ar.com.aleatoria.tracker.db.exception;

public class ToObjectConversionException extends RuntimeException {

	private static final long serialVersionUID = 2810392973006751663L;
	public Throwable innerException;
	public ToObjectConversionException(Throwable innerException) {
		super();
		this.innerException = innerException;
	}
}
