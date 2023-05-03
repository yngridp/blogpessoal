package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //indica classe de teste E utilza qq porta
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired 
	private TestRestTemplate testRestTemplate; //teste parecido com o que o insomnia faz 
	
	@BeforeAll //execute o metodo antes de todos os outros
	void start() {
		
		usuarioRepository.deleteAll(); // limpa o banco
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Root","root@root.com","rootrooot","-")); //cria o usuario para autenticar
	}
	
	@Test
	@DisplayName("😎 Deve cadastrar um novo usuário")
    public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Yngrid Padilha", "yngrid_pp@gmail.com", "12345678", "-")); //corpo
		
			ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class); //resposta
			
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("😎 Não Deve permitir a duplicação do usuário")
    public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Maria","maria@root.com","12345678","-"));
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Maria","maria@root.com","12345678","-")); //corpo
		
			ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class); //resposta
			
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("😎 Deve atualizar os dados do usuário")
    public void deveAtualizarUmUsuario() {
		// criou usuario, guardou com um id
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "Julia","julia@gmail.com","12345678","-"));
		//alterar usuario
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(usuarioCadastrado.get().getId(), "Julia Souza","juliasouza@gmail.com","12345678","-")); //corpo
		
			ResponseEntity<Usuario> corpoResposta = testRestTemplate
					.withBasicAuth("root@root.com","rootrooot")
					.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class); //resposta
			
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("😎 Deve Listar todos os usuários")
    public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Ana","anamaria@root.com","12345678","-"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Joao","jaaopedro@root.com","12345678","-"));
		
			ResponseEntity<String> resposta = testRestTemplate //lista convertida para string
					.withBasicAuth("root@root.com","rootrooot")
					.exchange("/usuarios/all", HttpMethod.GET, null, String.class); //resposta
			
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	@Test
	@DisplayName("😎 Deve Procurar usuários por Id")
    public void deveProcurarUsuariosPorId() {
		
		Optional<Usuario> usuarioBusca = usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Jessica Santos", "jessicasantos@gmail.com", "jessica2", "-"));
			
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com","rootrooot")
				.exchange("/usuarios/" + usuarioBusca.get().getId(), HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	
}
	@Test
	@DisplayName("😎 Deve Logar Usuário")
    public void deveLogarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Mariana Paula", "marianapaula@gmail.com", "13465278", "-"));

			HttpEntity<UsuarioLogin> corpoRequisicao = new HttpEntity<UsuarioLogin>(new UsuarioLogin(0L, 
				"", "marianapaula@gmail.com", "13465278", "", ""));

			ResponseEntity<UsuarioLogin> corpoResposta = testRestTemplate
				.exchange("/usuarios/logar", HttpMethod.POST, corpoRequisicao, UsuarioLogin.class);

			assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());

}
}
