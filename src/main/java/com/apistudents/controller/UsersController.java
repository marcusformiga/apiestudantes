package com.apistudents.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import org.springframework.web.server.ResponseStatusException;

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
	public ResponseEntity<Users> create(@RequestBody Users userEntity){
		String hashPass = new BCryptPasswordEncoder().encode(userEntity.getPassword());
		userEntity.setPassword(hashPass);
		Users user = userRepo.save(userEntity);
		return ResponseEntity.created(null).body(user);
	}
	@GetMapping
	@CacheEvict(value = "cacheusers", allEntries = true) // remove cache que nao esta sendo utilizado
	@CachePut("cacheusers") // tras atualizacoes dos dados cacheados
	// fazer cache @Cacheable ("nomedocache") -> enablecache na main
	// cacheevict arranca cache inutilizado
	// cacheput identificar mudança nos registros e trazer nas atualizacoes
	public ResponseEntity<List<UserDTO>> findAll(){
		List<Users> usersList = userRepo.findAll();
		// transformacao de uma lista de entidade categoria em dto
		List<UserDTO> listDto = usersList.stream().map((obj) -> new UserDTO(obj))
				.collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}
	
	@GetMapping(path = "/{id}")
	public Users findUserById(@PathVariable Long id){
		Users userExists = userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return userExists;
	}
	@PutMapping
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void update(@RequestBody Users user) {
		Users userSaved = userRepo.findUserByLogin(user.getLogin());
		// se as senhas sao diferentes faz o hash da nova senha
		if(!userSaved.getPassword().equals(user.getPassword())) {
			String hashPass = new BCryptPasswordEncoder().encode(user.getPassword());
			user.setPassword(hashPass);
		}
		userRepo.save(user);
	}
	@DeleteMapping(path = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		userRepo.deleteById(id);
	}
}
