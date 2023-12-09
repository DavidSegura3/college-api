package com.mitocode.college.exceptions;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Component
@Order(-1)
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private static final String MESSAGE = "message";
    private static final String STATUS = "status";

    public WebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext,
                               ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse); //req -> renderErrorResponse(req)
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest serverRequest) {
        Map<String, Object> generalError = getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
        Map<String, Object> customError = new HashMap<>();

        HttpStatus status = INTERNAL_SERVER_ERROR;
        String statusCode = String.valueOf(generalError.get(STATUS));
        Throwable error = getError(serverRequest);

        switch (statusCode) {
            case "400" -> {
                customError.put(MESSAGE, error.getMessage());
                customError.put(STATUS, 400);
                status = BAD_REQUEST;
            }
            case "404" -> {
                customError.put(MESSAGE, error.getMessage());
                customError.put(STATUS, 404);
                status = NOT_FOUND;
            }
            case "401", "403" -> {
                customError.put(MESSAGE, error.getMessage());
                customError.put(STATUS, 401);
                status = UNAUTHORIZED;
            }
            case "500" -> {
                customError.put(MESSAGE, error.getMessage());
                customError.put(STATUS, 500);
            }
            default -> {
                customError.put(MESSAGE, error.getMessage());
                customError.put(STATUS, 418);
                status = I_AM_A_TEAPOT;
            }
        }
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customError));
    }
}

