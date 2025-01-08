package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.controller.dto.AutorDTO;
import io.github.miguelpevidor.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.miguelpevidor.libraryapi.controller.dto.ErroResposta;
import io.github.miguelpevidor.libraryapi.controller.dto.ResultadopesquisaLivroDTO;
import io.github.miguelpevidor.libraryapi.controller.mappers.LivroMapper;
import io.github.miguelpevidor.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.model.GeneroLivro;
import io.github.miguelpevidor.libraryapi.model.Livro;
import io.github.miguelpevidor.libraryapi.service.AutorService;
import io.github.miguelpevidor.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("livros")
public class LivroController implements GenericController {

    @Autowired
    private LivroService service;

    @Autowired
    private AutorService autorService;

    @Autowired
    private LivroMapper mapper;

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO dto) {

        Livro livro = mapper.toEntity(dto);
        livro = service.salvar(livro);

        //http://localhost:8080/autores/f235621f-4200-4b8c-824c-1ca568ae4b82
        URI location = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultadopesquisaLivroDTO> obterDetalhes(@PathVariable UUID id){

        return service.obterLivroPorId(id)
                .map(livro -> {
                    ResultadopesquisaLivroDTO dto = mapper.toResultadopesquisaLivroDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(()->{
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> excluirLivro(@PathVariable UUID id){
        Optional<Livro> livro = service.obterLivroPorId(id);
        if (livro.isPresent()){
            service.excluitPorId(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<ResultadopesquisaLivroDTO>> pesquisar(
            @RequestParam(name = "isbn",required = false)
            String isbn,
            @RequestParam(name = "titulo",required = false)
            String titulo,
            @RequestParam(name = "nome-autor",required = false)
            String nomeAutor,
            @RequestParam(name = "genero",required = false)
            GeneroLivro genero,
            @RequestParam(name = "ano-publicacao",required = false)
            Integer anoPublicacao,
            @RequestParam(name = "pagina",defaultValue = "0")
            int pagina,
            @RequestParam(name = "tamanho-pagina",defaultValue = "10")
            int tamanhoPagina
    ){

        Page<Livro> paginaResultado = service.pesquisar(isbn, titulo, nomeAutor, genero, anoPublicacao,pagina,tamanhoPagina);
        Page<ResultadopesquisaLivroDTO> resultado = paginaResultado.map(mapper::toResultadopesquisaLivroDTO);

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> atualizar(@PathVariable UUID id,@RequestBody @Valid CadastroLivroDTO dto){
        Optional<Livro> optionalLivro = service.obterLivroPorId(id);
        Autor autor = autorService.obterAutorPorId(dto.idAutor()).orElse(null);

        if(optionalLivro.isPresent()){
            Livro  livro = optionalLivro.get();
            livro.setIsbn(dto.isbn());
            livro.setTitulo(dto.titulo());
            livro.setPreco(dto.preco());
            livro.setGenero(dto.genero());
            livro.setDataPublicacao(dto.dataPublicacao());
            livro.setAutor(autor);

            service.atualizar(livro);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
