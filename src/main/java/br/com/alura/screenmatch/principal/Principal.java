package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repositoty.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private Optional<Serie> serieBusca;

    private List<Serie> series = new ArrayList<>();

    private SerieRepository repositorio;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3- Listar Series buscadas
                    4- Buscar Serie por titulo
                    5- Buscar Serie por Ator
                    6 - Top 5 Series
                    7 - Buscar por Genero
                    8 - Filtrar séries
                    9 - Buscar por trecho
                    10 - Buscar top 5
                    11 - Buscar por data
                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6 :
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9 :
                    buscarEpisodioPorTrecho();
                    break;
                case 10 :
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie); // isso e um injeção de dependncia vai permitir eu acessr o meu banco de dados
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma serie pelo o nome: ");

        var nomeSerie = leitura.nextLine();

        // 1. Guardamos o resultado em 'serieBuscada'
        Optional<Serie> serieBuscada =  repositorio.findFirstByTituloContainingIgnoreCase(nomeSerie);

        // 2. Verificamos o Optional, e não a lista
        if (serieBuscada.isPresent()) {
            var serieEncontrada = serieBuscada.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            // 3. O flatMap agora retorna um Stream, e o collect fica no final do encadeamento principal
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Serie Não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        // esse metodo somente exibi os dados da serie
        series = repositorio.findAll(); // isso diz para pegar todos os cados cadastrado no meu banco de dados
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma serie pelo o nome:");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findFirstByTituloContainingIgnoreCase(nomeSerie); // aqui estou colocando um buscar serie chamando o metodo que chamei non repositorio

        if (serieBusca.isPresent()) {
            System.out.println("Dados da serie : " + serieBusca.get());
        }else {
            System.out.println("Serie não encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Escolha uma serie pelo o nome do ator:");
        var nomeAtor = leitura.next();
        System.out.println("Avaliações apartir de qual valor :");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontrada = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor , avaliacao);
        System.out.println("Series em que o : "+nomeAtor+" Trabalhou" );
        seriesEncontrada.forEach(s-> System.out.println(s.getTitulo() + "avaliação : " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc(); // estou buscando as avaliações em ordem decrecente
        seriesTop.forEach(s-> System.out.println(s.getTitulo() + "avaliação : " + s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Qual categoria deseja buscar?");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Series da Categoria : " +nomeGenero);
        seriesPorCategoria.forEach(System.out::println);

    }

    private void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Qual o total de temporadas que deseja na serie ? :");
        var temporadasDesejadas = leitura.nextInt();
        System.out.println("Qual avalição essa serie precisa ter? :");
        var avaliacoesDesejadas = leitura.nextDouble();

        List<Serie> filtroSerie = repositorio.seriesPorTemporadaEAValiacao(temporadasDesejadas, avaliacoesDesejadas);
        System.out.println("*** Series filtradas ***");
        filtroSerie.forEach(s -> System.out.println(s.getTitulo() + " - avaliacao : " + s.getAvaliacao()));

    }

    private void buscarEpisodioPorTrecho(){
        System.out.println("Qual o trecho deseja buscar?");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e -> System.out.println("Serie :"+e.getSerie()+" - "+e.getTitulo() + " - Temporadas : " + e.getTemporada()));
    }

    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();

        if(serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e -> System.out.println("Serie :"+e.getSerie()+" - "+e.getTitulo() + " - Temporadas : " + e.getTemporada()));

        }
    }

    private void buscarEpisodiosDepoisDeData(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()) {
            System.out.println("Digite o ano limite de lançamento:");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodiosAno = repositorio.episodiosporSerieEAno(serieBusca.get(),anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }

}