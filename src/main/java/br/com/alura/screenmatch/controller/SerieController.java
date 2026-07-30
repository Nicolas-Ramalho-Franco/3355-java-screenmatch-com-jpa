package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController // isso estou falando que esse pacote e um controlador
@RequestMapping("/series") // isso e uma forma de dizer que eles estão vindo de um ponto em comun
public class SerieController {
// o controller so fica responsavel pela a por delegar a função nada mais as requisilçoes ao banco quem faz e o service
    @Autowired
    private SerieService servico;

    @GetMapping// isso estou falando que quando houver uma requisição get isso abaixo vai acontecer e o ("/series") e o da requisação ""
    public List<SerieDTO> obterSeries(){
        return servico.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterSeriesTop5(){
        return servico.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos(){
        return servico.obterLancamentos();
    }

    @GetMapping("/{id}") // isso e um parametro que pode variar
    public SerieDTO obterSeriePorId(@PathVariable long id){
        //como ele não vai devolver uma lista ele vai devolver somente um SerieDTO
        return servico.obterProId(id); // passando o id como parametro
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasAsTemporadas(@PathVariable long id){ // criei um novo DTO para os episodio
        return servico.obterTodasAsTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable long id, @PathVariable long numero){
        return servico.obterTemporadasPorNumero(id,numero);
    }

    @GetMapping("/categoria/{nomeGenero}")
    public List<SerieDTO> obterSeriePorCategoria(@PathVariable String nomeGenero){
        return servico.obterPorCategoria(nomeGenero);
    }
}
