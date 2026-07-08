package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Optional;
import java.util.OptionalDouble;

//ISSO E UM METODO CONSTRUTOR esse arquivo todo
public class Serie {
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    private String atores;
    private Categoria genero; // AQUI CRIEI UM ENUM COM AS CATEGORIAS QUE EU QUERO
    private String poster;
    private String sinopse;

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0.0); // ESTOU TROCNDO O DADOS DE STRING PARA DOUBLE E CASO ESSE VALOR N EXISTA EU COLOCO ELE COMO 0.0
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim()); //  ESSE METODOS ESTA NA ENUN / estou pegando a index [0]
        this.poster = dadosSerie.poster();
        this.sinopse = dadosSerie.sinopse();
        this.atores = dadosSerie.atores();

    }
}
