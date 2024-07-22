package br.com.avaliacaosoc.AvaliacoSoc.controller;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Funcionario;
import br.com.avaliacaosoc.AvaliacoSoc.DTO.Usuario;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BasicException;
import br.com.avaliacaosoc.AvaliacoSoc.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioControllerTest {

    @InjectMocks
    FuncionarioController funcionarioController;

    @Mock
    LoginController loginController;

    @Mock
    FuncionarioRepository funcionarioRepository;

    Funcionario funcionario = new Funcionario();

    Usuario usuario = new Usuario();

    @BeforeEach
    void setup() {

        usuario.setId(1l);
        usuario.setLogin("loginTests");
        usuario.setSenha("1234Will");

        funcionario.setCodigo(1l);
        funcionario.setNome("Willians Teste");
        funcionario.setUsuario(usuario);
    }

    @Test
    void funcionarioNovo() {

        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);
        when(loginController.novoUsuario(funcionario.getUsuario())).thenReturn(new ResponseEntity(HttpStatus.OK));

        ResponseEntity<Funcionario> responseEntity = funcionarioController.funcionarioNovo(funcionario);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(funcionario, responseEntity.getBody());

        verify(loginController, times(1)).novoUsuario(any(Usuario.class));
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));

        when(loginController.novoUsuario(any(Usuario.class))).thenReturn(new ResponseEntity<>("Usuário já cadastrado", HttpStatus.NOT_ACCEPTABLE));

        assertThrows(BasicException.class, () -> funcionarioController.funcionarioNovo(funcionario));

    }

    @Test
    void listarTodosFuncionario() {
        List<Funcionario> funcionarioList = Collections.singletonList(funcionario);
        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        ResponseEntity<Funcionario> responseEntity = funcionarioController.listarTodosFuncionario();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(funcionarioList, responseEntity.getBody());
        verify(funcionarioRepository, times(1)).findAll();

        when(funcionarioRepository.findAll()).thenThrow(new BasicException("erro"));
        assertThrows(BasicException.class, () -> {
            funcionarioController.listarTodosFuncionario();
        });
    }

    @Test
    void listarFuncionarioPorCodigo() {
        when(funcionarioRepository.findById(anyLong())).thenReturn(Optional.of(funcionario));
        ResponseEntity<Funcionario> responseEntity = funcionarioController.listarFuncionarioPorCodigo(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Optional.of(funcionario), responseEntity.getBody());
        verify(funcionarioRepository, times(1)).findById(anyLong());

        when(funcionarioRepository.findById(anyLong())).thenThrow(new BasicException("erro"));
        assertThrows(BasicException.class, () -> {
            funcionarioController.listarFuncionarioPorCodigo(1l);
        });

    }

    @Test
    void listarFuncionarioNome() {
        List<Funcionario> mockFuncionarios = Collections.singletonList(funcionario);
        Funcionario filtro = new Funcionario();
        filtro.setNome("Funcionario");

        when(funcionarioRepository.findByNomeContaining(anyString())).thenReturn(mockFuncionarios);

        ResponseEntity<List<Funcionario>> responseEntity = funcionarioController.listarFuncionarioNome(filtro);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Optional.of(mockFuncionarios), responseEntity.getBody());

        verify(funcionarioRepository, times(1)).findByNomeContaining(anyString());

        when(funcionarioRepository.findByNomeContaining(anyString())).thenThrow(new BasicException("erro"));
        assertThrows(BasicException.class, () -> {
            funcionarioController.listarFuncionarioNome(filtro);
        });

    }

    @Test
    void editarFuncionario() {
        funcionario.setUsuario(null);

        when(funcionarioRepository.findById(anyLong())).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.save(any())).thenReturn(funcionario);
        ResponseEntity<Funcionario> responseEntity = funcionarioController.editarFuncionario(funcionario);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(funcionario, responseEntity.getBody());
        verify(funcionarioRepository, times(1)).findById(anyLong());
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
    }

    @Test
    void deletarFuncionario() {
        when(funcionarioRepository.findById(anyLong())).thenReturn(Optional.of(funcionario));
        ResponseEntity<Funcionario> responseEntity = funcionarioController.deletarFuncionario(funcionario.getCodigo());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(funcionarioRepository, times(1)).findById(anyLong());
        verify(funcionarioRepository, times(1)).deleteById(anyLong());

        doThrow(new BasicException("erro")).when(funcionarioRepository).deleteById(any());
        assertThrows(BasicException.class, () -> {
            funcionarioController.deletarFuncionario(1l);
        });
    }
}