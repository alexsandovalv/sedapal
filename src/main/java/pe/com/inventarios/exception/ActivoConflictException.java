package pe.com.inventarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Conlficto en la solicitud..")
public class ActivoConflictException extends RuntimeException{
	
	public ActivoConflictException() {
		super();
	}
	
	public ActivoConflictException(String message) {
		super(message);
	}

}
