package com.example.ms.venta.exception;


public class ProveedorNotFoundException extends RuntimeException {
    public ProveedorNotFoundException(String message) {
        super(message);
    }
}