package br.com.itau.insurance.core.usecase;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.dataprovider.ProductIntegration;
import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ProductCreatorUseCase {

    private final ProductIntegration productIntegration;

    public Product execute(Product product) {

        product.setChargePrice(evaluateChargePrice(product.getStandardPrice(), product.getCategory()));
        var entity = productIntegration.create(product);
        var response = buildProduct(entity);

        return response;
    }

    private BigDecimal evaluateChargePrice(BigDecimal standardPrice, CategoryType categoryType) {
        BigDecimal iof = standardPrice.multiply(categoryType.getIof());
        BigDecimal pis = standardPrice.multiply(categoryType.getPis());
        BigDecimal cofins = standardPrice.multiply(categoryType.getCofins());
        return standardPrice.add(iof).add(pis).add(cofins).setScale(2, RoundingMode.HALF_DOWN);
    }

    private Product buildProduct(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(CategoryType.toEnum(entity.getCategory()))
                .standardPrice(entity.getStandardPrice())
                .chargePrice(entity.getChargePrice())
                .build();
    }
}
