package br.com.itau.insurance.core.usecase.utils;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UseCaseUtils {

    public static BigDecimal evaluateChargePrice(BigDecimal standardPrice, CategoryType categoryType) {
        BigDecimal iof = standardPrice.multiply(categoryType.getIof());
        BigDecimal pis = standardPrice.multiply(categoryType.getPis());
        BigDecimal cofins = standardPrice.multiply(categoryType.getCofins());
        return standardPrice.add(iof).add(pis).add(cofins).setScale(2, RoundingMode.HALF_DOWN);
    }

    public static Product buildProduct(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(CategoryType.toEnum(entity.getCategory()))
                .standardPrice(entity.getStandardPrice())
                .chargePrice(entity.getChargePrice())
                .build();
    }
}
