package br.com.itau.insurance.entrypoint.product.http;

import br.com.itau.insurance.core.usecase.ProductCreatorUseCase;
import br.com.itau.insurance.core.usecase.ProductUpdaterUseCase;
import br.com.itau.insurance.entrypoint.product.http.converter.ProductRequestDTOToProductConverter;
import br.com.itau.insurance.entrypoint.product.http.converter.ProductResponseToProductResponseDTOConverter;
import br.com.itau.insurance.entrypoint.product.http.dto.request.ProductRequestDTO;
import br.com.itau.insurance.entrypoint.product.http.dto.response.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
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

    // Use cases
    private final ProductCreatorUseCase productCreatorUseCase;
    private final ProductUpdaterUseCase productUpdaterUseCase;

    // Converters
    private final ProductRequestDTOToProductConverter productRequestDTOToProductConverter;
    private final ProductResponseToProductResponseDTOConverter productResponseToProductResponseDTOConverter;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO requestDTO) {

        var request = productRequestDTOToProductConverter.parseObject(requestDTO);
        var response = productCreatorUseCase.execute(request);
        var responseDTO = productResponseToProductResponseDTOConverter.parseObject(response);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> enableOrDisable(@PathVariable String id,
                                                              @RequestBody ProductRequestDTO requestDTO) {

        var request = productRequestDTOToProductConverter.parseObject(id, requestDTO);
        var response = productUpdaterUseCase.execute(request);
        var responseDTO = productResponseToProductResponseDTOConverter.parseObject(response);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
