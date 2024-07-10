

package com.github.rhafaelcosta.todolist.exceptions.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.github.rhafaelcosta.todolist.exceptions.EntityAlreadyExistsException;
import com.github.rhafaelcosta.todolist.responses.ErrorResponse;

import jakarta.persistence.EntityNotFoundException;

/**
 * Global exception hanhler using @ControllerAdvice to handle specific exceptions and
 * send appropriate HTTP responses
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handles EntityNotFoundException and returns a 404 Not Found response.
	 * @param ex
	 * @param request
	 * @return
	 */
    @ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleEntityNotFoundExceptions(Exception ex, WebRequest request) {
		var response = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	/**
	 * Handles EntityAlreadyExistsException and returns a 400 Bad Request response.
	 * @param ex
	 * @param request
	 * @return
	 */
    @ExceptionHandler(EntityAlreadyExistsException.class)
	public final ResponseEntity<ErrorResponse> handleEntityAlreadyExistsExceptions(Exception ex, WebRequest request) {
		var response = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

}