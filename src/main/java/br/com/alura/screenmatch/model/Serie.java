package br.com.alura.screenmatch.model;

import java.util.OptionalDouble;

//ISSO E UM METODO CONSTRUTOR esse arquivo
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return " genero=" + genero +
                ",titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'' ;
    }
}
