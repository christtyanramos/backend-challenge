package br.com.itau.insurance.core.usecase;

import br.com.itau.insurance.core.ProductGateway;
import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.exception.InvalidProductIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.itau.insurance.core.usecase.utils.UseCaseUtils.buildProduct;
import static br.com.itau.insurance.core.usecase.utils.UseCaseUtils.evaluateChargePrice;

@Service
@RequiredArgsConstructor
public class ProductUpdaterUseCase {

    private final ProductGateway productGateway;

    public Product execute(Product product) {

        if (productGateway.findById(product.getId()).isPresent()) {
            product.setChargePrice(evaluateChargePrice(product.getStandardPrice(), product.getCategory()));
            var entity = productGateway.update(product);
            return buildProduct(entity);
        }
        throw new InvalidProductIdException(product.getId());
    }

}
