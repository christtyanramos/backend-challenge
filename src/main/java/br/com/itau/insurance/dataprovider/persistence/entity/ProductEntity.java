package br.com.itau.insurance.dataprovider.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "TB_PRODUCT")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CD_PRODUCT", nullable = false, unique = true)
    private UUID id;

    @Column(name = "DS_NAME")
    private String name;

    @Column(name = "TP_CATEGORY", nullable = false)
    private Integer category;

    @Column(name = "DS_STANDARD_PRICE", nullable = false)
    private BigDecimal standardPrice;

    @Column(name = "DS_CHARGE_PRICE", nullable = false)
    private BigDecimal chargePrice;
}
