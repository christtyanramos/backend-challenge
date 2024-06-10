package br.com.itau.insurance.dataprovider.persistence;

import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
