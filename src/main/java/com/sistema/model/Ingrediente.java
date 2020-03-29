package com.sistema.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity	
@Table(name = "ingredientes")
public class Ingrediente {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private Double carboidratos;
	private Double proteinas;
	private Double gordurastotais;
	private Double gordurassaturadas;
	private Double gordurastrans;
	private Double fibraalimentar;
	private Double sodio;
}
