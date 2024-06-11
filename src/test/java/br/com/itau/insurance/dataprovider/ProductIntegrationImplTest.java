package br.com.itau.insurance.dataprovider;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.dataprovider.persistence.ProductRepository;
import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;
import br.com.itau.insurance.exception.IntegrationException;
import jakarta.persistence.PersistenceException;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductIntegrationImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductIntegrationImpl productIntegrationImpl;

    @BeforeEach
    void setUp() {
        productIntegrationImpl = new ProductIntegrationImpl(productRepository);
    }

    @Test
    @DisplayName("Should find by id product successfully")
    void shouldFindByIdProductEntitySuccessfully() {
        // GIVEN a request to find
        var productId = UUID.fromString("efa433b3-0d42-488d-ade8-bff27e68f222");

        doReturn(Optional.of(getProductEntityMock())).when(productRepository).findById(any(UUID.class));

        // WHEN execute productIntegration
        Optional<ProductEntity> productResponse = productIntegrationImpl.findById(productId);

        // THEN verify calls
        verify(productRepository, times(1)).findById(any(UUID.class));

        // AND verify assertions
        assertNotNull(productResponse);
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductEntitySuccessfully() {
        // GIVEN a request to create
        var product = getProductMock(null);

        doReturn(getProductEntityMock()).when(productRepository).save(any(ProductEntity.class));

        // WHEN execute productIntegration
        ProductEntity productResponse = productIntegrationImpl.create(product);

        // THEN verify calls
        verify(productRepository, times(1)).save(any());

        // AND verify assertions
        assertNotNull(productResponse);
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProductEntitySuccessfully() {
        // GIVEN a request to update
        var product = getProductMock("1b4147af-74fb-40ff-bd5b-039340feef1d");

        doReturn(getProductEntityMock()).when(productRepository).save(any(ProductEntity.class));

        // WHEN execute productIntegration
        ProductEntity productResponse = productIntegrationImpl.update(product);

        // THEN verify calls
        verify(productRepository, times(1)).save(any());

        // AND verify assertions
        assertNotNull(productResponse);
    }

    @Test
    @DisplayName("Should find by id product unsuccessfully when throws IntegrationException")
    void shouldFindByIdProductUnsuccessfullyWhenThrowsIntegrationException() {
        // GIVEN a request to create

        doThrow(PersistenceException.class).when(productRepository).findById(any(UUID.class));

        // WHEN execute productIntegration
        assertThrows(IntegrationException.class, () ->
                productIntegrationImpl.findById(UUID.fromString("efa433b3-0d42-488d-ade8-bff27e68f222")));

        // THEN verify calls
        verify(productRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should create product unsuccessfully when throws IntegrationException")
    void shouldCreateProductUnsuccessfullyWhenThrowsIntegrationException() {
        // GIVEN a request to create

        doThrow(PersistenceException.class).when(productRepository).save(any(ProductEntity.class));

        // WHEN execute productIntegration
        assertThrows(IntegrationException.class, () ->
                productIntegrationImpl.create(getProductMock(null)));

        // THEN verify calls
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should update product unsuccessfully when throws IntegrationException")
    void shouldUpdateProductUnsuccessfullyWhenThrowsIntegrationException() {
        // GIVEN a request to update

        doThrow(PersistenceException.class).when(productRepository).save(any(ProductEntity.class));

        // WHEN execute productIntegration
        assertThrows(IntegrationException.class, () ->
                productIntegrationImpl.update(getProductMock("efa433b3-0d42-488d-ade8-bff27e68f222")));

        // THEN verify calls
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    private Product getProductMock(String id) {
        return Product.builder()
                .id(id != null ? UUID.fromString(id) : null)
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
