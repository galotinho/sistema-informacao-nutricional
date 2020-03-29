package com.sistema.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sistema.service.UsuarioService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UsuarioService service;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests()
		 .antMatchers("/webjars/**", "/css/**", "/js/**", "/image/**").permitAll()
		 .antMatchers("/login").permitAll()	
		 .antMatchers("/calcular/").hasAnyAuthority("ADMIN", "USUARIO")
		 /*
		  * Para adicionar autorização para cada ação do controller faça: 
		  *
		  * .antMatchers("/controller/acao", "/controller/acao/acao").hasAnyAuthority("ADMIN", "PERFIL_2")
		  * .antMatchers("/acao/**").hasAuthority("ADMIN")
		  *
		  * Caso decida dar permissão por anotação, inserir o seguinte código acima do método
		  * no controller
		  * 
		  * @PreAuthorize("hasAnyAuthority('ADMIN', 'PERFIL_2')")
		  * ou
		  * @PreAuthorize("hasAuthority('ADMIN')")
		  * 
		  * */
		 .anyRequest().authenticated()
		 .and()
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/home", true)
			.failureUrl("/login-error")
			.permitAll()
		.and()
			.logout()
			.logoutSuccessUrl("/login")
		.and()
			.exceptionHandling()
			.accessDeniedPage("/acesso-negado")
		.and()
		 	.rememberMe()
		 	.rememberMeCookieName("sistema-ifbaiano")  
		    .tokenValiditySeconds(604800);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}

	
	
}
