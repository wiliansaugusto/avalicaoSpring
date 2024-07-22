package br.com.avaliacaosoc.AvaliacoSoc.DTO;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Exame;
import br.com.avaliacaosoc.AvaliacoSoc.DTO.Funcionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FuncionarioExame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    boolean realizado;

    @ManyToOne
    @JoinColumn(name = "codigo")
    Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "cd_exame")
    Exame exame;
}
