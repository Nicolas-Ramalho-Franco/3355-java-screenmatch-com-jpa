package br.com.alura.screenmatch.repositoty;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo); // buscando por titulo iguinorando Maiusculo

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor , double avaliacao);// Buscando por ator e tendo como parametro usando as avaliações

    List<Serie> findTop5ByOrderByAvaliacaoDesc(); // buscando o top 5 em orde decresente

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessthanEqualsAndLiacaoGreaterThanEqual(Integer totalTemporadasLessthan, Double avaliacao);

}
