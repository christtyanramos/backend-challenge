package br.com.itau.insurance.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "BusinessException(errorCode=" + this.errorCode + ", message=" + this.getMessage() + ")";
    }
}
