package com.sistema.service;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.datatables.Datatables;
import com.sistema.datatables.DatatablesColunas;
import com.sistema.model.Usuario;
import com.sistema.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService{

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private Datatables dataTables;
	
	@Transactional(readOnly=true)
	public Optional<Usuario> buscarPorEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override 
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario " + username + " não encontrado."));
		
		return new User(
				usuario.getEmail(),
				usuario.getSenha(),
				AuthorityUtils.createAuthorityList(usuario.getPerfil())
				);
	}

	@Transactional(readOnly=false)
	public void salvar(Usuario usuario) {
		repository.save(usuario);		
	}	
	
	@Transactional(readOnly=false)
	public void remover(Long id) {
		repository.deleteById(id);		
	}
	
	@Transactional(readOnly=true)
	public Usuario buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly=true)
	public Map<String,Object> buscarUsuarios(HttpServletRequest http) {
		
		dataTables.setRequest(http);
		dataTables.setColunas(DatatablesColunas.USUARIOS);
		
		Page<?> page;
		
		if(dataTables.getSearch().isEmpty()) {
			page = repository.findAll(dataTables.getPageable());
		}else {
			page = repository.findAllByNome(dataTables.getSearch(), dataTables.getPageable());
		}
			
		return dataTables.getResponse(page);
	}
	
}
