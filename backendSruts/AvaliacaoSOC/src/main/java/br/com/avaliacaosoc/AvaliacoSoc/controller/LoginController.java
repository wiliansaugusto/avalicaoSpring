package br.com.avaliacaosoc.AvaliacoSoc.controller;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Usuario;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BasicException;
import br.com.avaliacaosoc.AvaliacoSoc.repository.LoginRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
    @Autowired
    private LoginRepository loginRepository;

    private static final Logger log =  LogManager.getLogger(LoginController.class);


    @PostMapping("usuario")
    public ResponseEntity getUsuario(@RequestBody  Usuario usuario){
        log.info("pesquisando usuario: "+usuario.getLogin()+ " e senha: "+usuario.getSenha());
        Optional<Usuario> resposta = Optional.of(loginRepository.findByLoginAndSenha(usuario.getLogin(), usuario.getSenha())
                .orElseThrow(() -> new BasicException("Usuario Não encontrado")));
        log.info("Usario logado com sucesso: {}",resposta.get().getLogin() );
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
    @PostMapping("usuario-novo")
    public ResponseEntity novoUsuario(@RequestBody Usuario usuario){

        if (!validarSenha(usuario.getSenha())){
            throw new BasicException("Senha não permitida");
        }

        List<Usuario> usuarioList = loginRepository.findByLogin(usuario.getLogin());
        if (usuarioList.size() == 0){
            Usuario novoUsuario = loginRepository.save(usuario);
            log.info("Usuario salvo com sucesso\n id:{}",novoUsuario.getId());
            return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Usuário já cadastrado", HttpStatus.NOT_ACCEPTABLE) ;
    }

    @PutMapping("alterar-usuario")
    public ResponseEntity alterarUsuario(@RequestBody Usuario usuario){
        if (!loginRepository.existsById(usuario.getId())){
            throw new BasicException("Usuario nao encontrado");
        }
        if (!validarSenha(usuario.getSenha())){
            throw new BasicException("Senha não permitida");
        }
        try{
            usuario.setId(usuario.getId());
            loginRepository.save(usuario);
            return  new ResponseEntity<>(usuario, HttpStatus.OK);
        }catch (BasicException e ){
            throw new BasicException("Erro no banco de dados contate o administrador");
        }

    }

    @DeleteMapping("deletar-usuario/{id}")
    public ResponseEntity deletearUsuario(@PathVariable Long id){
        log.info("Buscando o Usuario com id: {}", id);
        Optional<Usuario> resposta = loginRepository.findById(id);
        if(resposta.isEmpty()){
            log.info("Usuario com id: {} não encontrado", id);
            throw new BasicException("Usuario não encontrado");
        }
        try {
            log.info("Deletando  o Usuario com id: {}", id);
            loginRepository.deleteById(id);
            log.info("Usuario com id: {} deletado com sucesso", id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw  new BasicException("Erro no banco de dados contate o administrador");
        }
    }

    private boolean validarSenha(String senha){
        log.info("Validando Senha");
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(senha);
        if(!matcher.matches()){
            log.info("Senha não segue o padrão");
        }
        return matcher.matches();

    }
}
