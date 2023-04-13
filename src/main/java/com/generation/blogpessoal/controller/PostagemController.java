package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*" )
public class PostagemController {
	
	@Autowired
	private PostagemRepository postagemRepository;
	
	@GetMapping // devolver //cria um objetevo http , mostrará uma lista com os objetos da postagem
	public ResponseEntity<List<Postagem>> getAll(){ // lista de todos os objs. da classe postagem // resposta http 
       return ResponseEntity.ok(postagemRepository.findAll()); //status de ok, encontrou , irá mostrar
    		   //SELECT * FROM tb_postagens;
}

}