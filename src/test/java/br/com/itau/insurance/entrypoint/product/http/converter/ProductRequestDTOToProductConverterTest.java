package br.com.itau.insurance.entrypoint.product.http.converter;

import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.entrypoint.product.http.dto.request.ProductRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ProductRequestDTOToProductConverterTest {

    @InjectMocks
    private ProductRequestDTOToProductConverter converter;

    @Test
    @DisplayName("Should parse successfully")
    void shouldParseSuccessfully() {
        // GIVEN a request to convert
        var requestDTO = getProductRequestDTOMock();

        // WHEN execute converter
        Product product = converter.parseObject(requestDTO);

        // THEN verify assertions
        assertNotNull(product);
        assertEquals(CategoryType.LIFE, product.getCategory());
    }

    @Test
    @DisplayName("Should parse with id successfully")
    void shouldParseWithIdSuccessfully() {
        // GIVEN a request to convert
        var requestDTO = getProductRequestDTOMock();

        // WHEN execute converter
        Product product = converter.parseObject("efa433b3-0d42-488d-ade8-bff27e68f222", requestDTO);

        // THEN verify assertions
        assertNotNull(product);
        assertEquals(CategoryType.LIFE, product.getCategory());
    }

    private ProductRequestDTO getProductRequestDTOMock() {
        return ProductRequestDTO.builder()
                .nome("Seguro de Vida Individual")
                .categoria("VIDA")
                .precoBase(BigDecimal.valueOf(100.0))
                .build();
    }

}
