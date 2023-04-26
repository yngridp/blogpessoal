package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController //classe será um controladar rest eresponderá o qeu vier em determinado contulta
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*" ) // habilitar requisições vindo de outras origens, 
// se eu quisesse buscar de uma api especifica, colocaria no lugar do asteristico
public class PostagemController {
	
	@Autowired // injeção de dependecia
	private PostagemRepository postagemRepository; // cirando um objeto que vai puxar todos os metodos da interface repository
	
	@Autowired
	private TemaRepository temaRepository;
	
	//CRUD(criar, ler, deletar e atualizar)
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
	  return temaRepository.findById(postagem.getTema().getId())// corpo da respota, metodo status(CREATED) indica que alguma coisa foi criada dentro do banco
			  .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem))) //se variavel resposta não for nula mostrará a resposta
		      .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	  /*INSERT INTO tb_postagens(data, titulo, texto)
	  VALUES (?, ?, ?)*/
	}	
	@PutMapping //atualizar 
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){ // como vai mexer, no corpo da requisição utilizar o metodo status

		//Optional<Postagem> post = postagemRepository.findById(id);
		// solução para quando não encontrar o id e adicionar mensagem , quando id não encontrado o  erro será 404
		//if(postagem.isEmpty())
		//	 throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		return postagemRepository.findById(postagem.getId())
				.map(resposta -> temaRepository.findById(postagem.getTema().getId())
						.map(resposta2 -> ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem)))
			      .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
			      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
     /*UPDATE tb_postagens SET titulo = ?, texto = ?, data = ?
     * WHERE ID = id*/
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT) // mostrar o erro quando apagar, por exemplo NO_CONTENT ( erro 204)
	@DeleteMapping("/{id}") //void pois delete nao devolve nada
	public void delete(@PathVariable Long id) { 
		
		Optional<Postagem> postagem = postagemRepository.findById(id);
		// solução para quando não encontrar o id e adicionar mensagem , quando id não encontrado o  erro será 404
		if(postagem.isEmpty())
			 throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		postagemRepository.deleteById(id);
	    /* DELETE FROM tb_postagens WHERE id = id*/
		
	}
	
}