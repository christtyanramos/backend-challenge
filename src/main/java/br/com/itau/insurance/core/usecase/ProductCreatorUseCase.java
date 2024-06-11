package br.com.itau.insurance.core.usecase;

import br.com.itau.insurance.core.ProductIntegration;
import br.com.itau.insurance.core.usecase.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.itau.insurance.core.usecase.utils.UseCaseUtils.buildProduct;
import static br.com.itau.insurance.core.usecase.utils.UseCaseUtils.evaluateChargePrice;

@Service
@RequiredArgsConstructor
public class ProductCreatorUseCase {

    private final ProductIntegration productIntegration;

    public Product execute(Product product) {

        product.setChargePrice(evaluateChargePrice(product.getStandardPrice(), product.getCategory()));
        var entity = productIntegration.create(product);
        return buildProduct(entity);
    }

}
