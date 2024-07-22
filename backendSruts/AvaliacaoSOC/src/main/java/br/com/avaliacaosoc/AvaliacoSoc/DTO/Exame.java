package br.com.avaliacaosoc.AvaliacoSoc.DTO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Exame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_exame")
    Long cd_exame;

    @Column(name = "nm_exame")
    private String nmExame;

    Boolean ic_ativo;
    String ds_detalhe_exame;
    String ds_detalhe_exame1;



}
