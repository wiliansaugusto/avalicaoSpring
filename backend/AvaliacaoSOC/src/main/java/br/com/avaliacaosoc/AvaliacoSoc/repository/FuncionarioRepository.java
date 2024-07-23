package br.com.avaliacaosoc.AvaliacoSoc.repository;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByNomeContaining(String nome);
}
