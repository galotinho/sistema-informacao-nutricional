package com.sistema.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	@Query("select u from Usuario u where u.email like :email" )
	Optional<Usuario> findByEmail(@Param ("email") String email);

	@Query("select u from Usuario u where u.nome like :nome%")
	Page<Usuario> findAllByNome(String nome, Pageable pageable);
	
}
