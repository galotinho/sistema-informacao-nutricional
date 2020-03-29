package com.sistema.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistema.model.Ingrediente;
import com.sistema.service.IngredienteService;


@Controller
@RequestMapping("ingrediente")
public class IngredienteController {

	@Autowired
	private IngredienteService service;
	
	@GetMapping({"","/"})
	public String inicial(Ingrediente ingrediente) {
		return "ingrediente/novo";
	}
	
	@PostMapping("/salvar")
	public String salvar(Ingrediente ingrediente, RedirectAttributes attr) {
		service.salvar(ingrediente);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");		
		return "redirect:/ingrediente";
	}
	
	@GetMapping("/datatables/server")
	public ResponseEntity<?> getIngredientess(HttpServletRequest request){
		return ResponseEntity.ok(service.buscarIngredientes(request));
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model){
		model.addAttribute("ingrediente", service.buscarPorId(id));
		return  "ingrediente/novo";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr){
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
		return "redirect:/ingrediente";
	}
	
	@GetMapping("/listar")
	public ResponseEntity<?> getIngredientes() {		
		return ResponseEntity.ok(service.buscarTodos());
	}
}
