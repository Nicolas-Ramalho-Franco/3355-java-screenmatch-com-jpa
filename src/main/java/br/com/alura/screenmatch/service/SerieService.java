package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repositoty.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service// isso estou dizendo que vai ser uma classe service e vai ficar responsavel pela a Serie(Logica de negocio)
public class SerieService {
    public List<SerieDTO> obterLancamentos;
    // O service que vai ser responsavel por procurar no banco de dados tirando essa função do controller, ele que vai ficar responsavel pela a regra de negocio
    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterTodasAsSeries(){
        return converteDados(repository.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.lancamentosMaisRecentes());
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(long id, long numero) {
        return repository.obterEpisodiosPortemporada(id, numero)
                .stream()
                .map(episodio -> new EpisodioDTO(episodio.getTemporada(), episodio.getNumeroEpisodio(), episodio.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<EpisodioDTO> obterTodasAsTemporadas(long id) {
        Optional<Serie> serie = repository.findById(id);
        if  (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(episodio -> new EpisodioDTO(episodio.getTemporada(), episodio.getNumeroEpisodio(), episodio.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public SerieDTO obterProId(long id) {// quando ele precisa de uma variavel tem que ser no optional
        Optional<Serie> serie = repository.findById(id);
        if  (serie.isPresent()) {
            Serie s = serie.get();
            return new  SerieDTO(s.getId(),
                    s.getTitulo(), s.getTotalTemporadas(),
                    s.getAvaliacao(), s.getAtores(), s.getGenero(),
                    s.getPoster(), s.getSinopse());
        }
        return null;
    }
    
    private List<SerieDTO> converteDados(List<Serie> series){
        return series.stream()
                .map(serie -> new SerieDTO(serie.getId(),
                        serie.getTitulo(), serie.getTotalTemporadas(),
                        serie.getAvaliacao(), serie.getAtores(), serie.getGenero(),
                        serie.getPoster(), serie.getSinopse()))// nesse map estou passando que cada serie nova vai ser um serie DTO novo tbm e estou passando os parametros
                .collect(Collectors.toList());// aqui estou colocando essa coleta em uma lista
    }



}
