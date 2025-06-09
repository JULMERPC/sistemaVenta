package com.example.msalmacen.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404:
                if (methodKey.contains("obtenerProveedor")) {
                    return new RuntimeException("Proveedor no encontrado");
                }
                break;
            case 500:
                return new RuntimeException("Error interno del servidor de proveedores");
            case 503:
                return new RuntimeException("Servicio de proveedores no disponible");
            default:
                return new RuntimeException("Error de comunicación con el servicio de proveedores");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}