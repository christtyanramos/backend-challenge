package br.com.itau.insurance.entrypoint.product.http;

import br.com.itau.insurance.entrypoint.product.http.dto.request.ProductRequestDTO;
import br.com.itau.insurance.entrypoint.product.http.dto.response.ProductResponseDTO;
import org.springframework.http.ResponseEntity;

//TODO Swagger Documentation
public interface ProductController {

    ResponseEntity<ProductResponseDTO> create(ProductRequestDTO requestDTO);
}
