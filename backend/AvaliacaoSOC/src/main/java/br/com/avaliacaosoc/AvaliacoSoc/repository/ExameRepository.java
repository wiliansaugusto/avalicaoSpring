package br.com.avaliacaosoc.AvaliacoSoc.repository;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Exame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExameRepository extends JpaRepository<Exame, Long> {
    @Query("SELECT e FROM Exame e WHERE e.ic_ativo = :ic_ativo")
    Page<Exame> findByIcAtivo(boolean ic_ativo, Pageable pageable);
    @Query("SELECT e FROM Exame e WHERE e.cd_exame = :codigo")
    Exame findByCdExame(Long codigo);
    List<Exame> findByNmExameContaining(String nmExame);

    Exame findByNmExame(String nmExame);

}
