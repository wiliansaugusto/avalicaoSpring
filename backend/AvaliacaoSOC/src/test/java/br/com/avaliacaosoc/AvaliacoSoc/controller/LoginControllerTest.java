package br.com.avaliacaosoc.AvaliacoSoc.controller;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Usuario;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BasicException;
import br.com.avaliacaosoc.AvaliacoSoc.repository.LoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginRepository loginRepository;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("loginTests");
        usuario.setSenha("1234Will");
    }

    @Test
    void getUsuario() {
        when(loginRepository.findByLoginAndSenha(anyString(), anyString())).thenReturn(Optional.of(usuario));

        ResponseEntity<?> responseEntity = loginController.getUsuario(usuario);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Optional.of(usuario), responseEntity.getBody());
        verify(loginRepository, times(1)).findByLoginAndSenha(anyString(), anyString());

        when(loginRepository.findByLoginAndSenha(anyString(), anyString())).thenReturn(Optional.ofNullable(null));
        assertThrows(BasicException.class, () -> {
            loginController.getUsuario(usuario);
        });

    }


    @Test
    void novoUsuario() {
        when(loginRepository.findByLogin(anyString())).thenReturn(Collections.singletonList(usuario));

        ResponseEntity<?> responseEntity = loginController.novoUsuario(usuario);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Usuário já cadastrado", responseEntity.getBody());
        verify(loginRepository, times(1)).findByLogin(anyString());


        when(loginRepository.findByLogin(anyString())).thenReturn(Collections.emptyList());
        when(loginRepository.save(any())).thenReturn(usuario);

        ResponseEntity<?> responseEntitySuccess = loginController.novoUsuario(usuario);

        assertEquals(HttpStatus.CREATED, responseEntitySuccess.getStatusCode());
        assertEquals(usuario, responseEntitySuccess.getBody());
        verify(loginRepository, times(1)).save(any());


        usuario.setSenha("abc");
        assertThrows(BasicException.class, () -> {
            loginController.novoUsuario(usuario);
        });


    }


    @Test
    void alterarUsuario() {

        when(loginRepository.existsById(anyLong())).thenReturn(true);
        when(loginRepository.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> responseEntity = loginController.alterarUsuario(usuario);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(usuario, responseEntity.getBody());
        verify(loginRepository, times(1)).existsById(anyLong());
        verify(loginRepository, times(1)).save(any(Usuario.class));

        usuario.setSenha("abc");
        assertThrows(BasicException.class, () -> {
            loginController.alterarUsuario(usuario);
        });


        assertThrows(BasicException.class, () -> {
            when(loginRepository.existsById(anyLong())).thenReturn(false);
            loginController.alterarUsuario(usuario);
        });



    }

    @Test
    void deletearUsuario() {

        when(loginRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        doNothing().when(loginRepository).deleteById(anyLong());

        ResponseEntity<?> responseEntity = loginController.deletearUsuario(1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(loginRepository, times(1)).findById(anyLong());
        verify(loginRepository, times(1)).deleteById(anyLong());

        when(loginRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BasicException.class, () -> {
            loginController.deletearUsuario(1L);
        });

        assertThrows(BasicException.class, () -> {

            loginController.deletearUsuario(1L);
        });

        when(loginRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        doThrow(new BasicException("erro")).when(loginRepository).deleteById(any());
        assertThrows(BasicException.class, () -> {
            loginController.deletearUsuario(usuario.getId());
        });
    }

    @Test
    void alterarUsuarioComErro(){

        assertThrows(BasicException.class, ()->{
            when(loginRepository.existsById(anyLong())).thenReturn(true);

            when(loginRepository.save(any(Usuario.class))).thenThrow(BasicException.class);

            loginController.alterarUsuario(usuario);

        });
    }

}
