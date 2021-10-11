package com.epam.esm.exceptions;

import com.epam.esm.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError springHandleTagNotFound(TagNotFoundException exception) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), ErrorCode.TAG_NOT_FOUND.getErrorCode());
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError springHandleCertificateNotFound(GiftCertificateNotFoundException exception) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), ErrorCode.CERTIFICATE_NOT_FOUND.getErrorCode());
    }
}