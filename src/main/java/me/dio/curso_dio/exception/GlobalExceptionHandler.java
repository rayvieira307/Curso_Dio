package me.dio.curso_dio.exception;

import java.util.NoSuchElementException;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ch.qos.logback.classic.Logger;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	    
	   private final Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	    @ExceptionHandler(IllegalArgumentException.class)
	     public ResponseEntity<String> handle(IllegalArgumentException businessException) {
	    	 
	    	 return new ResponseEntity<>(businessException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	    	 
	     }
	    
	    
	    @ExceptionHandler(NoSuchElementException.class)
	     public ResponseEntity<String> handle(NoSuchElementException notFoundException) {
	    	 
	    	 return new ResponseEntity<>("Este Id nao existe", HttpStatus.NOT_FOUND);
	    	 
	     }
	    
	    
	    @ExceptionHandler(Throwable.class)
	     public ResponseEntity<String> handle(Throwable unexpectedException) {
	    	
	    	 var message = "Erro interno veja os logs do servidor. ";
	    	 logger.error(message, unexpectedException);
	    	 return new ResponseEntity<>("Erro no servidor", HttpStatus.INTERNAL_SERVER_ERROR);
	    	 
	     }


}
