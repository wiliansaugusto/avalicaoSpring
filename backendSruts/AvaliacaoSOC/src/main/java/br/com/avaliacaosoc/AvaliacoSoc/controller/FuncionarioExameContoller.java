package br.com.avaliacaosoc.AvaliacoSoc.controller;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Exame;
import br.com.avaliacaosoc.AvaliacoSoc.DTO.Funcionario;
import br.com.avaliacaosoc.AvaliacoSoc.DTO.FuncionarioExame;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BasicException;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BussinessException;
import br.com.avaliacaosoc.AvaliacoSoc.repository.ExameRepository;
import br.com.avaliacaosoc.AvaliacoSoc.repository.FuncionarioExameRepository;
import br.com.avaliacaosoc.AvaliacoSoc.repository.FuncionarioRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class FuncionarioExameContoller {

    @Autowired
    FuncionarioExameRepository funcionarioExameRepository;
    @Autowired
    ExameRepository exameRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;
    private static final Logger log = LogManager.getLogger(FuncionarioExameContoller.class);

    @PostMapping("salvar-funcionario-exame")
    public ResponseEntity salvarFuncionarioExame(@RequestBody FuncionarioExame funcionarioExame) {
        log.info("pesquisando o funcionario: {}", funcionarioExame.getFuncionario().getCodigo());
        Optional<Optional<Funcionario>> funcionarPesquisado = Optional.of(
                Optional.of(funcionarioRepository.findById(funcionarioExame.getFuncionario().getCodigo())
                        .orElseThrow(() -> new BasicException("Usuario nao encontrado"))));
        log.info("Pesquisando Exame informado");
        Optional<Exame> examePesquisado = Optional.of(exameRepository.findById(funcionarioExame.getExame().getCd_exame())
                .orElseThrow(()-> new BasicException("Exame não encontrado")));
        log.info("Salvando requisicao de funcionario-Exame");
        FuncionarioExame funcionarioExameSalvo;
        try {
            funcionarioExameSalvo = funcionarioExameRepository.save(funcionarioExame);
        } catch (BasicException e) {
            throw new BasicException("Problemas para salvar, contate o administrador");
        }


        return new ResponseEntity(funcionarioExameSalvo,HttpStatus.CREATED);
    }

    @PutMapping("alterar-status-funcionario-exame")
    public ResponseEntity alterarStatusFuncionarioExame(@RequestBody FuncionarioExame id){

        log.info("pesquisando funcionarioExame: {}", id.getId());
        Optional<FuncionarioExame> funcionarioExame = Optional.of(funcionarioExameRepository.findById(id.getId())
                .orElseThrow(() -> new BasicException("Funcionario exame não encontrado")));
        if (funcionarioExame.get().isRealizado()){
            funcionarioExame.get().setRealizado(false);
        }else{
            funcionarioExame.get().setRealizado(true);
        }
        funcionarioExameRepository.save(funcionarioExame.get());
        return new ResponseEntity<>(funcionarioExame, HttpStatus.ACCEPTED);
    }

    @GetMapping("funcionario-exame/{id}")
    public ResponseEntity getFuncionarioExame(@PathVariable Long id){
        log.info("pesquisando o funcionario com id: {}", id);
        Funcionario funcionario = new Funcionario();
        funcionario.setCodigo(id);
        funcionarioRepository.findById(id).orElseThrow(()-> new BasicException("funcionario nao encontrado"));
        List<FuncionarioExame> funcionarioExame = Optional.of(funcionarioExameRepository.findByFuncionario(funcionario))
                .orElseThrow(()-> new BasicException("Funcionario nao encontrado"));
        List<FuncionarioExame> sortedFuncionarioExame =  funcionarioExame.stream()
            .sorted(Comparator.comparing(FuncionarioExame::isRealizado))
            .collect(Collectors.toList());
        return new ResponseEntity<>(sortedFuncionarioExame, HttpStatus.OK);
    }
}
