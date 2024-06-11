package br.com.itau.insurance.dataprovider;

import br.com.itau.insurance.core.ProductIntegration;
import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.dataprovider.persistence.ProductRepository;
import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;
import br.com.itau.insurance.exception.IntegrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductIntegrationImpl implements ProductIntegration {

    private final ProductRepository productRepository;

    public Optional<ProductEntity> findById(UUID id) {
        try {
            return productRepository.findById(id);
        } catch (Exception exception) {
            throw new IntegrationException("PRODUCT_FIND_BY_ID_INTEGRATION_ERROR", "Erro ao tentar localizar produto.", exception);
        }
    }

    public ProductEntity create(Product product) {
        try {
            var entity = buildProductEntity(product);
            return productRepository.save(entity);
        } catch (Exception exception) {
            throw new IntegrationException("PRODUCT_CREATE_INTEGRATION_ERROR", "Erro ao tentar criar produto.", exception);
        }
    }

    public ProductEntity update(Product product) {
        try {
            var entity = buildProductEntity(product);
            return productRepository.save(entity);
        } catch (Exception exception) {
            throw new IntegrationException("PRODUCT_UPDATE_INTEGRATION_ERROR", "Erro ao tentar atualizar produto.", exception);
        }
    }

    private static ProductEntity buildProductEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId() != null ? product.getId() : null)
                .name(product.getName())
                .category(product.getCategory().getId())
                .standardPrice(product.getStandardPrice())
                .chargePrice(product.getChargePrice())
                .build();
    }
}
