package com.factory.contabancaria.controllerTest;

import com.factory.contabancaria.controller.ContasController;
import com.factory.contabancaria.exception.ContaNaoEncontradaException;
import com.factory.contabancaria.model.ContasModel;
import com.factory.contabancaria.model.dto.ContaDTOGet;
import com.factory.contabancaria.model.dto.ContaDTOInformacoes;
import com.factory.contabancaria.model.dto.ContaDTOPostPut;
import com.factory.contabancaria.model.dto.ContaDTOTransacao;
import com.factory.contabancaria.model.factory.ContaFactory;
import com.factory.contabancaria.repository.ContasRepository;
import com.factory.contabancaria.service.ContasService;
import com.factory.contabancaria.utils.mapper.ContaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContasController.class)
public class ContasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContasController contasController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContasService contasService;

    @MockBean
    private ContasRepository contasRepository;

    @Test
    @DisplayName("Buscar todas as contas ")
    public void buscarTodasAsContasTeste() throws Exception {

        List<ContaDTOGet> contasEsperadas = new ArrayList<>();
        ContaDTOGet conta1 = new ContaDTOGet("123456", "123", "Joao", BigDecimal.valueOf(3000));
        ContaDTOGet conta2 = new ContaDTOGet("123456", "123", "Joao", BigDecimal.valueOf(3000));
        contasEsperadas.add(conta1);
        contasEsperadas.add(conta2);

        when(contasService.listarContas()).thenReturn(contasEsperadas);

        String expectedJson = objectMapper.writeValueAsString(contasEsperadas);

        mockMvc.perform(get("/api/contas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(print());
    }

    @Test
    @DisplayName("Buscar conta por ID existente")
    public void buscarContaPorIDExistenteTeste() throws Exception {

        Long idConta = 1L;
        ContaDTOGet contaEsperada = new ContaDTOGet("123456", "123", "Joao", BigDecimal.valueOf(3000));

        when(contasService.exibeContaPorId(idConta)).thenReturn(Optional.of(contaEsperada));

        String expectedJson = objectMapper.writeValueAsString(contaEsperada);

        mockMvc.perform(get("/api/contas/id/{id}", idConta)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(print());
    }

    @Test
    @DisplayName("Buscar conta por ID inexistente")
    public void buscarContaPorIDInexistenteTeste() throws Exception {

        Long idConta = 1L;

        when(contasService.exibeContaPorId(idConta)).thenThrow(new ContaNaoEncontradaException("Conta não encontrada, tente novamente!"));

        mockMvc.perform(get("/api/contas/id/{id}", idConta)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Conta não encontrada, tente novamente!"))
                .andDo(print());
    }

    @Test
    @DisplayName("Buscar conta por nome existente")
    public void buscarContaPorNomeExistenteTeste() throws Exception {

        String nome = "Joao";
        ContaDTOGet contaEsperada = new ContaDTOGet("123456", "123", "Joao", BigDecimal.valueOf(3000));

        when(contasService.exibeContaPorNome(nome)).thenReturn(List.of(contaEsperada));

        String expectedJson = objectMapper.writeValueAsString(List.of(contaEsperada));

        mockMvc.perform(get("/api/contas/{nomeDoUsuario}", nome)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(print());
    }

    @Test
    @DisplayName("Buscar conta por nome inexistente")
    public void buscarContaPorNomeInexistenteTeste() throws Exception {

        String nome = "Joao";
        when(contasService.exibeContaPorNome(nome)).thenThrow(new ContaNaoEncontradaException("Erro: Conta de nome " + nome + " não encontrada"));

        mockMvc.perform(get("/api/contas/{nomeDoUsuario}", nome)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Cadastrar nova conta")
    public void cadastrarContaTeste() throws Exception {
        ContasModel contaEnviada = new ContasModel();
        contaEnviada.setNomeDoUsuario("Joao");
        contaEnviada.setNumConta("123456");
        contaEnviada.setAgencia("123");
        contaEnviada.setValorAtualConta(BigDecimal.valueOf(3000));
        contaEnviada.setValorFornecido(BigDecimal.valueOf(1000));
        contaEnviada.setTipoServico("saque");

        ContaDTOPostPut contaEsperada = new ContaDTOPostPut("123456", BigDecimal.valueOf(3000), BigDecimal.valueOf(1000), "saque");

        when(contasService.cadastrar(Mockito.any(contaEnviada.getClass()), Mockito.any())).thenReturn(contaEsperada);

        mockMvc.perform(post("/api/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contaEnviada)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(contaEsperada)))
                .andDo(print());

        verify(contasService, times(1)).cadastrar(Mockito.any(), Mockito.any());

    }

    @Test
    @DisplayName("Alterar uma conta")
    public void alterarContaTest() throws Exception {

        ContaDTOInformacoes contaEnviada = new ContaDTOInformacoes("123456", "132", "Joao");
        ContaDTOInformacoes contaEsperada = new ContaDTOInformacoes("123456", "132", "Joao");
        Long idConta = 1L;
        when(contasService.alterarInformacoesConta(Mockito.any(), Mockito.any(contaEnviada.getClass()), Mockito.any())).thenReturn(contaEnviada);

        mockMvc.perform(put("/api/contas/{id}", idConta)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contaEnviada)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contaEsperada)))
                .andDo(print());

        verify(contasService, times(1)).alterarInformacoesConta(Mockito.any(), Mockito.any(contaEnviada.getClass()), Mockito.any());
    }

    @Test
    @DisplayName("Realizar uma transação")
    public void RealizarTransacaoNaContaTest() throws Exception {

        ContaDTOTransacao contaEnviada = new ContaDTOTransacao(BigDecimal.valueOf(3000), "saque");
        ContaDTOPostPut contaDTOPostPut = new ContaDTOPostPut("Joao", BigDecimal.valueOf(3000), BigDecimal.valueOf(3000), "saque");

        Long idConta = 1L;

        when(contasService.realizarTransacao(Mockito.any(), Mockito.any(contaEnviada.getClass()), Mockito.any())).thenReturn(contaDTOPostPut);

        mockMvc.perform(put("/api/contas//{id}/transacao", idConta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaEnviada)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contaDTOPostPut)))
                .andDo(print());

        verify(contasService, times(1)).realizarTransacao(Mockito.any(), Mockito.any(contaEnviada.getClass()), Mockito.any());
    }

    @Test
    @DisplayName("Deletar uma conta")
    public void deletarUmaContaTest() throws Exception{

        mockMvc.perform(delete("/api/contas/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(contasService, times(1)).deletarConta(Mockito.any());
    }



}
