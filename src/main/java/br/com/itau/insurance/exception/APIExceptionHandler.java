package br.com.itau.insurance.exception;

import br.com.itau.insurance.exception.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
@RequiredArgsConstructor
public class APIExceptionHandler {

    Logger logger = LoggerFactory.getLogger(APIExceptionHandler.class);

    private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String BAD_REQUEST_ERROR = "BAD_REQUEST_ERROR";

    private static final String UNEXPECTED_ERROR_MSG = "Ocorreu um erro inesperado";
    private static final String VALIDATION_ERROR_MSG = "Ocorreu um erro de validação";
    private static final String BAD_REQUEST_ERROR_MSG = "Ocorreu um erro de requisição";

    @ExceptionHandler(Exception.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handle(Exception exception) {
        logger.error(UNEXPECTED_ERROR_MSG, exception);
        var errorResponse = buildErrorResponse(INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR_MSG);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handle(HttpMessageNotReadableException exception) {
        logger.error(VALIDATION_ERROR_MSG, exception);

        var errorResponse = buildErrorResponse(VALIDATION_ERROR, "JSON inválido.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException exception) {
        logger.error(BAD_REQUEST_ERROR_MSG, exception);

        var errorResponse = buildErrorResponse(BAD_REQUEST_ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        logger.error(VALIDATION_ERROR_MSG, exception);

        var errorResponse = buildErrorResponse(VALIDATION_ERROR, VALIDATION_ERROR_MSG);

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            var errorDetail = new ErrorResponse.FieldError();
            errorDetail.setField(fieldError.getField());
            errorDetail.setMessage(fieldError.getDefaultMessage());

            errorResponse.getFields().add(errorDetail);
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handle(ConstraintViolationException exception) {
        logger.error(VALIDATION_ERROR_MSG, exception);

        var errorResponse = buildErrorResponse(VALIDATION_ERROR, VALIDATION_ERROR_MSG);

        for (var validation : exception.getConstraintViolations()) {
            var errorDetail = new ErrorResponse.FieldError();
            errorDetail.setField(validation.getPropertyPath().toString());
            errorDetail.setMessage(validation.getMessage());

            errorResponse.getFields().add(errorDetail);
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handle(MissingServletRequestParameterException exception) {
        logger.error(VALIDATION_ERROR_MSG, exception);

        var errorResponse = buildErrorResponse(VALIDATION_ERROR, VALIDATION_ERROR_MSG);

        var errorField = new ErrorResponse.FieldError();
        errorField.setField(exception.getParameterName());
        errorField.setMessage(exception.getMessage());
        errorResponse.getFields().add(errorField);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handle(IllegalArgumentException exception) {
        logger.error(VALIDATION_ERROR_MSG, exception);

        var errorResponse = buildErrorResponse(VALIDATION_ERROR, VALIDATION_ERROR_MSG);

        var errorField = new ErrorResponse.FieldError();
        errorField.setField(VALIDATION_ERROR);
        errorField.setMessage(exception.getMessage());
        errorResponse.getFields().add(errorField);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handle(BusinessException exception) {
        logger.error(exception.getMessage(), exception);

        var errorResponse = buildErrorResponse(INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR_MSG);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IntegrationException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handle(IntegrationException exception) {
        logger.error(exception.getMessage(), exception.getException());

        var errorResponse = buildErrorResponse(INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR_MSG);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse buildErrorResponse(String errorCode, String message) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .fields(new ArrayList<>())
                .build();
    }

}
