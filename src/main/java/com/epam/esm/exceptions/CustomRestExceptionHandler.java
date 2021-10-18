package com.epam.esm.exceptions;

import com.epam.esm.enums.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.enums.ErrorCode.ARGUMENT_NOT_VALID;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        ApiError errorDetails = new ApiError(HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage(), ARGUMENT_NOT_VALID.getErrorCode());
        return handleExceptionInternal(ex, errorDetails, headers, errorDetails.getStatus(), request);
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError springHandleTagNotFound(TagNotFoundException exception) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), ErrorCode.TAG_NOT_FOUND.getErrorCode());
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError springHandleCertificateNotFound(GiftCertificateNotFoundException exception) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), ErrorCode.CERTIFICATE_NOT_FOUND.getErrorCode());
    }

}