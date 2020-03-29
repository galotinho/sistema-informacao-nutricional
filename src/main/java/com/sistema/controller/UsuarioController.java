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

import com.sistema.config.GeradorSenhaBCrypt;
import com.sistema.model.Usuario;
import com.sistema.service.UsuarioService;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@GetMapping({"","/"})
	public String inicial(Usuario usuario) {
		return "usuario/novo";
	}
	
	@PostMapping("/salvar")
	public String salvar(Usuario usuario, RedirectAttributes attr) {
		usuario.setSenha(GeradorSenhaBCrypt.gerarBCrypt(usuario.getSenha()));
		service.salvar(usuario);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");		
		return "redirect:/usuario";
	}
	
	@GetMapping("/datatables/server")
	public ResponseEntity<?> getUsuarios(HttpServletRequest request){				
		return ResponseEntity.ok(service.buscarUsuarios(request));
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model){
		model.addAttribute("usuario", service.buscarPorId(id));
		return "usuario/novo";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr){
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
		return "redirect:/usuario";
	}
}
