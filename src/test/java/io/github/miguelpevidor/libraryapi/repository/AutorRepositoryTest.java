package io.github.miguelpevidor.libraryapi.repository;

import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.model.GeneroLivro;
import io.github.miguelpevidor.libraryapi.model.Livro;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository repository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest(){

        Autor autor = new Autor();
        autor.setNome("Mariela");
        autor.setNacionalidade("Francesa");
        autor.setDataNascimento(LocalDate.of(1895,8,30));

       repository.save(autor);
    }

    @Test
    void salvarAutorComLivros(){
        Autor autor = new Autor();
        autor.setNome("Ricardo");
        autor.setNacionalidade("Italiano");
        autor.setDataNascimento(LocalDate.of(2005,6,21));

        Livro livro1 = new Livro();
        livro1.setGenero(GeneroLivro.CIENCIA);
        livro1.setIsbn("90887-84874");
        livro1.setTitulo("O vigarista");
        livro1.setDataPublicacao(LocalDate.of(2017,5,6));
        livro1.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setGenero(GeneroLivro.CIENCIA);
        livro2.setIsbn("90887-84874");
        livro2.setTitulo("Matilda");
        livro2.setDataPublicacao(LocalDate.of(1984,5,6));
        livro2.setAutor(autor);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro1);
        autor.getLivros().add(livro2);

        repository.save(autor);
        livroRepository.saveAll(autor.getLivros());
    }

    @Test
    public void atualizarTest(){
        var id = UUID.fromString("1cba27b3-295a-43eb-81f8-6f10715c4b2f");

        Optional<Autor> autor = repository.findById(id);

        if(autor.isPresent()){
            Autor autorPresente = autor.get();
            autorPresente.setDataNascimento(LocalDate.of(1966,4,25));

            repository.save(autorPresente);
        }
    }

    @Test
    public void  listarTest(){
        List<Autor> lista = repository.findAll();
        lista.forEach(System.out::println);
    }

    @Test
    void listarLivrosDoAutor(){
        UUID id = UUID.fromString("a0d618df-6696-44d0-879f-9dddf248d5e9");
        Autor autor = repository.findById(id).get();

        //buscar os livros do autor
        List<Livro> livros = livroRepository.findByAutor(autor);
        autor.setLivros(new ArrayList<>());
        autor.getLivros().addAll(livros);
        autor.getLivros().forEach(System.out::println);
    }

    @Test
    public void excluirPorIdTest(){
        var id = UUID.fromString("1cba27b3-295a-43eb-81f8-6f10715c4b2f");
        repository.deleteById(id);
    }

    @Test
    public void excluirPorObjetoTest(){
        var id = UUID.fromString("1cba27b3-295a-43eb-81f8-6f10715c4b2f");
        Optional<Autor> autor = repository.findById(id);
        if (autor.isPresent()) {
            Autor autorDeletado = autor.get();
            repository.delete(autorDeletado);
        }
    }
}
