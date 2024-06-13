package br.com.itau.insurance.dataprovider;

import br.com.itau.insurance.InsuranceApplication;
import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.dataprovider.persistence.ProductRepository;
import br.com.itau.insurance.dataprovider.persistence.entity.ProductEntity;
import br.com.itau.insurance.exception.IntegrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsuranceApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductGatewayImplIngTest {

    @Autowired
    private ProductRepository productRepository;

    @InjectMocks
    private ProductGatewayImpl productGatewayImpl;

    @BeforeEach
    private void setUp() {
        productGatewayImpl = new ProductGatewayImpl(productRepository);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:testdb", "admin", "123");
    }

    @Test
    @DisplayName("Should find by id product successfully")
    void shouldFindByIdSuccessfully() {
        // GIVEN a request to create
        var product = getProductMock(
                null
                , "Seguro de Vida Individual"
                , CategoryType.toEnum("VIDA")
                , BigDecimal.valueOf(100.0)
                , BigDecimal.valueOf(103.2));
        var productCreateResponse = productGatewayImpl.create(product);

        // AND a request to find
        var productId = productCreateResponse.getId();

        // WHEN execute productRepository
        Optional<ProductEntity> productResponse = productGatewayImpl.findById(productId);

        // THEN verify assertions
        assertNotNull(productResponse.get());
        assertNotNull(productResponse.get().getId());
        assertEquals("Seguro de Vida Individual", productResponse.get().getName());
        assertEquals(CategoryType.LIFE.getId(), productResponse.get().getCategory());
        assertEquals(BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_DOWN), productResponse.get().getStandardPrice());
        assertEquals(BigDecimal.valueOf(103.20).setScale(2, RoundingMode.HALF_DOWN), productResponse.get().getChargePrice());
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateSuccessfully() {
        // GIVEN a request to create
        var product = getProductMock(null
                , "Seguro de Vida Individual"
                , CategoryType.toEnum("VIDA")
                , BigDecimal.valueOf(100.0)
                , BigDecimal.valueOf(103.2));

        // WHEN execute productRepository
        ProductEntity productResponse = productGatewayImpl.create(product);

        // THEN verify assertions
        assertNotNull(productResponse);
        assertNotNull(productResponse.getId());
        assertEquals("Seguro de Vida Individual", productResponse.getName());
        assertEquals(CategoryType.LIFE.getId(), productResponse.getCategory());
        assertEquals(BigDecimal.valueOf(100.0), productResponse.getStandardPrice());
        assertEquals(BigDecimal.valueOf(103.2), productResponse.getChargePrice());
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateSuccessfully() {
        // GIVEN a request to create
        var product = getProductMock(
                null
                , "Seguro de Vida Individual"
                , CategoryType.toEnum("VIDA")
                , BigDecimal.valueOf(100.0)
                , BigDecimal.valueOf(103.2));
        var productCreateResponse = productGatewayImpl.create(product);

        // AND a request to update
        var productUpdate = getProductMock(
                productCreateResponse.getId().toString()
                , "Seguro de Auto"
                , CategoryType.toEnum("AUTO")
                , BigDecimal.valueOf(50.0)
                , BigDecimal.valueOf(55.25));

        // WHEN execute productRepository
        ProductEntity productUpdateResponse = productGatewayImpl.update(productUpdate);

        // THEN verify assertions
        assertNotNull(productUpdateResponse);
        assertEquals(productCreateResponse.getId(), productUpdateResponse.getId());
        assertEquals("Seguro de Auto", productUpdateResponse.getName());
        assertEquals(CategoryType.AUTOMOBILE.getId(), productUpdateResponse.getCategory());
        assertEquals(BigDecimal.valueOf(50.0), productUpdateResponse.getStandardPrice());
        assertEquals(BigDecimal.valueOf(55.25), productUpdateResponse.getChargePrice());
    }

    @Test
    @DisplayName("Should find by id product unsuccessfully when throws IntegrationException")
    void shouldFindByIdUnsuccessfullyWhenThrowsIntegrationException() throws SQLException {
        // GIVEN a request to create
        var product = getProductMock(
                null
                , "Seguro de Vida Individual"
                , CategoryType.toEnum("VIDA")
                , BigDecimal.valueOf(100.0)
                , BigDecimal.valueOf(103.2));
        var productCreateResponse = productGatewayImpl.create(product);

        // AND a request to find
        var productId = productCreateResponse.getId();

        // AND shutdown in memory database
        Connection con = getConnection();
        con.createStatement().execute("DROP ALL OBJECTS DELETE FILES");

        // WHEN execute productRepository
        IntegrationException integrationException = assertThrows(IntegrationException.class, () ->
                productGatewayImpl.findById(productId));

        // THEN verify calls
        assertEquals("Erro ao tentar localizar produto.", integrationException.getMessage());
    }

    @Test
    @DisplayName("Should create product unsuccessfully when throws IntegrationException")
    void shouldCreateUnsuccessfullyWhenThrowsIntegrationException() throws SQLException {
        // GIVEN a request to create
        var product = getProductMock(null
                , "Seguro de Vida Individual"
                , CategoryType.toEnum("VIDA")
                , BigDecimal.valueOf(100.0)
                , BigDecimal.valueOf(103.2));

        // AND shutdown in memory database
        Connection con = getConnection();
        con.createStatement().execute("DROP ALL OBJECTS DELETE FILES");

        // WHEN execute productRepository
        IntegrationException integrationException = assertThrows(IntegrationException.class, () ->
                productGatewayImpl.create(product));

        // THEN verify calls
        assertEquals("Erro ao tentar criar produto.", integrationException.getMessage());
    }

    @Test
    @DisplayName("Should update product unsuccessfully when throws IntegrationException")
    void shouldUpdateUnsuccessfullyWhenThrowsIntegrationException() throws SQLException {
        // GIVEN a request to create
        var product = getProductMock(
                null
                , "Seguro de Vida Individual"
                , CategoryType.toEnum("VIDA")
                , BigDecimal.valueOf(100.0)
                , BigDecimal.valueOf(103.2));
        var productCreateResponse = productGatewayImpl.create(product);

        // AND a request to update
        var productUpdate = getProductMock(
                productCreateResponse.getId().toString()
                , "Seguro de Auto"
                , CategoryType.toEnum("AUTO")
                , BigDecimal.valueOf(50.0)
                , BigDecimal.valueOf(55.25));

        // AND shutdown in memory database
        Connection con = getConnection();
        con.createStatement().execute("DROP ALL OBJECTS DELETE FILES");

        // WHEN execute productRepository
        IntegrationException integrationException = assertThrows(IntegrationException.class, () ->
                productGatewayImpl.update(productUpdate));

        // THEN verify calls
        assertEquals("Erro ao tentar atualizar produto.", integrationException.getMessage());
    }

    private Product getProductMock(String id, String name, CategoryType category, BigDecimal standardPrice, BigDecimal chargePrice) {
        return Product.builder()
                .id(id != null ? UUID.fromString(id) : null)
                .name(name)
                .category(category)
                .standardPrice(standardPrice)
                .chargePrice(chargePrice)
                .build();
    }
}
