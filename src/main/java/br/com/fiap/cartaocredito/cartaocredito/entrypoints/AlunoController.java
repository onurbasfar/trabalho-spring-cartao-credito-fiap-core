package br.com.fiap.cartaocredito.cartaocredito.entrypoints;

import br.com.fiap.cartaocredito.cartaocredito.domain.Aluno;
import br.com.fiap.cartaocredito.cartaocredito.domain.AlunoRepository;
import br.com.fiap.cartaocredito.cartaocredito.domain.AlunoService;
import br.com.fiap.cartaocredito.cartaocredito.domain.ImportacaoArquivoAlunosService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    private final ImportacaoArquivoAlunosService importacaoArquivoAlunosService;
    private final AlunoService alunoService;

    public AlunoController(ImportacaoArquivoAlunosService importacaoArquivoAlunosService, AlunoService alunoService) {
        this.importacaoArquivoAlunosService = importacaoArquivoAlunosService;
        this.alunoService = alunoService;
    }

    @GetMapping("{rm}")
    public AlunoDto obtemAlunoPorRm(@PathVariable Long rm){
        try{
            Aluno aluno = alunoService.buscaAlunoPorRm(rm);
            return new AlunoDto(aluno.getRm(), aluno.getNome(),aluno.getNumeroCartao(), aluno.getDigitoVerificadorCartao());
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    //Contrato consiste em enviar o arquivo no formato especifico
    @PostMapping
    public void importaAlunos(@RequestParam("file") MultipartFile file) {

        try {
            String conteudoArquivo = new String(file.getBytes(), StandardCharsets.UTF_8);
            importacaoArquivoAlunosService.importaAlunos(conteudoArquivo);
        } catch (IOException e) {
            //Todo - Tratar melhor os retornos.
            e.printStackTrace();
        }
    }
}