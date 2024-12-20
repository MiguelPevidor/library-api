package io.github.miguelpevidor.libraryapi.repository;

import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.model.GeneroLivro;
import io.github.miguelpevidor.libraryapi.model.Livro;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;


public interface LivroRepository extends JpaRepository<Livro, UUID> {

    //QUERY METHOD
    // select * from livro where id_autor = id
    List<Livro> findByAutor(Autor autor);

    // select * from livro where titulo = titulo
    List<Livro> findByTitulo(String titulo);

    //select * from livro where isbn = isbn
    List<Livro> findByIsbn(String isbn);

    //select * from livro where titulo = titulo and preco = preco
    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    //select * from livro where titulo = titulo or preco = preco
    List<Livro> findByTituloOrPreco(String titulo, BigDecimal preco);

    //JPQL -> referencia as entidades e suas propriedades

    // select * from livro order by titulo,preco
    @Query(" select l from Livro as l order by l.titulo, l.preco")
    List<Livro> listarLivrosOrdernadoPorTituloAndPreco();

    /**
     * select a.*
     * from livro
     * join autor a on autor.id = livro.id_autor
     * */
    @Query("select a from Livro l join l.autor a")
    List<Autor> listarAutoresDosLivros();

    @Query("""
            select l.genero
            from Livro l
            join l.autor a
            where a.nacionalidade = 'Brasileira'
            order by 1
            """)
    List<GeneroLivro> listarGenerosAutoresbrasileiros();

    // named parameters -> parametros nomeados
    @Query("""
            select l
            from Livro l
            where l.genero = :genero
            """)
    List<Livro> findByGenero(@Param("genero") GeneroLivro generoLivro);

    // positional parameters -> parametros nomeados
    @Query("""
            select l
            from Livro l
            where l.genero = ?1
            order by ?2 desc
            """)
    //O JPQL não permite que o nome de uma propriedade (como dataPublicacao) seja passado como um
    // parâmetro dinâmico diretamente na cláusula order by.
    List<Livro> findByGeneroPositionalParameters(GeneroLivro generoLivro,String nomePropriedadelivro);

    @Modifying
    @Transactional
    void deleteByGenero(GeneroLivro genero);

    @Modifying
    @Transactional
    @Query("update Livro set dataPublicacao = ?1")
    void updateByDataPublicacao(LocalDate dataPublicacao);
}
