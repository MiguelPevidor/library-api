package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.miguelpevidor.libraryapi.controller.dto.ResultadopesquisaLivroDTO;
import io.github.miguelpevidor.libraryapi.controller.mappers.LivroMapper;
import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.model.GeneroLivro;
import io.github.miguelpevidor.libraryapi.model.Livro;
import io.github.miguelpevidor.libraryapi.service.AutorService;
import io.github.miguelpevidor.libraryapi.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;


@RestController
@PreAuthorize("hasAnyRole('OPERADOR','GERENTE')")
@RequestMapping("livros")
@Tag(name = "Livros")
public class LivroController implements GenericController {

    @Autowired
    private LivroService service;

    @Autowired
    private AutorService autorService;

    @Autowired
    private LivroMapper mapper;

    @PostMapping
    @Operation(summary = "Salvar", description = "Cadastrar novo Livro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de Validação"),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado")
    })
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO dto) {

        Livro livro = mapper.toEntity(dto);
        livro = service.salvar(livro);

        //http://localhost:8080/autores/f235621f-4200-4b8c-824c-1ca568ae4b82
        URI location = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do Livro pelo seu ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<ResultadopesquisaLivroDTO> obterDetalhes(@PathVariable UUID id){

        return service.obterLivroPorId(id)
                .map(livro -> {
                    ResultadopesquisaLivroDTO dto = mapper.toResultadopesquisaLivroDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar", description = "Deleta um Livro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
    })
    public ResponseEntity<Void> excluirLivro(@PathVariable UUID id){
        Optional<Livro> livro = service.obterLivroPorId(id);
        if (livro.isPresent()){
            service.excluitPorId(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Pesquisar", description = "Realiza pesquisa de Livros paginada, com com Parametros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso")
    })
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
    @Operation(summary = "Atualizar", description = "Atualiza um Livro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado Com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "409", description = "ISBN Duplicado"),
            @ApiResponse(responseCode = "422", description = "Erro de Validação")
    })
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
