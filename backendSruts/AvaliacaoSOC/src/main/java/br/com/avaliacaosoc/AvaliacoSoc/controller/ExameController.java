package br.com.avaliacaosoc.AvaliacoSoc.controller;

import br.com.avaliacaosoc.AvaliacoSoc.DTO.Exame;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BasicException;
import br.com.avaliacaosoc.AvaliacoSoc.exception.BussinessException;
import br.com.avaliacaosoc.AvaliacoSoc.repository.ExameRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ExameController {

    @Autowired
    private ExameRepository exameRepository;

    private static final Logger log = LogManager.getLogger(ExameController.class);

    Pageable pageable = PageRequest.of(0, 24000);

    @GetMapping("listar-todos-exames")
    public ResponseEntity getAllExame() {

        log.info("pesquisando todos os exames: ");
        List<Exame> resposta = exameRepository.findAll(pageable).stream().toList();
        log.info("há no banco: {}", exameRepository.count());
        log.info("Tem {} listados: ", resposta.size());
        if (resposta.isEmpty()) {
            throw new BasicException("Exames não foram encontrado");

        }
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @GetMapping("listar-todos-exames/{ic_ativo}")
    public ResponseEntity getAllExameAtivo(@PathVariable boolean ic_ativo) {

        log.info("pesquisando todos os exames: ");
        List<Exame> resposta = exameRepository.findByIcAtivo(ic_ativo, pageable).stream().toList();
        log.info("Tem {} listados: ", resposta.size());
        if (resposta.isEmpty()) {
            throw new BasicException("Exames não foram encontrado verifique a url");

        }
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @GetMapping("listar-exames-cd/{cd_exame}")
    public ResponseEntity getExameCodigo(@PathVariable Long cd_exame) {

        log.info("pesquisando  o exame: ");
        Exame resposta = exameRepository.findByCdExame(cd_exame);
        log.info(resposta);
        if (isNull(resposta)) {
            throw new BasicException("Exames não foram encontrado verifique o código informado");

        }
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PostMapping("listar-exames-nome")
    public ResponseEntity getExameNome(@RequestBody Exame exame) {

        log.info("pesquisando  o exame: {}", exame.getNmExame());
        List<Exame> resposta = exameRepository.findByNmExameContaining(exame.getNmExame());
        log.info(resposta.size());
        if (resposta.isEmpty()) {
            throw new BasicException("Exames não foram encontrado verifique a url");

        }
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PutMapping("editar-exame")
    public ResponseEntity editarExame(@RequestBody Exame exame) {

        log.info("pesquisando  o exame:\n cd_exame {}\n nm_exame: {}", exame.getCd_exame(), exame.getNmExame());
        if (!exameRepository.existsById(exame.getCd_exame())) {
            log.info(exameRepository.existsById(exame.getCd_exame()));
            throw new BasicException("Exame não encontrado");
        }

        try {
            exameRepository.save(exame);
            log.info("Exame salvo com sucesso");
            return new ResponseEntity<>(exame, HttpStatus.OK);
        } catch (BasicException e) {
            log.error("Problemas de infra contate o administrador", e);
            throw new BasicException("Problemas de infra contate o administrador");
        }

    }

    @PostMapping("novo-exame")
    public ResponseEntity novoExame(@RequestBody Exame exame) {
        if (!isNull(exameRepository.findByNmExame(exame.getNmExame()))) {
            log.info("exame encontrado");
            throw new BasicException("Exame já existente");
        }
        try {
            exameRepository.save(exame);
            log.info("Exame salvo com sucesso");
        } catch (BasicException e) {
            throw new BasicException("Problemas de infra contate o administrador");
        }

        return new ResponseEntity<>(exame, HttpStatus.OK);
    }

    @DeleteMapping("deletar-exame/{cd_exame}")
    public ResponseEntity deletarUsuario(@PathVariable Long cd_exame) {
        Exame exame = exameRepository.findByCdExame(cd_exame);
        if (isNull(exame)) {
            throw new BasicException("Exame não encontrado");
        }
        try {

            log.info("Exame: {} encontrado", exame.getNmExame());
            log.info("tentativa de deletar exame: {}", exame.getNmExame());
            exameRepository.delete(exame);
            log.info("Exame: {} deletado com suceso", exame.getNmExame());

        } catch (BasicException e) {
            throw new BasicException("Problemas para deletar " + e.getMessage());
        }
        return new ResponseEntity<>(exame, HttpStatus.OK);
    }

    @GetMapping("importar-banco-de-dados")
    public void importarBanco() {
        final String DB_URL = "jdbc:mysql://localhost:3306/avaliacao";
        final String DB_USER = "root";
        final String DB_PASSWORD ="";
        final String SQL_FILE_PATH = "C:\\Users\\wilia\\Downloads\\avaliacao1\\";

        Connection connection = null;
        Statement statement = null;
        BufferedReader br = null;
        File folder = new File(SQL_FILE_PATH);
        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("Nenhum arquivo .arquivo encontrado na pasta.");
            return;
        }
        try {
            // Conectar ao banco de dados
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();

            // Processar cada arquivo na pasta
            for (File file : files) {
                System.out.println("Processando arquivo: " + file.getName());
                br = new BufferedReader(new FileReader(file));
                processFile(br, statement);
            }

            System.out.println("Importação dos arquivos concluída com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                if (br != null) br.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void processFile(BufferedReader br, Statement statement) {
        StringBuilder sql = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
                // Executar comandos ao encontrar um ponto e vírgula (;)
                if (line.trim().endsWith(";")) {
                    statement.execute(sql.toString());
                    sql.setLength(0); // Reset the StringBuilder
                }
            }
            // Executar o último comando se não terminar com ;
            if (sql.length() > 0) {
                statement.execute(sql.toString());
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }


}
