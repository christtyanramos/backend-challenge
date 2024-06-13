package br.com.itau.insurance.entrypoint.product.http;

import br.com.itau.insurance.core.usecase.ProductCreatorUseCase;
import br.com.itau.insurance.core.usecase.ProductUpdaterUseCase;
import br.com.itau.insurance.core.usecase.model.Product;
import br.com.itau.insurance.core.usecase.model.enums.CategoryType;
import br.com.itau.insurance.entrypoint.product.http.converter.ProductRequestDTOToProductConverter;
import br.com.itau.insurance.entrypoint.product.http.converter.ProductToProductResponseDTOConverter;
import br.com.itau.insurance.entrypoint.product.http.dto.request.ProductRequestDTO;
import br.com.itau.insurance.entrypoint.product.http.dto.response.ProductResponseDTO;
import br.com.itau.insurance.exception.IntegrationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static br.com.itau.insurance.entrypoint.product.http.examples.ProductExamples.PRODUCT_REQUEST_EXAMPLE;
import static br.com.itau.insurance.entrypoint.product.http.examples.ProductExamples.PRODUCT_RESPONSE_EXAMPLE;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductControllerImpl.class)
@AutoConfigureMockMvc
class ProductControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductCreatorUseCase productCreatorUseCase;

    @MockBean
    private ProductUpdaterUseCase productUpdaterUseCase;

    @MockBean
    private ProductRequestDTOToProductConverter productRequestDTOToProductConverter;

    @MockBean
    private ProductToProductResponseDTOConverter productToProductResponseDTOConverter;

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() throws Exception {
        // GIVEN a external request
        var productRequest = PRODUCT_REQUEST_EXAMPLE;

        when(productRequestDTOToProductConverter.parseObject(any(ProductRequestDTO.class))).thenReturn(getProductMock(null));
        when(productCreatorUseCase.execute(any(Product.class))).thenReturn(getProductMock("1b4147af-74fb-40ff-bd5b-039340feef1d"));
        when(productToProductResponseDTOConverter.parseObject(any(Product.class))).thenReturn(getProductResponseDTOMock());

        // WHEN calls post
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))

                // THEN verify assertions
                .andExpect(status().isCreated())
                .andExpect(content().json(PRODUCT_RESPONSE_EXAMPLE));

        // AND verify calls
        verify(productRequestDTOToProductConverter, times(1)).parseObject(any(ProductRequestDTO.class));
        verify(productCreatorUseCase, times(1)).execute(any(Product.class));
        verify(productToProductResponseDTOConverter, times(1)).parseObject(any(Product.class));
    }

    @Test
    @DisplayName("Should create product unsuccessfully when body is missing")
    void shouldCreateProductUnsuccessfullyWhenBodyIsMissing() throws Exception {
        // GIVEN a external request

        // WHEN calls post
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))

                // THEN verify assertions
                .andExpect(status().isBadRequest()).andReturn();

        // AND verify calls
        verify(productRequestDTOToProductConverter, times(0)).parseObject(any(ProductRequestDTO.class));
        verify(productCreatorUseCase, times(0)).execute(any(Product.class));
        verify(productToProductResponseDTOConverter, times(0)).parseObject(any(Product.class));
    }

    @Test
    @DisplayName("Should create product unsuccessfully when JSON is invalid")
    void shouldCreateProductUnsuccessfullyWhenJSONIsInvalid() throws Exception {
        // GIVEN a external request
        var productRequest = "{}";

        // WHEN calls post
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))

                // THEN verify assertions
                .andExpect(status().isBadRequest()).andReturn();

        // AND verify calls
        verify(productRequestDTOToProductConverter, times(0)).parseObject(any(ProductRequestDTO.class));
        verify(productCreatorUseCase, times(0)).execute(any(Product.class));
        verify(productToProductResponseDTOConverter, times(0)).parseObject(any(Product.class));
    }

    @Test
    @DisplayName("Should create product unsuccessfully when throws integration exception")
    void shouldCreateProductUnsuccessfullyWhenThrowsIntegrationException() throws Exception {
        // GIVEN a external request
        var productRequest = PRODUCT_REQUEST_EXAMPLE;

        when(productRequestDTOToProductConverter.parseObject(any(ProductRequestDTO.class))).thenReturn(getProductMock(null));
        doThrow(IntegrationException.class).when(productCreatorUseCase).execute(any(Product.class));

        // WHEN calls post
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))

                // THEN verify assertions
                .andExpect(status().isInternalServerError()).andReturn();

        // AND verify calls
        verify(productRequestDTOToProductConverter, times(1)).parseObject(any(ProductRequestDTO.class));
        verify(productCreatorUseCase, times(1)).execute(any(Product.class));
        verify(productToProductResponseDTOConverter, times(0)).parseObject(any(Product.class));
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProductSuccessfully() throws Exception {
        // GIVEN a external request
        var productRequest = PRODUCT_REQUEST_EXAMPLE;

        when(productRequestDTOToProductConverter.parseObject(any(ProductRequestDTO.class))).thenReturn(getProductMock("1b4147af-74fb-40ff-bd5b-039340feef1d"));
        when(productUpdaterUseCase.execute(any(Product.class))).thenReturn(getProductMock("1b4147af-74fb-40ff-bd5b-039340feef1d"));
        when(productToProductResponseDTOConverter.parseObject(any(Product.class))).thenReturn(getProductResponseDTOMock());

        // WHEN calls post
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1b4147af-74fb-40ff-bd5b-039340feef1d")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))

                // THEN verify assertions
                .andExpect(status().isOk());
//                .andExpect(content().json(PRODUCT_RESPONSE_EXAMPLE)).andReturn();

        // AND verify calls
        verify(productRequestDTOToProductConverter, times(1)).parseObject(anyString(), any(ProductRequestDTO.class));
        verify(productUpdaterUseCase, times(1)).execute(any());
        verify(productToProductResponseDTOConverter, times(1)).parseObject(any());
    }

    @Test
    @DisplayName("Should update product unsuccessfully when body is missing")
    void shouldUpdateProductUnsuccessfullyWhenBodyIsMissing() throws Exception {
        // GIVEN a external request

        // WHEN calls put
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1b4147af-74fb-40ff-bd5b-039340feef1d")
                        .contentType(MediaType.APPLICATION_JSON))

                // THEN verify assertions
                .andExpect(status().isBadRequest()).andReturn();

        // AND verify calls
        verify(productRequestDTOToProductConverter, times(0)).parseObject(any(ProductRequestDTO.class));
        verify(productUpdaterUseCase, times(0)).execute(any(Product.class));
        verify(productToProductResponseDTOConverter, times(0)).parseObject(any(Product.class));
    }

    @Test
    @DisplayName("Should update product unsuccessfully when JSON is invalid")
    void shouldUpdateProductUnsuccessfullyWhenJSONIsInvalid() throws Exception {
        // GIVEN a external request
        var productRequest = "{}";

        // WHEN calls post
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1b4147af-74fb-40ff-bd5b-039340feef1d")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))

                // THEN verify assertions
                .andExpect(status().isBadRequest()).andReturn();

        // AND verify calls
        verify(productRequestDTOToProductConverter, times(0)).parseObject(any(ProductRequestDTO.class));
        verify(productUpdaterUseCase, times(0)).execute(any(Product.class));
        verify(productToProductResponseDTOConverter, times(0)).parseObject(any(Product.class));
    }

    private ProductResponseDTO getProductResponseDTOMock() {
        return ProductResponseDTO.builder()
                .id("1b4147af-74fb-40ff-bd5b-039340feef1d")
                .nome("Seguro de Vida Individual")
                .categoria("VIDA")
                .precoBase(BigDecimal.valueOf(100.0))
                .precoTarifado(BigDecimal.valueOf(103.2))
                .build();
    }

    private Product getProductMock(String id) {
        return Product.builder()
                .id(id != null ? UUID.fromString(id) : null)
                .name("Seguro de Vida Individual")
                .category(CategoryType.LIFE)
                .standardPrice(BigDecimal.valueOf(100.0))
                .chargePrice(BigDecimal.valueOf(103.2))
                .build();
    }
}