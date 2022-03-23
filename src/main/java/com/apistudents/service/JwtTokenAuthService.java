package com.apistudents.service;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.apistudents.ApplicationContextLoad;
import com.apistudents.model.Users;
import com.apistudents.repository.UsersRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// gera token e faz validação do token
@Service
@Component
public class JwtTokenAuthService {

//	@Autowired
//	private UsersRepository userRepo;

	// Tempo de expiracao do token jwt 2dias em ms.
	private static final long EXP_TIME = 172800000;
	// Senha secreata para ajudar na auth
	private static final String SECRET = "saheusaheuash231321";
	// prefixo do token jwt
	private static final String TOKEN_PREFIX = "Bearer";
	// localizacao do cabeçalho da resp do token
	private static final String HEADER_STRING = "Authorization";

	// gerar token de auth e add resp http
	public void addAuthentication(HttpServletResponse resp, String username) throws IOException {
		// Montagem do token
		String JWT = Jwts.builder().setSubject(username) // ADD user dono do token
				.setExpiration(new Date(System.currentTimeMillis() + EXP_TIME)) // add exp na data atual
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		String token = TOKEN_PREFIX + " " + JWT;

		// add ao cabeçalho http

		resp.addHeader(HEADER_STRING, token);

		// escrever token como resposta
		// libera cors
		freeCors(resp);
		resp.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}

	// retorna o usuario valida ou null se nao tiver valido
	public Authentication getAuthentication(HttpServletRequest req, HttpServletResponse resp) {
		String token = req.getHeader(HEADER_STRING);

		try {
			if (token != null) {
				String clearToken = token.replace(TOKEN_PREFIX, "").trim(); // token que vem da req
				String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(clearToken).getBody().getSubject();
				if (user != null) { // verificar se o token enviada na req == tokenbd
					Users userValid = ApplicationContextLoad.getAppContext().getBean(UsersRepository.class)
							.findUserByLogin(user);
					return new UsernamePasswordAuthenticationToken(userValid.getLogin(), userValid.getPassword(),
							userValid.getAuthorities());
					// apos fazer todas as validações retornamos o token e os dados do user
				} else {
					return null;
				}

			}
		} catch (ExpiredJwtException e) {
			try {
				resp.getOutputStream().println("Token expirado");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		{
			freeCors(resp);
			return null;
		}
	}

	private void freeCors(HttpServletResponse resp) {
		if (resp.getHeader("Access-Control-Allow-Origin") == null) {
			resp.addHeader("Access-Control-Allow-Origin", "*");
		}
		if (resp.getHeader("Access-Control-Allow-Headers") == null) {
			resp.addHeader("Access-Control-Allow-Headers", "*");
		}
		if (resp.getHeader("Access-Control-Request-Headers") == null) {
			resp.addHeader("Access-Control-Request-Headers", "*");
		}
		if (resp.getHeader("Access-Control-Allow-Methods") == null) {
			resp.addHeader("Access-Control-Allow-Methods", "*");
		}

	}
}
