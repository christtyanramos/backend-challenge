package br.com.itau.insurance.core;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;

import java.util.Optional;
import java.util.UUID;

public interface ProductIntegration {

    Optional<ProductEntity> findById(UUID id);

    ProductEntity create(Product product);

    ProductEntity update(Product product);
}
