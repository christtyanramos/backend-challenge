package br.com.itau.insurance.entrypoint.product.http;

import br.com.itau.insurance.core.usecase.ProductCreatorUseCase;
import br.com.itau.insurance.core.usecase.ProductUpdaterUseCase;
import br.com.itau.insurance.entrypoint.product.http.converter.ProductRequestDTOToProductConverter;
import br.com.itau.insurance.entrypoint.product.http.converter.ProductToProductResponseDTOConverter;
import br.com.itau.insurance.entrypoint.product.http.dto.request.ProductRequestDTO;
import br.com.itau.insurance.entrypoint.product.http.dto.response.ProductResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/products")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    Logger logger = LoggerFactory.getLogger(ProductControllerImpl.class);

    // Use cases
    private final ProductCreatorUseCase productCreatorUseCase;
    private final ProductUpdaterUseCase productUpdaterUseCase;

    // Converters
    private final ProductRequestDTOToProductConverter productRequestDTOToProductConverter;
    private final ProductToProductResponseDTOConverter productToProductResponseDTOConverter;

    @Override
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO requestDTO) {
        logger.info("Iniciando requisição de criação de produto. " + requestDTO);

        var request = productRequestDTOToProductConverter.parseObject(requestDTO);
        var response = productCreatorUseCase.execute(request);
        var responseDTO = productToProductResponseDTOConverter.parseObject(response);

        logger.info("Finalizando requisição de criação de produto. " + responseDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Override
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable String id,
                                                     @Valid @RequestBody ProductRequestDTO requestDTO) {
        logger.info("Iniciando requisição de atualização de produto. Id:" + id + " " + requestDTO);

        var request = productRequestDTOToProductConverter.parseObject(id, requestDTO);
        var response = productUpdaterUseCase.execute(request);
        var responseDTO = productToProductResponseDTOConverter.parseObject(response);

        logger.info("Finalizando requisição de atualização de produto. ", responseDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
