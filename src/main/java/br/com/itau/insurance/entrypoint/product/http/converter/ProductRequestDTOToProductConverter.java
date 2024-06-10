package br.com.itau.insurance.entrypoint.product.http.converter;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.entrypoint.product.http.dto.request.ProductRequestDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductRequestDTOToProductConverter {

    public Product parseObject(ProductRequestDTO requestDTO) {
        return Product.builder()
                .name(requestDTO.getNome())
                .category(CategoryType.toEnum(requestDTO.getCategoria()))
                .standardPrice(requestDTO.getPrecoBase())
                .build();
    }

    public Product parseObject(String id, ProductRequestDTO requestDTO) {
        var product = parseObject(requestDTO);
        product.setId(UUID.fromString(id));
        return product;
    }
}
