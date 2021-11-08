package com.epam.esm.exceptions;

import com.epam.esm.enums.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static com.epam.esm.enums.ErrorCode.ARGUMENT_NOT_VALID;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError errorDetails = new ApiError(
                ex.getCause().getLocalizedMessage(), ARGUMENT_NOT_VALID.getErrorCode());
        ResponseEntity<Object> entity =
                new ResponseEntity<Object>(errorDetails, status);
        return entity;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError errorDetails = new ApiError(
                ex.getMessage(), ARGUMENT_NOT_VALID.getErrorCode());
        ResponseEntity<Object> entity =
                new ResponseEntity<Object>(errorDetails, status);
        return entity;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError springHandleIllegalArgumentException(IllegalArgumentException exception) {
        return new ApiError(exception.getMessage(), ErrorCode.TAG_NOT_FOUND.getErrorCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterValidationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError springHandleParameterValidationException(ParameterValidationException exception) {
        return new ApiError(exception.getMessage(), ErrorCode.TAG_NOT_FOUND.getErrorCode());
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError springHandleTagNotFound(TagNotFoundException exception) {
        return new ApiError(exception.getMessage(), ErrorCode.TAG_NOT_FOUND.getErrorCode());
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError springHandleCertificateNotFound(GiftCertificateNotFoundException exception) {
        return new ApiError(exception.getMessage(), ErrorCode.CERTIFICATE_NOT_FOUND.getErrorCode());
    }

}