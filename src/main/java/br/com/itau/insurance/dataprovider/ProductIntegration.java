package br.com.itau.insurance.dataprovider;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.dataprovider.persistence.ProductRepository;
import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductIntegration {

    private final ProductRepository productRepository;

    public Optional<ProductEntity> findById(UUID id) {
       return productRepository.findById(id);
    }

    public ProductEntity create(Product product) {
        var entity = ProductEntity.builder()
                .name(product.getName())
                .category(product.getCategory().getId())
                .standardPrice(product.getStandardPrice())
                .chargePrice(product.getChargePrice())
                .build();

        return productRepository.save(entity);
    }

    public ProductEntity update(Product product) {
        var entity = ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory().getId())
                .standardPrice(product.getStandardPrice())
                .chargePrice(product.getChargePrice())
                .build();

        return productRepository.save(entity);
    }
}
