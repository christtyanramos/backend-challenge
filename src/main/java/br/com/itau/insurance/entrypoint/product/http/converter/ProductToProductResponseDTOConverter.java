package br.com.itau.insurance.entrypoint.product.http.converter;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.entrypoint.product.http.dto.response.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductResponseDTOConverter {

    public ProductResponseDTO parseObject(Product response) {
        return ProductResponseDTO.builder()
                .id(response.getId().toString())
                .nome(response.getName())
                .categoria(response.getCategory().getDescription())
                .precoBase(response.getStandardPrice())
                .precoTarifado(response.getChargePrice())
                .build();
    }
}
