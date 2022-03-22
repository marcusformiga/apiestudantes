package com.apistudents.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apistudents.model.Users;
import com.apistudents.repository.UsersRepository;

@Service
public class UsersService implements UserDetailsService{

	@Autowired
	UsersRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*verificar se existe user por login*/
		Users userExists = userRepo.findUserByLogin(username);
		if(userExists == null) {
			throw new UsernameNotFoundException("Usuario como o login informado não existe");
		}
		return new User(userExists.getLogin(), userExists.getPassword(), userExists.getAuthorities());
	}

}
