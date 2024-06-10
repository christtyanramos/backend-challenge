package br.com.itau.insurance.core.usecase.model;

import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

    private UUID id;
    private String name;
    private CategoryType category;
    private BigDecimal standardPrice;
    private BigDecimal chargePrice;
}
