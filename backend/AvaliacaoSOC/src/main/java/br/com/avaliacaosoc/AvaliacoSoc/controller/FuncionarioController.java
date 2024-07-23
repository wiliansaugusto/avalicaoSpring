package br.com.avaliacaosoc.AvaliacoSoc.controller;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Funcionario;
import br.com.avaliacaosoc.AvaliacoSoc.DTO.Usuario;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BasicException;
import br.com.avaliacaosoc.AvaliacoSoc.repository.ExameRepository;
import br.com.avaliacaosoc.AvaliacoSoc.repository.FuncionarioRepository;
import br.com.avaliacaosoc.AvaliacoSoc.repository.LoginRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private LoginController loginController;
    private static final Logger log = LogManager.getLogger(FuncionarioController.class);

    @PostMapping("funcionario-novo")
    public ResponseEntity funcionarioNovo(@RequestBody Funcionario funcionario){
        log.info("Salvando o cadastro de funcionario");
        ResponseEntity usuario = loginController.novoUsuario(funcionario.getUsuario());
        if(usuario.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)){
            log.info("Usuario ja encontrado");
            throw new BasicException("Login ja cadastrado");
        }

        funcionario =  funcionarioRepository.save(funcionario);
        return new ResponseEntity<>(funcionario,HttpStatus.CREATED);
    }

    @GetMapping("listar-todos-funcionario")
    public ResponseEntity listarTodosFuncionario(){
       List<Funcionario> funcionario = null;
        try{
            log.info("pesquisando todos os funcionarios");
           funcionario = funcionarioRepository.findAll();
            log.info("foram encontrados: {} registros", funcionario.size());
        }catch(BasicException e){
            throw new BasicException("Problemas com a aplicação consulte o administrador"+e.getMessage());
        }

        return new ResponseEntity<>(funcionario, HttpStatus.OK);
    }

    @GetMapping("listar-funcionario-codigo/{codigo}")
    public ResponseEntity listarFuncionarioPorCodigo(@PathVariable Long codigo){
        log.info("Pesquisando o funcionario do codigo {}", codigo);
        Optional<Funcionario> funcionario = Optional.of(funcionarioRepository.findById(codigo)
                .orElseThrow(()->new BasicException("Usuario nao encontrado para o codigo: "+codigo)));
        log.info("Funcionario encontrado: {}", funcionario.get().getNome());
        return new ResponseEntity<>(funcionario, HttpStatus.OK);
    }

    @PostMapping("listar-funcionario-nome")
    public ResponseEntity listarFuncionarioNome(@RequestBody Funcionario funcionario){
        log.info("pesquisando todos os funcionarios que contem no nome: {}", funcionario.getNome());

        Optional< List<Funcionario>> funcionarioPesquisado =
                Optional.of(Optional.of(funcionarioRepository.findByNomeContaining(funcionario.getNome())).orElseThrow(() -> new BasicException("Funcionario nao encontrado para o nome: " + funcionario.getNome())));
        log.info("Foram encontrados total: {}",funcionarioPesquisado.get().size());

        return new ResponseEntity<>(funcionarioPesquisado, HttpStatus.OK);
    }

    @PutMapping("editar-funcionario")
    public ResponseEntity editarFuncionario(@RequestBody Funcionario funcionario){
        log.info("pesquisando funcionarios por codigo");
        Optional<Funcionario> funcionarioEditar = Optional.of(funcionarioRepository.findById(funcionario.getCodigo())
                .orElseThrow(()->new BasicException("Usuario nao encontrado para o codigo: "+ funcionario.getCodigo())));
        log.info("funcionario encontrado: {}", funcionarioEditar.get().getNome());
        log.info("Editando funcionarios");
        if (isNull(funcionario.getUsuario())){
            funcionario.setUsuario(funcionarioEditar.get().getUsuario());
        }
        Funcionario funcionarioEditarNovo = Optional.of(funcionarioRepository.save(funcionario)).orElseThrow(() -> new BasicException("Problemas com a aplicação consulte o administrador"));
        log.info("Funcionario Editado com sucesso");
        return new ResponseEntity(funcionarioEditarNovo,HttpStatus.OK);
    }

    @DeleteMapping("deletar-funcionario/{codigo}")
    public ResponseEntity deletarFuncionario(@PathVariable Long codigo){
        log.info("pesquisando funcionarios por codigo");
        Optional<Funcionario> funcionarioEditar = Optional.of(funcionarioRepository.findById(codigo).orElseThrow(() -> new BasicException("Usuario nao encontrado para o codigo: " + codigo)));
        log.info("funcionario encontrado: {}", funcionarioEditar.get().getNome());
        log.info("deletando funcionario");
        try{
            funcionarioRepository.deleteById(codigo);
        }catch (BasicException e){
            throw new BasicException("Problemas com a aplicação consulte o administrador"+e.getMessage());
        }
        log.info("Deletado com sucesso id: {}",codigo);
        return new ResponseEntity<>(funcionarioEditar,HttpStatus.OK);
    }
}
