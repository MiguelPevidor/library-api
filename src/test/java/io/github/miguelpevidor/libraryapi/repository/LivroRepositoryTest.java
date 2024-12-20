package io.github.miguelpevidor.libraryapi.repository;

import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.model.GeneroLivro;
import io.github.miguelpevidor.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LivroRepositoryTest {
    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarLivroTest(){
        Livro livro = new Livro();
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setIsbn("90887-84874");
        livro.setTitulo("Mestre da Magia");
        livro.setDataPublicacao(LocalDate.of(2017,5,6));

        Autor autor = autorRepository.findById(
                UUID.fromString("4fdd11bf-0ebf-4c04-aa04-1a2c9068423b"))
                .orElse(null);

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    void salvarLivroCascade(){
        Livro livro = new Livro();
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setIsbn("90887-84874");
        livro.setTitulo("Harry Potter");
        livro.setDataPublicacao(LocalDate.of(2017,5,6));

        Autor autor = new Autor();
        autor.setNome("Sicario");
        autor.setNacionalidade("Ingles");
        autor.setDataNascimento(LocalDate.of(1915,8,30));

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    void salvarLivroEAutor(){
        Livro livro = new Livro();
        livro.setGenero(GeneroLivro.ROMANCE);
        livro.setIsbn("90887-84874");
        livro.setTitulo("Pai de Mentirinha");
        livro.setDataPublicacao(LocalDate.of(2017,5,6));

        Autor autor = new Autor();
        autor.setNome("Adao Silva");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1915,8,30));

        autorRepository.save(autor);

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    void atualizarAutorDoLivro(){
        var idLivro = UUID.fromString("fc1395a0-d387-4c65-838e-313af30f97bc");
        var livro = livroRepository.findById(idLivro).orElse(null);

        var idAutor = UUID.fromString("8ad50b11-5481-4292-9688-8febf26981e3");
        Autor autorSicario = autorRepository.findById(idAutor).orElse(null);

        livro.setAutor(autorSicario);
        livroRepository.save(livro);
    }

    @Test
    void pesquisarPorTituloTest(){
        String titulo = "O vigarista";
        List<Livro> livros = livroRepository.findByTitulo(titulo);
        livros.forEach(System.out::println);
    }

    @Test
    void listarLivrosComQueryJPQL(){
        List<Livro> livros = livroRepository.listarLivrosOrdernadoPorTituloAndPreco();
        livros.forEach(System.out::println);
    }

    @Test
    void listarAutoresComQueryJPQL(){
        List<Autor> autores = livroRepository.listarAutoresDosLivros();
        autores.forEach(System.out::println);
    }

    @Test
    void listarGenerosLivrosDeAutoresBrasileiros(){
        List<GeneroLivro> generos = livroRepository.listarGenerosAutoresbrasileiros();
        generos.forEach(System.out::println);
    }

    @Test
    void listarLivroPorGeneroQueryParam(){
        List<Livro> livros = livroRepository.findByGenero(GeneroLivro.CIENCIA);
        livros.forEach(System.out::println);
    }

    @Test
    void listarLivroPorGeneroPositionalParam(){
        List<Livro> livros = livroRepository.findByGeneroPositionalParameters(GeneroLivro.CIENCIA,"dataPublicacao");
        livros.forEach(System.out::println);
    }

    @Test
    void updatePorDataPublicacao(){
        livroRepository.updateByDataPublicacao(LocalDate.of(2000,5,20));
    }

    @Test
    void deletarPorGenero(){
        livroRepository.deleteByGenero(GeneroLivro.ROMANCE);
    }
    @Test
    void excluirLivro(){
        var id = UUID.fromString("235bddf0-cd47-4f96-b0fb-a30b8971febf");
        livroRepository.deleteById(id);
    }


}