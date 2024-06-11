package br.com.itau.insurance.entrypoint.product.http.converter;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.entrypoint.product.http.dto.response.ProductResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ProductToProductResponseDTOConverterTest {

    @InjectMocks
    private ProductToProductResponseDTOConverter converter;

    @Test
    @DisplayName("Should parse successfully")
    void shouldParseSuccessfully() {
        // GIVEN a product to convert
        var product = getProductMock();

        // WHEN execute converter
        ProductResponseDTO responseDTO = converter.parseObject(product);

        // THEN verify assertions
        assertNotNull(responseDTO);
        assertEquals(CategoryType.LIFE, product.getCategory());
    }

    private Product getProductMock() {
        return Product.builder()
                .id(UUID.fromString("efa433b3-0d42-488d-ade8-bff27e68f222"))
                .name("Seguro de Vida Individual")
                .category(CategoryType.toEnum("VIDA"))
                .standardPrice(BigDecimal.valueOf(100.0))
                .chargePrice(BigDecimal.valueOf(103.2))
                .build();
    }

}
