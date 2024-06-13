package br.com.itau.insurance.core.usecase;

import br.com.itau.insurance.core.ProductGateway;
import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;
import br.com.itau.insurance.exception.IntegrationException;
import br.com.itau.insurance.exception.InvalidProductIdException;
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
class ProductUpdaterUseCaseTest {

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private ProductUpdaterUseCase productUpdaterUseCase;

    @BeforeEach
    void setUp() {
        productUpdaterUseCase = new ProductUpdaterUseCase(productGateway);
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProductSuccessfully() {
        // GIVEN a request to update
        var product = Optional.of(getProductMock());

        doReturn(product).when(productGateway).findById(any(UUID.class));
        doReturn(getProductEntityMock()).when(productGateway).update(any(Product.class));

        // WHEN execute productUpdaterUseCase
        Product productResponse = productUpdaterUseCase.execute(product.get());

        // THEN verify calls
        verify(productGateway, times(1)).findById(any(UUID.class));
        verify(productGateway, times(1)).update(any(Product.class));

        // AND verify assertions
        assertNotNull(productResponse);
        assertEquals(BigDecimal.valueOf(103.2), productResponse.getChargePrice());
    }

    @Test
    @DisplayName("Should update product unsuccessfully when throws InvalidProductIdException")
    void shouldUpdateProductUnsuccessfullyWhenThrowsInvalidProductIdException() {
        // GIVEN a request to update
        var product = Optional.empty();

        doReturn(product).when(productGateway).findById(any(UUID.class));

        // WHEN execute productUpdaterUseCase
        InvalidProductIdException exception = assertThrows(InvalidProductIdException.class, () ->
                productUpdaterUseCase.execute(getProductMock()));

        // THEN verify calls
        verify(productGateway, times(1)).findById(any(UUID.class));
        verify(productGateway, times(0)).update(any(Product.class));

        // AND verify assertions
        assertEquals("Id inválido ou não encontrado. Id: efa433b3-0d42-488d-ade8-bff27e68f222", exception.getMessage());
    }

    @Test
    @DisplayName("Should update product unsuccessfully when throws IntegrationException in findById method")
    void shouldUpdateProductUnsuccessfullyWhenThrowsIntegrationExceptionInFindById() {
        // GIVEN a request to update

        doThrow(IntegrationException.class).when(productGateway).findById(any(UUID.class));

        // WHEN execute productUpdaterUseCase
        assertThrows(IntegrationException.class, () ->
                productUpdaterUseCase.execute(getProductMock()));

        // THEN verify calls
        verify(productGateway, times(1)).findById(any(UUID.class));
        verify(productGateway, times(0)).update(any(Product.class));
    }

    @Test
    @DisplayName("Should update product unsuccessfully when throws IntegrationException in update method")
    void shouldUpdateProductUnsuccessfullyWhenThrowsIntegrationExceptionInUpdate() {
        // GIVEN a request to update
        var product = Optional.of(getProductMock());

        doReturn(product).when(productGateway).findById(any(UUID.class));
        doThrow(IntegrationException.class).when(productGateway).update(any(Product.class));

        // WHEN execute productUpdaterUseCase
        assertThrows(IntegrationException.class, () ->
                productUpdaterUseCase.execute(getProductMock()));

        // THEN verify calls
        verify(productGateway, times(1)).findById(any(UUID.class));
        verify(productGateway, times(1)).update(any(Product.class));
    }

    private Product getProductMock() {
        return Product.builder()
                .id(UUID.fromString("efa433b3-0d42-488d-ade8-bff27e68f222"))
                .name("Seguro de Vida Individual")
                .category(CategoryType.toEnum("VIDA"))
                .standardPrice(BigDecimal.valueOf(100.0))
                .chargePrice(BigDecimal.valueOf(100.0))
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
