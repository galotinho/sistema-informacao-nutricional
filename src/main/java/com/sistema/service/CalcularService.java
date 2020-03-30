package com.sistema.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.sistema.model.Formula;
import com.sistema.model.Ingrediente;
import com.sistema.model.Usuario;
import com.sistema.repository.IngredienteRepository;
import com.sistema.repository.UsuarioRepository;
import com.zaxxer.hikari.HikariDataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class CalcularService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HikariDataSource hikariDataSource;
	
	@Autowired
	private IngredienteRepository ingredienteRepository;
	
	public Double calcularTotalPercentual(List<Double> quantidades) {
		
		Double soma = 0.0;
		for(Double quantidade : quantidades) {
			soma = soma+quantidade;
		}
		return soma;
	}

	public void relatorio(HttpServletResponse response, String username, Formula formula) throws JRException, IOException, SQLException {
		
	    Map<String, Object> parametros = new HashMap<>();
	    Optional<Usuario> chocolatier = usuarioRepository.findByEmail(username);
		List<Ingrediente> composicao = processarCalculo(formula);
		
		Optional<Double> carboidratos = composicao.stream().map(x -> x.getCarboidratos()).reduce((x,y) -> x + y);
		Optional<Double> proteinas = composicao.stream().map(x -> x.getProteinas()).reduce((x,y) -> x + y);
		Optional<Double> gordurastotais = composicao.stream().map(x -> x.getGordurastotais()).reduce((x,y) -> x + y);
		Optional<Double> gordurassaturadas = composicao.stream().map(x -> x.getGordurassaturadas()).reduce((x,y) -> x + y);
		Optional<Double> gordurastrans = composicao.stream().map(x -> x.getGordurastrans()).reduce((x,y) -> x + y);
		Optional<Double> fibraalimentar = composicao.stream().map(x -> x.getFibraalimentar()).reduce((x,y) -> x + y);
		Optional<Double> sodio = composicao.stream().map(x -> x.getSodio()).reduce((x,y) -> x + y);		
		
		double energeticokcal = (carboidratos.get()*4+proteinas.get()*4+gordurastotais.get()*9);
		double energeticokj = (carboidratos.get()*17+proteinas.get()*17+gordurastotais.get()*37);
	
		parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
		parametros.put("NOME", chocolatier.get().getNome());		
		parametros.put("PORCAO", String.valueOf(formula.getPorcao()).concat("g"));
		
		parametros.put("CARBOIDRATOS", new DecimalFormat("#,##0.00").format(carboidratos.get()).concat(" g"));
		parametros.put("PROTEINAS", new DecimalFormat("#,##0.00").format(proteinas.get()).concat(" g"));
		parametros.put("GORDURASTOTAIS", new DecimalFormat("#,##0.00").format(gordurastotais.get()).concat(" g"));
		parametros.put("GORDURASSATURADAS", new DecimalFormat("#,##0.00").format(gordurassaturadas.get()).concat(" g"));
		parametros.put("GORDURASTRANS", new DecimalFormat("#,##0.00").format(gordurastrans.get()).concat(" g"));
		parametros.put("FIBRAALIMENTAR", new DecimalFormat("#,##0.00").format(fibraalimentar.get()).concat(" g"));
		parametros.put("SODIO", new DecimalFormat("#,##0.00").format(sodio.get()*1000).concat(" mg"));
		
		parametros.put("VDCARBOIDRATOS", new DecimalFormat("#,##0.0").format((carboidratos.get()/300)*100));
		parametros.put("VDPROTEINAS", new DecimalFormat("#,##0.0").format((proteinas.get()/75)*100));
		parametros.put("VDGORDURASTOTAIS", new DecimalFormat("#,##0.0").format((gordurastotais.get()/55)*100));
		parametros.put("VDGORDURASSATURADAS", new DecimalFormat("#,##0.0").format((gordurassaturadas.get()/22)*100));
		parametros.put("VDGORDURASTRANS", new DecimalFormat("#,##0.0").format((gordurastrans.get()/25)*100));
		parametros.put("VDFIBRAALIMENTAR", new DecimalFormat("#,##0.0").format((fibraalimentar.get()/25)*100));
		parametros.put("VDSODIO", new DecimalFormat("#,##0.0").format((sodio.get()/24)*1000));
		
		parametros.put("VDENERGETICO", new DecimalFormat("#,##0.0").format((energeticokj/8400)*100));
		parametros.put("ENERGETICOKCAL", new DecimalFormat("#,##0.00").format(energeticokcal));
		parametros.put("ENERGETICOKJ", new DecimalFormat("#,##0.00").format(energeticokj));
			
		InputStream jasperStream = this.getClass().getResourceAsStream("/relatorios/informacao.jasper");
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, hikariDataSource.getConnection());

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=informacao-nutricional.pdf");
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());	
		
	}

	private List<Ingrediente> processarCalculo(Formula formula) {
		List<Double> listaEmGramas = conveterEmGramas(formula.getQuantidades(), formula.getPorcao());
		
		return gerarComposicaoIngredientes(listaEmGramas, formula.getIngredientes());	
	}

	private List<Ingrediente> gerarComposicaoIngredientes(List<Double> listaEmGramas, List<Long> ingredientes) {
		
		List<Ingrediente> composicao = new ArrayList<>();
		
		for(int i=0; i<ingredientes.size(); i++) {
			
			Ingrediente ingrediente = ingredienteRepository.findById(ingredientes.get(i)).get();
			
			ingrediente.setCarboidratos((ingrediente.getCarboidratos()*listaEmGramas.get(i))/100);
			ingrediente.setProteinas((ingrediente.getProteinas()*listaEmGramas.get(i))/100);
			ingrediente.setGordurastotais((ingrediente.getGordurastotais()*listaEmGramas.get(i))/100);
			ingrediente.setGordurassaturadas((ingrediente.getGordurassaturadas()*listaEmGramas.get(i))/100);
			ingrediente.setGordurastrans((ingrediente.getGordurastrans()*listaEmGramas.get(i))/100);
			ingrediente.setFibraalimentar((ingrediente.getFibraalimentar()*listaEmGramas.get(i))/100);
			ingrediente.setSodio((ingrediente.getSodio()*listaEmGramas.get(i))/100);		
						
		    composicao.add(ingrediente);
		}
			
		return composicao;
	}

	private List<Double> conveterEmGramas(List<Double> quantidades, int porcao) {
		
		List<Double> listaEmGramas = new ArrayList<>();
		
		for(Double quantidade : quantidades) {
			listaEmGramas.add((porcao*quantidade)/100);
		}
		
		return listaEmGramas;		
	}

	public int debitarCredito(User usuario) {
		Usuario user = usuarioRepository.findByEmail(usuario.getUsername()).get();
		if(user.getPerfil().equals("USUARIO")) {
			if(user.getQuantidade()>0) {
				user.setQuantidade(user.getQuantidade()-1);
				usuarioRepository.save(user);
				return 0;
			}else {
				return -1;
			}
		}
		return 0;		
	}
}
