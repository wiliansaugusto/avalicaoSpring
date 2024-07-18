package br.com.avaliacaosoc.AvaliacoSoc.repository;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Funcionario;
import br.com.avaliacaosoc.AvaliacoSoc.DTO.FuncionarioExame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioExameRepository extends JpaRepository<FuncionarioExame, Long> {

    List<FuncionarioExame> findByFuncionario(Funcionario funcionario);
}
