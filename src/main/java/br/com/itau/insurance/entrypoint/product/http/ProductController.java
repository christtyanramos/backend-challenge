package br.com.itau.insurance.entrypoint.product.http;

import br.com.itau.insurance.exception.response.ErrorResponse;
import br.com.itau.insurance.entrypoint.product.http.dto.request.ProductRequestDTO;
import br.com.itau.insurance.entrypoint.product.http.dto.response.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import static br.com.itau.insurance.entrypoint.product.http.examples.ProductExamples.PRODUCT_REQUEST_EXAMPLE;

@Tag(name = "Products", description = "Desafio Produtos de Seguros")
public interface ProductController {

    @Operation(summary = "Cria o produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payload de criação com sucesso",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(value = PRODUCT_REQUEST_EXAMPLE)
            })
    })
    ResponseEntity<ProductResponseDTO> create(ProductRequestDTO requestDTO);

    @Operation(summary = "Atualiza o produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payload de atualização com sucesso",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(value = PRODUCT_REQUEST_EXAMPLE)
            })
    })
    ResponseEntity<ProductResponseDTO> update(@PathVariable String id, ProductRequestDTO requestDTO);

}
