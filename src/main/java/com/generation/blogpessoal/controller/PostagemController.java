package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*" )
public class PostagemController {
	
	@Autowired
	private PostagemRepository postagemRepository;
	
	@GetMapping // getmapping=devolver //mostrará uma lista com os objetos da postagem no http
	public ResponseEntity<List<Postagem>> getAll(){ // lista de todos os objetos da classe postagem // resposta no http 
       return ResponseEntity.ok(postagemRepository.findAll()); //status de ok, se encontrou, irá mostrar a lista toda
    		   //SELECT * FROM tb_postagens;
}
	@GetMapping("/{id}") // parametro entre chaves = variavel , o valor altera conforme a necessidade
	public ResponseEntity<Postagem> getById(@PathVariable Long id){ //durante o codigo não pode ter nomes de metodos iguais //byId = um objeto da classe postagem
          //PathVariable vai pegar o id e passar diretamente na URL
		return postagemRepository.findById(id) //procurar no metodo o id e dar a resposta se encontrado
		// criacao lambda para tratar erro nulo, utiliza metodos map e orElse -> que são de optional( pois o objeto não pode ser nulo)
		      .map(resposta -> ResponseEntity.ok(resposta)) //se variavel resposta não for nula mostrará a resposta
		      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); //nao encontrou ,mostrará erro, false
    		   //SELECT * FROM tb_postagens WHERE id = id que vc quer que procure
	           //lampda -> igual if e else mais simplificado
	}
	@GetMapping("/titulo/{titulo}") // titulo indicador e depois da barra vai digitar o titulo que quer procurar
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo)); // ok - status comum 
		
		//SELECT * FROM tb_postagens WHERE titulo = id que vc quer que procurar
		//utilizar no ecommerce para consulta de produtos
	}
	
	@PostMapping // método todo post
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){ // entre () - objeto, valid-validar a request do corpo
	  return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem)); // corpo da respota, metodo status(CREATED) indica que alguma coisa foi criada dentro do banco
     /*INSERT INTO tb_postagens(data, titulo, texto)
	  VALUES (?, ?, ?)*/
	}	
	@PutMapping //atualizar 
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){  // como vai mexer, no corpo da requisição utilizar o metodo status
	  return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem)); 
     /*UPDATE tb_postagens SET titulo = ?, texto = ?, data = ?
     * WHERE ID = id*/
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT) // mostrar status 204 quando apagar
	@DeleteMapping("/{id}") //void pois delete nao devolve nada
	public void delete(@PathVariable Long id) { 
		postagemRepository.deleteById(id);
		//colocar solução para quando não encontrar o id e adicionar mensagem , quando id não encontrado o  erro será 404
	    /* DELETE FROM tb_postagens WHERE id = id*/
		
		/*(exemplo da solução delete)
		.map(id>  -> ResponseEntity.ok(delete))
	    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());  */
	}
	
}