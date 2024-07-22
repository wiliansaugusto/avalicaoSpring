package br.com.avaliacaosoc.AvaliacoSoc.controller;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Exame;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BasicException;
import br.com.avaliacaosoc.AvaliacoSoc.repository.ExameRepository;
import net.bytebuddy.matcher.CollectionElementMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExameControllerTest {

    @InjectMocks
    ExameController exameController;

    @Mock
    ExameRepository exameRepository;


    @Test
    void getExameCodigo() {
        Long cdExame = 1L;
        Exame mockExame = new Exame();
        mockExame.setCd_exame(cdExame);
        when(exameRepository.findByCdExame(anyLong())).thenReturn(mockExame);

        ResponseEntity<?> responseEntity = exameController.getExameCodigo(cdExame);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockExame, responseEntity.getBody());

        verify(exameRepository, times(1)).findByCdExame(anyLong());

        when(exameRepository.findByCdExame(anyLong())).thenReturn(null);

        assertThrows(BasicException.class, () -> {
            exameController.getExameCodigo(cdExame);

        });
        when(exameRepository.findByCdExame(anyLong())).thenThrow(new BasicException("erro"));

        assertThrows(BasicException.class, () -> {
            exameRepository.findByCdExame(2l);
        });
    }


    @Test
    void getExameNome() {
        Exame exame = new Exame();
        exame.setNmExame("teste");
        exame.setCd_exame(1l);
        exame.setDs_detalhe_exame("testeds");
        exame.setDs_detalhe_exame1("testeds1");
        exame.setIc_ativo(true);
        List<Exame> mockExame = Collections.singletonList(exame);
        when(exameRepository.findByNmExameContaining(anyString())).thenReturn(mockExame);

        ResponseEntity<?> responseEntity = exameController.getExameNome(exame);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockExame, responseEntity.getBody());

        verify(exameRepository, times(1)).findByNmExameContaining(anyString());


        when(exameRepository.findByNmExameContaining(anyString())).thenReturn(Collections.emptyList());

        assertThrows(BasicException.class, () -> {
            exameController.getExameNome(exame);

        });
    }

    @Test
    void getAllExame() {
        Exame exame = new Exame();
        exame.setNmExame("teste");
        exame.setCd_exame(1L);
        List<Exame> exameList = Collections.singletonList(exame);

        when(exameRepository.findAll()).thenReturn(exameList);

        ResponseEntity responseEntity = exameController.getAllExame();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(exameList, responseEntity.getBody());

        verify(exameRepository, times(1)).findAll();

        when(exameRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(BasicException.class, () -> exameController.getAllExame());

        when(exameRepository.findAll()).thenThrow(new BasicException("erro"));

        assertThrows(BasicException.class, () -> exameController.getAllExame());

    }

    @Test
    void getAllExameAtivo() {
        Exame exame = new Exame();
        exame.setNmExame("teste");
        exame.setCd_exame(1L);
        exame.setDs_detalhe_exame("testeds");
        exame.setDs_detalhe_exame1("testeds1");
        exame.setIc_ativo(true);
        List<Exame> exameList = Collections.singletonList(exame);
        Page<Exame> examePage = new PageImpl<>(exameList);

        Pageable pageable = PageRequest.of(0, 100);

        when(exameRepository.findByIcAtivo(true, pageable)).thenReturn(examePage);

        ResponseEntity responseEntity = exameController.getAllExameAtivo(true);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(exameList, responseEntity.getBody());

        verify(exameRepository, times(1)).findByIcAtivo(true, pageable);

        examePage = new PageImpl<>(Collections.emptyList());
        when(exameRepository.findByIcAtivo(true, pageable)).thenReturn(examePage);

        assertThrows(BasicException.class, () -> exameController.getAllExameAtivo(true));

        when(exameRepository.findByIcAtivo(true, pageable)).thenThrow(new BasicException("erro"));

        assertThrows(BasicException.class, () -> exameController.getAllExameAtivo(true));

    }

    @Test
    void editarExame() {
        Exame exame = new Exame();
        exame.setNmExame("teste");
        exame.setCd_exame(1L);
        exame.setDs_detalhe_exame("testeds");
        exame.setDs_detalhe_exame1("testeds1");
        exame.setIc_ativo(true);
        when(exameRepository.existsById(any())).thenReturn(true);
        ResponseEntity responseEntity = exameController.editarExame(exame);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(exame, responseEntity.getBody());

        when(exameRepository.existsById(any())).thenReturn(false);
        assertThrows(BasicException.class, () -> {
            exameController.editarExame(exame);
            verify(exameController, times(1)).editarExame(exame);

        });

        when(exameRepository.existsById(any())).thenReturn(true);
        when(exameRepository.save(any())).thenThrow(new BasicException("erro"));
        assertThrows(BasicException.class, () -> {
            exameController.editarExame(exame);
        });

    }

    @Test
    void novoExame() {
        Exame exame = new Exame();
        exame.setNmExame("teste");
        exame.setCd_exame(1L);
        exame.setDs_detalhe_exame("testeds");
        exame.setDs_detalhe_exame1("testeds1");
        exame.setIc_ativo(true);


        ResponseEntity responseEntity = exameController.novoExame(exame);
        Exame exameBody = (Exame) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(exame, responseEntity.getBody());
        assertEquals(exame.getDs_detalhe_exame(),exameBody.getDs_detalhe_exame());
        assertEquals(exame.getDs_detalhe_exame1(),exameBody.getDs_detalhe_exame1());
        assertEquals(exame.getIc_ativo(),exameBody.getIc_ativo());

        when(exameRepository.findByNmExame(anyString())).thenReturn(exame);
        assertThrows(BasicException.class, () -> {
            exameController.novoExame(exame);
        });

        when(exameRepository.findByNmExame(anyString())).thenReturn(null);
        when(exameRepository.save(exame)).thenThrow(new BasicException("erro"));
        assertThrows(BasicException.class, () -> {
            exameController.novoExame(exame);
        });

    }

    @Test
    void deletarUsuario() {
        Exame exame = new Exame();
        exame.setNmExame("teste");
        exame.setCd_exame(1L);
        exame.setDs_detalhe_exame("testeds");
        exame.setDs_detalhe_exame1("testeds1");
        exame.setIc_ativo(true);

        when(exameRepository.findByCdExame(anyLong())).thenReturn(exame);
        ResponseEntity responseEntity = exameController.deletarUsuario(exame.getCd_exame());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        when(exameRepository.findByCdExame(anyLong())).thenReturn(null);
        assertThrows(BasicException.class,()->{
            exameController.deletarUsuario(exame.getCd_exame());
        });

        when(exameRepository.findByCdExame(anyLong())).thenReturn(exame);
        doThrow(new BasicException("erro")).when(exameRepository).delete(any(Exame.class));
        assertThrows(BasicException.class,()->{
            exameController.deletarUsuario(exame.getCd_exame());
        });
    }
}