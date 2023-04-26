package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_postagens") /*entity - criar tabela, table- nome da tabela --> no sql CREATE TABLE tb_postagens*/
public class Postagem {
	
    @Id // @id para definfir chave priamria @GeneratedValue - define como será chave
    @GeneratedValue(strategy = GenerationType.IDENTITY) //chave primária , identify-autoicrement 
	private Long id; //long = int , L maisuculo pq se refere a um objeto
    
    @NotBlank(message = "O Atributo título é obrigatório!")  //exclusivo para string, tipo not null
    @Size(min = 5, max = 100, message = "O atributo título deve ter no minímo 05 e no máximo 100 de caracteres.")
	private String titulo;
    
    @NotBlank(message = "O Atributo texto é obrigatório!")  
    @Size(min = 5, max = 100, message = "O atributo texto deve ter no minímo 10 e no máximo 1000 de caracteres.")
	private String texto;
    
    @UpdateTimestamp   // create data e hora da criaçao e update atualização data e hora da atualização
	private LocalDateTime data; // classe mais moderna no java para data */
	
    /*Relacionamento */
    
    @ManyToOne // tipo de relacionamento postagem , um para muitos
    @JsonIgnoreProperties("postagem")
    private Tema tema; // inserir o objeto da classe tema ,
    
    @ManyToOne 
    @JsonIgnoreProperties("postagem")
    private Usuario usuario; 
    
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	
	/* Criar os métodos Get e Set do Objeto Tema */
	public Tema getTema() {
		return tema;
	}
	public void setTema(Tema tema) {
		this.tema = tema;
    }
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
