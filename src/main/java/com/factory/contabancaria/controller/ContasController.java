package com.factory.contabancaria.controller;

import com.factory.contabancaria.model.ContasModel;
import com.factory.contabancaria.model.dto.ContaDTOInformacoes;
import com.factory.contabancaria.model.dto.ContaDTOPostPut;
import com.factory.contabancaria.model.dto.ContaDTOTransacao;
import com.factory.contabancaria.model.factory.ContaFactory;
import com.factory.contabancaria.repository.ContasRepository;
import com.factory.contabancaria.service.ContasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contas")
public class ContasController {

    @Autowired
    ContasService contasService;

    @Autowired
    ContasRepository contasRepository;

    //requisições
    //GET - Pegar as informações do nosso banco
    @GetMapping
    public ResponseEntity<List<ContasModel>> listarTodasContas(){
        return ResponseEntity.ok(contasService.listarContas());
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<?> exibeUmaContaPeloId(@PathVariable Long id){
        Optional<ContasModel> contaOpcional = contasService.exibeContaPorId(id);
        if (contaOpcional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada, tente novamente!");
        }
        return ResponseEntity.ok(contaOpcional.get());
    }

    @GetMapping(path = "/{nomeDoUsuario}")
    public ResponseEntity<?> exibeUmaContaPeloNome(@PathVariable String nomeDoUsuario) throws Exception {
        return ResponseEntity.ok(contasService.exibeContaPorNome(nomeDoUsuario));
    }

    //POST - Cria uma nova conta dentro do banco
    @PostMapping
    public ResponseEntity<ContaDTOPostPut> cadastrarConta(@RequestBody ContasModel contasModel, ContaFactory contaFactory){
        ContaDTOPostPut novaConta = contasService.cadastrar(contasModel, contaFactory);
        return new ResponseEntity<>(novaConta, HttpStatus.CREATED);
    }

    //PUT - Alterar uma conta já existente dentro do banco
    @PutMapping(path = "/{id}")
    public ResponseEntity<ContaDTOInformacoes> atualizarConta(@PathVariable Long id, @RequestBody ContaDTOInformacoes contaDTOInformacoes, ContaFactory contaFactory){
        ContaDTOInformacoes contaAtualizada = contasService.alterarInformacoesConta(id, contaDTOInformacoes, contaFactory);
        return ResponseEntity.ok(contaAtualizada);
    }

    @PutMapping(path = "/{id}/transacao")
    public ContaDTOPostPut realizarTransacao(@PathVariable Long id, @RequestBody ContaDTOTransacao contaDTOTransacao, ContaFactory contaFactory){
        return contasService.realizarTransacao(id,contaDTOTransacao, contaFactory);
    }


    //DELETE - Deleta uma conta já existente dentro do banco
    @DeleteMapping(path = "/{id}")
    public void deletarConta(@PathVariable Long id){
        contasService.deletarConta(id);
    }

}
