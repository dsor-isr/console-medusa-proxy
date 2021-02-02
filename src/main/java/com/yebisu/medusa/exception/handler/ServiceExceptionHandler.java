//package com.yebisu.medusa.exception.handler;
//
//import com.yebisu.medusa.exception.ResourceNotFoundException;
//import com.yebisu.medusa.exception.dto.ErrorDetail;
//import com.yebisu.medusa.exception.dto.ValidationError;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.function.Consumer;
//
//@RestControllerAdvice
//@Slf4j
//public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<?> handleUnexpectedException(Exception e) {
//        logger.error(e.getMessage());
//        return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<?> handleResourceNotFoundException(Exception e) {
//        logger.error(e.getMessage());
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//    }
//
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        log.info(ex.getMessage());
//        var errorDetail = new ErrorDetail();
//        errorDetail.setTimeStamp(new Date().getTime());
//        errorDetail.setStatus(status.value());
//        errorDetail.setTitle("Validation Field");
//        errorDetail.setDetail("Input validation failed!");
//        errorDetail.setDeveloperMessage(ex.getClass().getName());
//        ex.getBindingResult().getFieldErrors().forEach(enrichValidationErrors(errorDetail));
//        log.info(ex.getMessage());
//        return handleExceptionInternal(ex, errorDetail, headers, status, request);
//    }
//
//    private Consumer<FieldError> enrichValidationErrors(ErrorDetail errorDetail) {
//        return fieldError -> addValidationError(errorDetail, fieldError);
//    }
//
//    private void addValidationError(ErrorDetail errorDetail, FieldError fieldError) {
//        List<ValidationError> validationErrors = errorDetail.getErrors()
//                .computeIfAbsent(fieldError.getField(), k -> new ArrayList<>());
//
//        ValidationError validationError = new ValidationError();
//        validationError.setCode(fieldError.getCode());
//        validationError.setMessage(fieldError.getDefaultMessage());
//        validationErrors.add(validationError);
//    }
//
//
//}
