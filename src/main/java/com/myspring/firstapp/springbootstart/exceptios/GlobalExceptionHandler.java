package com.myspring.firstapp.springbootstart.exceptios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.myspring.firstapp.springbootstart.controllers.UserNotFoundException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest wr)
	{
		ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), wr.getDescription(false));
		return new ResponseEntity(er,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> handleAllUserNotFound(Exception ex, WebRequest wr)
	{
		ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), wr.getDescription(false));
		return new ResponseEntity(er,HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("");
		ExceptionResponse er = new ExceptionResponse(new Date(), "Validation Failed!", ex.getBindingResult().toString());
		return new ResponseEntity(er,HttpStatus.BAD_REQUEST);
		
		
	}
}
