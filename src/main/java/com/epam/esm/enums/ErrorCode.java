package com.epam.esm.enums;

public enum ErrorCode {
    TAG_NOT_FOUND(40401),
    CERTIFICATE_NOT_FOUND(40402),
    BASE_NOT_FOUND(40400),
    ARGUMENT_NOT_VALID(40000);

    private long errorCode;

    ErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public long getErrorCode() {
        return errorCode;
    }
}