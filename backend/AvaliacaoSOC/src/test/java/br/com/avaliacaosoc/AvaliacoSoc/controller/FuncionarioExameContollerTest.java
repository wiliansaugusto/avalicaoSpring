package br.com.avaliacaosoc.AvaliacoSoc.controller;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Exame;
import br.com.avaliacaosoc.AvaliacoSoc.DTO.Funcionario;
import br.com.avaliacaosoc.AvaliacoSoc.DTO.FuncionarioExame;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BasicException;
import br.com.avaliacaosoc.AvaliacoSoc.repository.ExameRepository;
import br.com.avaliacaosoc.AvaliacoSoc.repository.FuncionarioExameRepository;
import br.com.avaliacaosoc.AvaliacoSoc.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioExameContollerTest {

    @InjectMocks
    FuncionarioExameContoller funcionarioExameContoller;

    @Mock
    FuncionarioRepository funcionarioRepository;

    @Mock
    ExameRepository exameRepository;
    @Mock
    FuncionarioExameRepository repository;

    FuncionarioExame funcionarioExame = new FuncionarioExame();
    Exame exame = new Exame();
    Funcionario funcionario = new Funcionario();

    @BeforeEach()
    void setup() {
        exame.setNmExame("exameTeste");
        exame.setCd_exame(123L);
        exame.setIc_ativo(true);

        funcionario.setCodigo(1L);
        funcionario.setNome("Willians Teste");
        funcionario.setUsuario(null);

        funcionarioExame.setRealizado(true);
        funcionarioExame.setExame(exame);
        funcionarioExame.setFuncionario(funcionario);
    }

    @Test
    void salvarFuncionarioExame() {
        when(funcionarioRepository.findById(anyLong())).thenReturn(Optional.of(funcionario));
        when(exameRepository.findById(anyLong())).thenReturn(Optional.of(exame));
        when(repository.save(any(FuncionarioExame.class))).thenReturn(funcionarioExame);

        ResponseEntity response = funcionarioExameContoller.salvarFuncionarioExame(funcionarioExame);
        assertEquals(funcionarioExame, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(funcionarioRepository, times(1)).findById(anyLong());
        verify(exameRepository, times(1)).findById(anyLong());

        assertThrows(BasicException.class, () -> {
            when(funcionarioRepository.findById(anyLong())).thenThrow(new BasicException("erro"));
            funcionarioExameContoller.salvarFuncionarioExame(funcionarioExame);
        });


        assertThrows(BasicException.class, () -> {
            when(funcionarioRepository.findById(anyLong())).thenReturn(Optional.ofNullable(funcionario));
            when(exameRepository.findById(anyLong())).thenThrow(new BasicException("erro"));
            funcionarioExameContoller.salvarFuncionarioExame(funcionarioExame);
        });

    }

    @Test
    void tentarSalvarExameFuncionario(){

        assertThrows(BasicException.class, () -> {
            when(funcionarioRepository.findById(anyLong())).thenReturn(Optional.of(funcionario));
            when(exameRepository.findById(anyLong())).thenReturn(Optional.of(exame));
            doThrow(new BasicException("erro")).when(repository).save(any());
            funcionarioExameContoller.salvarFuncionarioExame(funcionarioExame);
        });
    }

    @Test
    void alterarStatusFuncionarioExame(){
        when(repository.findById(any())).thenReturn(Optional.of(funcionarioExame));
        when(repository.save(any())).thenReturn(funcionarioExame);

        ResponseEntity response = funcionarioExameContoller.alterarStatusFuncionarioExame(funcionarioExame);
        assertEquals(Optional.of(funcionarioExame), response.getBody());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(repository,times(1)).save(any(FuncionarioExame.class));

        funcionarioExame.setRealizado(false);
        ResponseEntity responseFalse = funcionarioExameContoller.alterarStatusFuncionarioExame(funcionarioExame);
        assertEquals(HttpStatus.ACCEPTED, responseFalse.getStatusCode());

    }

    @Test
    void getFuncionarioExame(){
        when(funcionarioRepository.findById(any())).thenReturn(Optional.ofNullable(funcionario));
        when(repository.findByFuncionario(any())).thenReturn((Collections.singletonList(funcionarioExame)));
        ResponseEntity responseEntity = funcionarioExameContoller.getFuncionarioExame(1L);
        assertEquals(Collections.singletonList(funcionarioExame), responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}