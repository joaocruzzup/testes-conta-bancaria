package com.factory.contabancaria.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaDTOGet {

    private String numConta;

    private String agencia;

    private String nomeDoUsuario;

    private BigDecimal valorAtualConta;

}
