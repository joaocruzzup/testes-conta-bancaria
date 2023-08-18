package com.factory.contabancaria.utils.mapper;

import com.factory.contabancaria.model.ContasModel;
import com.factory.contabancaria.model.dto.ContaDTOGet;
import com.factory.contabancaria.model.dto.ContaDTOInformacoes;
import com.factory.contabancaria.model.dto.ContaDTOPostPut;
import com.factory.contabancaria.model.dto.ContaDTOTransacao;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ContaMapper {
    public ContaDTOGet toContaDtoGet (ContasModel contasModel){
        ContaDTOGet contaDTOGet = new ContaDTOGet();
        BeanUtils.copyProperties(contasModel, contaDTOGet);
        return contaDTOGet;
    }

    public ContaDTOPostPut toContaDtoPostPut(ContasModel contasModel){
        ContaDTOPostPut contaDTOPost = new ContaDTOPostPut();
        BeanUtils.copyProperties(contasModel, contaDTOPost);
        return contaDTOPost;
    }

    public ContaDTOTransacao toContaDtoTransacao(ContasModel contasModel){
        ContaDTOTransacao contaDTOTransacao = new ContaDTOTransacao();
        BeanUtils.copyProperties(contasModel, contaDTOTransacao);
        return contaDTOTransacao;
    }

    public ContaDTOInformacoes toContaDtoInformacoes(ContasModel contasModel){
        ContaDTOInformacoes contaDTOInformacoes = new ContaDTOInformacoes();
        BeanUtils.copyProperties(contasModel, contaDTOInformacoes);
        return contaDTOInformacoes;
    }

}
