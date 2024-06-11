package br.com.itau.insurance.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InvalidProductIdException extends BusinessException {

    private final UUID id;

    public InvalidProductIdException(UUID id) {
        super("INVALID_PRODUCT_ID", "Id inválido ou não encontrado. Id: " + id);
        this.id = id;
    }

}
