package com.apistudents.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apistudents.model.UserDTO;
import com.apistudents.model.Users;
import com.apistudents.repository.UsersRepository;

@RestController
@RequestMapping(path = "/users")
@CrossOrigin
public class UsersController {

	private UsersRepository userRepo;
	public UsersController(UsersRepository userRepo) {
		this.userRepo = userRepo;
	}
	@PostMapping
	public ResponseEntity<Users> create(@RequestBody Users userData){
		Users user = userRepo.save(userData);
		return ResponseEntity.created(null).body(user);
	}
	@GetMapping
	public List<Users> findAll(){
		return this.userRepo.findAll();
	}
	@PutMapping
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void update(@RequestBody Users user) {
		userRepo.save(user);
	}
	@DeleteMapping(path = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		userRepo.deleteById(id);
	}
}
