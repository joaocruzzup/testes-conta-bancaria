package com.factory.contabancaria.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContaDTOTransacao {
    @Column(nullable = false)
    private BigDecimal ValorFornecido;

    @Column(nullable = false)
    private String tipoServico;
}
