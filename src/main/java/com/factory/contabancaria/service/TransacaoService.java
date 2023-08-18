package com.factory.contabancaria.service;

import com.factory.contabancaria.exception.TransacaoInvalidaException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransacaoService {
    public void validarTransacao(BigDecimal resultado){
        if (resultado.compareTo(BigDecimal.ZERO) < 0){
            throw new TransacaoInvalidaException("Erro: Saldo insuficiente para essa transação");
        }
    }
}
