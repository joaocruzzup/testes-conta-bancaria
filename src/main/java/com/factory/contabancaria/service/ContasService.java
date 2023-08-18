package com.factory.contabancaria.service;

import com.factory.contabancaria.exception.ContaNaoEncontradaException;
import com.factory.contabancaria.exception.ParametrosNaoPreenchidosException;
import com.factory.contabancaria.model.ContasModel;
import com.factory.contabancaria.model.dto.ContaDTOGet;
import com.factory.contabancaria.model.dto.ContaDTOInformacoes;
import com.factory.contabancaria.model.dto.ContaDTOPostPut;
import com.factory.contabancaria.model.dto.ContaDTOTransacao;
import com.factory.contabancaria.model.factory.ContaFactory;
import com.factory.contabancaria.repository.ContasRepository;
import com.factory.contabancaria.utils.mapper.ContaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContasService {
    @Autowired
    ContasRepository contasRepository;

    @Autowired
    ContaMapper contaMapper;

    @Autowired
    TransacaoService transacaoService;

    //métodos
    public List<ContasModel> listarContas(){
        return contasRepository.findAll();
    }

    public Optional<ContasModel> exibeContaPorId(Long id){
        return contasRepository.findById(id);
    }

    public List<ContaDTOGet> exibeContaPorNome(String nomeDoUsuario) {
        List<ContasModel> listaContasNomes = contasRepository.findByNomeDoUsuario(nomeDoUsuario);
        if (listaContasNomes.size() == 0){
            throw new ContaNaoEncontradaException("Erro: Conta de nome " + nomeDoUsuario + " não encontrada");
        }
        List<ContaDTOGet> listaContasRetorno = new ArrayList<>();
        for (ContasModel conta: listaContasNomes) {
            ContaDTOGet contaDTOGet = contaMapper.toContaDtoGet(conta);
            listaContasRetorno.add(contaDTOGet);
        };
        return listaContasRetorno;
    }

    public ContaDTOPostPut cadastrar(ContasModel contasModel, ContaFactory contaFactory){
        BigDecimal resultado = contaFactory.tipoServicoConta(contasModel.getTipoServico())
                .calcular(contasModel.getValorAtualConta(), contasModel.getValorFornecido());

        transacaoService.validarTransacao(resultado);
        contasModel.setValorAtualConta(resultado);
        contasRepository.save(contasModel);
        ContaDTOPostPut contaDTOPost = contaMapper.toContaDtoPostPut(contasModel);
        return contaDTOPost;
    }

    public ContaDTOInformacoes alterarInformacoesConta(Long id, ContaDTOInformacoes contaDTOInformacoes, ContaFactory contaFactory) {

        Optional<ContasModel> contasModelOptional = exibeContaPorId(id);
        if (!contasModelOptional.isPresent()){
            throw new ContaNaoEncontradaException("Erro: Conta de ID " + id + " não encontrada");
        }
        ContasModel conta = contasModelOptional.get();

        if (contaDTOInformacoes.getNumConta() != null) {
            conta.setNumConta(contaDTOInformacoes.getNumConta());
        }
        if (contaDTOInformacoes.getAgencia() != null) {
            conta.setAgencia(contaDTOInformacoes.getAgencia());
        }
        if (contaDTOInformacoes.getNomeDoUsuario() != null) {
            conta.setNomeDoUsuario(contaDTOInformacoes.getNomeDoUsuario());
        }

        contasRepository.save(conta);

        ContaDTOInformacoes contaDTOInformacoesRetorno = contaMapper.toContaDtoInformacoes(conta);

        return contaDTOInformacoesRetorno;
    }

    public ContaDTOPostPut realizarTransacao(Long id, ContaDTOTransacao contaDTOTransacao, ContaFactory contaFactory){
        Optional<ContasModel> contasModelOptional = contasRepository.findById(id);
        if (!contasModelOptional.isPresent()){
            throw new ContaNaoEncontradaException("Erro: Conta de id " + id + " não foi encontrada");
        }

        ContasModel contasModel = contasModelOptional.get();
        if (contaDTOTransacao.getTipoServico() == null || contaDTOTransacao.getValorFornecido() == null){
            throw new ParametrosNaoPreenchidosException("Erro: Parametros obrigatorios nao foram preenchidos");
        }

        contasModel.setTipoServico(contaDTOTransacao.getTipoServico());
        contasModel.setValorFornecido(contaDTOTransacao.getValorFornecido());

        BigDecimal resultado = contaFactory.tipoServicoConta(contasModel.getTipoServico()).calcular(contasModel.getValorAtualConta(), contasModel.getValorFornecido());
        transacaoService.validarTransacao(resultado);
        contasModel.setValorAtualConta(resultado);

        ContaDTOPostPut contaDtoRetorno = contaMapper.toContaDtoPostPut(contasModel);
        contasRepository.save(contasModel);
        return contaDtoRetorno;
    }

    public void deletarConta(Long id){
        contasRepository.deleteById(id);
    }

}
