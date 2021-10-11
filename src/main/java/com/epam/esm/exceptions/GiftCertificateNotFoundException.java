package com.epam.esm.exceptions;

public class GiftCertificateNotFoundException extends RuntimeException {
    public GiftCertificateNotFoundException(String message) {
        super(message);
    }
}