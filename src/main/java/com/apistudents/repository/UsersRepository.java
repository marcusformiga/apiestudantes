package com.apistudents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apistudents.model.UserDTO;
import com.apistudents.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	Users save(UserDTO dto);
	@Query("select u from Users u where u.login = ?1")
	Users findUserByLogin(String login);

}
