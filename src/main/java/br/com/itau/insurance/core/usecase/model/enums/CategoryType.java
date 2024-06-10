package br.com.itau.insurance.core.usecase.model.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum CategoryType {

    LIFE(1, "VIDA", BigDecimal.valueOf(0.01), BigDecimal.valueOf(0.022), BigDecimal.ZERO),
    AUTOMOBILE(2, "AUTO", BigDecimal.valueOf(0.055), BigDecimal.valueOf(0.04), BigDecimal.valueOf(0.01)),
    TRAVEL(3, "VIAGEM", BigDecimal.valueOf(0.02), BigDecimal.valueOf(0.04), BigDecimal.valueOf(0.01)),
    HOME(4, "RESIDENCIAL", BigDecimal.valueOf(0.04), BigDecimal.ZERO, BigDecimal.valueOf(0.03)),
    PROPERTY(5, "PATRIMONIAL", BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.03), BigDecimal.ZERO);

    private final Integer id;
    private final String description;
    private final BigDecimal iof;
    private final BigDecimal pis;
    private final BigDecimal cofins;

    CategoryType(Integer id, String description, BigDecimal iof, BigDecimal pis, BigDecimal cofins) {
        this.id = id;
        this.description = description;
        this.iof = iof;
        this.pis = pis;
        this.cofins = cofins;
    }

    public static CategoryType toEnum(Integer id) {

        if (id == null) {
            return null;
        }

        for (CategoryType x : CategoryType.values()) {
            if (id.equals(x.getId())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + id);
    }

    public static CategoryType toEnum(String description) {

        if (description == null) {
            return null;
        }

        for (CategoryType x : CategoryType.values()) {
            if (description.equals(x.getDescription())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + description);
    }
}
