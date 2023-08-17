package com.factory.contabancaria.service;

import com.factory.contabancaria.model.ContasModel;
import com.factory.contabancaria.model.dto.ContaDTOGet;
import com.factory.contabancaria.model.dto.ContaDTOPostPut;
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

    //métodos
    public List<ContasModel> listarContas(){
        return contasRepository.findAll();
    }

    public Optional<ContasModel> exibeContaPorId(Long id){
        return contasRepository.findById(id);
    }

    public List<ContaDTOGet> exibeContaPorNome(String nomeDoUsuario) throws Exception {
        List<ContasModel> listaContasNomes = contasRepository.findByNomeDoUsuario(nomeDoUsuario);
        if (listaContasNomes.size() == 0){
            throw new Exception("Conta de nome " + nomeDoUsuario + " não encontrada");
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

        contasModel.setValorAtualConta(resultado);
        contasRepository.save(contasModel);
        ContaDTOPostPut contaDTOPost = contaMapper.toContaDtoPostPut(contasModel);
        return contaDTOPost;
    }

    public ContaDTOPostPut alterar(Long id, ContasModel contasModel, ContaFactory contaFactory) {

        ContasModel conta = exibeContaPorId(id).get();

        if (contasModel.getNumConta() != null) {
            conta.setNumConta(contasModel.getNumConta());
        }
        if (contasModel.getAgencia() != null) {
            conta.setAgencia(contasModel.getAgencia());
        }
        if (contasModel.getValorFornecido() != null) {
            conta.setValorFornecido(contasModel.getValorFornecido());
        }
        if (contasModel.getTipoServico() != null && contasModel.getValorFornecido() != null){
            conta.setTipoServico(contasModel.getTipoServico());
            BigDecimal resultado = contaFactory.tipoServicoConta(conta.getTipoServico()).calcular(conta.getValorAtualConta(), conta.getValorFornecido());
            conta.setValorAtualConta(resultado);
        }
        contasRepository.save(conta);
        ContaDTOPostPut contaDTOPut = contaMapper.toContaDtoPostPut(conta);

        return contaDTOPut;
    }

    public void deletarConta(Long id){
        contasRepository.deleteById(id);
    }

}
