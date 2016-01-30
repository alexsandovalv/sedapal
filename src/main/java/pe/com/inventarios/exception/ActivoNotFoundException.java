package pe.com.inventarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No encontrado")
public class ActivoNotFoundException extends RuntimeException{
	
	public ActivoNotFoundException() {
		super();
	}
	
	public ActivoNotFoundException(String message) {
		super(message);
	}

}
