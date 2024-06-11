package br.com.itau.insurance.core.usecase;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.dataprovider.ProductIntegration;
import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;
import br.com.itau.insurance.exception.IntegrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductCreatorUseCaseTest {

    @Mock
    private ProductIntegration productIntegration;

    @InjectMocks
    private ProductCreatorUseCase productCreatorUseCase;

    @BeforeEach
    void setUp() {
        productCreatorUseCase = new ProductCreatorUseCase(productIntegration);
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() {
        // GIVEN a request to create
        var product = Optional.of(getProductMock());

        doReturn(getProductEntityMock()).when(productIntegration).create(any(Product.class));

        // WHEN execute productCreatorUseCase
        Product productResponse = productCreatorUseCase.execute(product.get());

        // THEN verify calls
        verify(productIntegration, times(1)).create(any(Product.class));

        // AND verify assertions
        assertNotNull(productResponse);
        assertEquals(BigDecimal.valueOf(103.2), productResponse.getChargePrice());
    }

    @Test
    @DisplayName("Should create product unsuccessfully when throws IntegrationException in create method")
    void shouldCreateProductUnsuccessfullyWhenThrowsIntegrationExceptionInFindById() {
        // GIVEN a request to create

        doThrow(IntegrationException.class).when(productIntegration).create(any(Product.class));

        // WHEN execute productCreatorUseCase
        assertThrows(IntegrationException.class, () ->
                productCreatorUseCase.execute(getProductMock()));

        // THEN verify calls
        verify(productIntegration, times(1)).create(any(Product.class));
    }

    private Product getProductMock() {
        return Product.builder()
                .name("Seguro de Vida Individual")
                .category(CategoryType.toEnum("VIDA"))
                .standardPrice(BigDecimal.valueOf(100.0))
                .build();
    }

    private ProductEntity getProductEntityMock() {
        return ProductEntity.builder()
                .id(UUID.fromString("efa433b3-0d42-488d-ade8-bff27e68f222"))
                .name("Seguro de Vida Individual")
                .category(1)
                .standardPrice(BigDecimal.valueOf(100.0))
                .chargePrice(BigDecimal.valueOf(103.2))
                .build();
    }
}
