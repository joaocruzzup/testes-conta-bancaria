package com.factory.contabancaria.serviceTest;


import com.factory.contabancaria.model.ContasModel;
import com.factory.contabancaria.model.dto.ContaDTOGet;
import com.factory.contabancaria.repository.ContasRepository;
import com.factory.contabancaria.service.ContasService;
import com.factory.contabancaria.utils.mapper.ContaMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContasServiceTest {

    @Mock
    private ContasRepository contasRepository;

    @Mock
    private ContaMapper contaMapper;

    @InjectMocks
    private ContasService contasService;

    @Test
    @DisplayName("listar contas")
    public void listarContasTeste(){
        ContasModel conta1 = new ContasModel();
        ContasModel conta2 = new ContasModel();
        List<ContasModel> listaContasModel = Arrays.asList(conta1, conta2);

        when(contasRepository.findAll()).thenReturn(listaContasModel);

        List<ContaDTOGet> listaContasDTO = contasService.listarContas();

        Assertions.assertEquals(2, listaContasDTO.size());

        verify(contasRepository, times(1)).findAll();

    }


}
