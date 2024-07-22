package br.com.avaliacaosoc.AvaliacoSoc.repository;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoginRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByLogin(String login);

    Optional<Usuario> findByLoginAndSenha(String login, String senha);

}
