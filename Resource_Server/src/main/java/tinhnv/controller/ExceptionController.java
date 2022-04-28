package tinhnv.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tinhnv.exception.*;

import javax.persistence.*;

@RestControllerAdvice
public class ExceptionController {

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<String> accountNotFound(AccountNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}
	
	@ExceptionHandler(AccountExistException.class)
	public ResponseEntity<String> accountExist(AccountExistException exception) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exception.getMessage());
	}
	
	@ExceptionHandler(FormTransferException.class)
	public ResponseEntity<String> formError(FormTransferException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(exception.getMessage());
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> entityNotFound(EntityNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(exception.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> accessDenied(Exception exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(exception.getMessage());
	}
}
