package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Categoria;
// isso somente vai devolver uma resposta não vai estar ligada na regra de negocio
public record SerieDTO(long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       String atores,
                       Categoria genero,
                       String poster,
                       String sinopse) {

}
