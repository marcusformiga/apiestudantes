package com.apistudents.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import com.apistudents.service.JwtApiAuthFilter;
import com.apistudents.service.UsersService;

/*Classe central de segurança do spring
 * fazemos nela a seguranca da nossa api, end points etc, autoriza ou block acessos a urls
 * authmanager = provedor de autenticacao*/

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsersService userService;
	
	// config acessos http
	@Override
		protected void configure(HttpSecurity http) throws Exception {
			// ativando protecao contra usuarios nao identificados por token
			http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			// ativando a permissao para a pagina inicial
			.disable().authorizeRequests().antMatchers("/").permitAll()
			// ativando restricação contra url
			.antMatchers("/index").permitAll()
			// liberacao do cors para todos endpoints e metodos
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			// deslogou do sistema volta para o index
			.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
			// mapea url de logout e invalida user
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.and().addFilterBefore(new JWTLoginFilter("/users/login", authenticationManager()), 
					UsernamePasswordAuthenticationFilter.class).addFilterBefore(new JwtApiAuthFilter(), UsernamePasswordAuthenticationFilter.class);
			

			
			
			//todo:filtrar login para auth
			
		}
	@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			// serviço que ira consultar usuarios no bd
			auth.userDetailsService(userService)
			.passwordEncoder(new BCryptPasswordEncoder());
		}
}
