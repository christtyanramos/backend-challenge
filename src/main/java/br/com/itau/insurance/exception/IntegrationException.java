package br.com.itau.insurance.exception;

import lombok.Getter;

@Getter
public class IntegrationException extends BusinessException {

    private final Exception exception;

    public IntegrationException(String errorCode, String message, Exception exception) {
        super(errorCode, message);
        this.exception = exception;
    }

}
