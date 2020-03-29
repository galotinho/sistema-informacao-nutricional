package com.sistema.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistema.model.Formula;
import com.sistema.service.CalcularService;

import net.sf.jasperreports.engine.JRException;


@Controller
@RequestMapping("calcular")
public class CalculoController {
	
	@Autowired
	private CalcularService service;

	@GetMapping({"","/"})
	public String abrir(Formula formula) {
		return "calculo/calculo";
	}
	
	@PostMapping("/calcular")
	public String calcular(Formula formula, RedirectAttributes attr, HttpServletResponse response, @AuthenticationPrincipal User usuario) throws JRException, IOException, SQLException {
		
		Double soma = service.calcularTotalPercentual(formula.getQuantidades());
		int quantidade = service.debitarCredito(usuario);
		
		if(quantidade == -1) {
			attr.addFlashAttribute("falha", "Você não tem mais créditos. Entre em contato e adquira mais!");	
			return "redirect:/calcular";
		}
				
		if(soma > 100.2 || soma < 99.8) {
			attr.addFlashAttribute("falha", "O soma do percentual precisa atingir 100%");	
			return "redirect:/calcular";
		}else {			
			service.relatorio(response, usuario.getUsername(), formula);			
			return "calculo/calculo";
		}
	}	

}
