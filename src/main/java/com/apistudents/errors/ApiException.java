package com.apistudents.errors;

import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestControllerAdvice
public class ApiException extends ResponseEntityExceptionHandler {
	
	// erros comuns da api
	@Override
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class}) // erros mais comuns
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String msg = "";
		if(ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			for (ObjectError objError : list) {
				msg += objError.getDefaultMessage() + "\n";
			}
		}else {
			msg = ex.getMessage();
		}
			
		ObjectErr objErr = new ObjectErr();
		objErr.setErr(msg);
		objErr.setCode(status.value());
		return new ResponseEntity<>(objErr, headers, status);
	}
	
	// erros comuns de banco de dados
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
	protected ResponseEntity<Object> handleExceptionDataIntegrity(Exception ex){
		
		String msgs = "";
		if(ex instanceof DataIntegrityViolationException) {
			msgs = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
		}else {
			msgs = ex.getMessage();
		}
		ObjectErr objErr = new ObjectErr();
		objErr.setErr(msgs);
		objErr.setCode(500);
		return new ResponseEntity<Object>(objErr, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
