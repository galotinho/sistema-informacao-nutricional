package com.sistema.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.datatables.Datatables;
import com.sistema.datatables.DatatablesColunas;
import com.sistema.model.Ingrediente;
import com.sistema.repository.IngredienteRepository;

@Service
public class IngredienteService {
	
	@Autowired
	private IngredienteRepository repository;
		
	@Autowired
	private Datatables dataTables;
	
	@Transactional(readOnly=false)
	public void salvar(Ingrediente ingrediente) {
		repository.save(ingrediente);		
	}

	@Transactional(readOnly=true)
	public Map<String,Object> buscarIngredientes(HttpServletRequest http) {
		
		dataTables.setRequest(http);
		dataTables.setColunas(DatatablesColunas.INGREDIENTES);
		
		Page<?> page;
		
		if(dataTables.getSearch().isEmpty()) {
			page = repository.findAll(dataTables.getPageable());
		}else {
			page = repository.findAllByNome(dataTables.getSearch(), dataTables.getPageable());
		}
		
		return dataTables.getResponse(page);
	}

	@Transactional(readOnly=true)
	public Ingrediente buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly=false)
	public void remover(Long id) {
		repository.deleteById(id);		
	}
	
	@Transactional(readOnly=true)
	public List<Ingrediente> buscarTodos() {
		return repository.findAll();
	}
}
