package com.apistudents.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

// filtro onde todas as req sao capturadas para autenticar
@Service
public class JwtApiAuthFilter extends GenericFilterBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//estabelece a auth para a requisicao
		Authentication auth = new JwtTokenAuthService().getAuthentication((HttpServletRequest) request);
		
		// coloca o processo de autenticao no spring
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		// segue o fluxo da app
		chain.doFilter(request, response);
		
	}

}
