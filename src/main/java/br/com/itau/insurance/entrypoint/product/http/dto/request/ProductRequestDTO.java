package br.com.itau.insurance.entrypoint.product.http.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String nome;

    private String categoria;

    @JsonProperty("preco_base")
    private BigDecimal precoBase;
}
