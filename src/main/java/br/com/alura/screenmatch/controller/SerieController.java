package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.repositoty.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController // isso estou falando que esse pacote e um controlador
public class SerieController {

    @Autowired
    private SerieRepository repository;

    @GetMapping("/series") // isso estou falando que quando houver uma requisição get isso abaixo vai acontecer e o ("/series") e o da requisação ""
    public List<SerieDTO> obterSeries(){
        return repository.findAll()
                .stream()
                .map(serie -> new SerieDTO(serie.getId(),
                        serie.getTitulo(), serie.getTotalTemporadas(),
                        serie.getAvaliacao(), serie.getAtores(), serie.getGenero(),
                        serie.getPoster(), serie.getSinopse())) // nesse map estou passando que cada serie nova vai ser um serie DTO novo tbm e estou passando os parametros
                .collect(Collectors.toList()); // aqui estou colocando essa coleta em uma lista
    }

}
