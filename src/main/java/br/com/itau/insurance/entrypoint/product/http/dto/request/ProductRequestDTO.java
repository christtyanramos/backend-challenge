package br.com.itau.insurance.entrypoint.product.http.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String categoria;

    @NotNull
    @DecimalMin(value = "0.0")
    @JsonProperty("preco_base")
    private BigDecimal precoBase;
}
