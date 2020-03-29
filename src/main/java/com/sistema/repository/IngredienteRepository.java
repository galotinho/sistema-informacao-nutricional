package com.sistema.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sistema.model.Ingrediente;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long>{
	
	@Query("select i from Ingrediente i where i.nome like :nome%")
	Page<Ingrediente> findAllByNome(String nome, Pageable pageable);
}
