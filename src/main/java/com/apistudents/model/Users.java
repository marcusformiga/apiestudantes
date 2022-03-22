package com.apistudents.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Users implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String login;
	private String password;
	@CreationTimestamp
	private Instant createdAt;
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Phone> phones = new ArrayList<Phone>();
	
	/*Explicando o rel
	 * joinTable name = nome da tabela no banco de dados, nao eh permitida a duplicacao de role
	 * dentro da tabela pivo estao os id de usuarios e das roles
	 * joincolumn name = nome em que sera feita o join para buscar os dados relacionados
	 * foreingkey = nome da chave estrangeira na tabela pivot*/
	@OneToMany(fetch = FetchType.EAGER) // tras as roles anexadas
	@JoinTable(name = "users_roles", uniqueConstraints = @UniqueConstraint (
			columnNames = {"id_users", "id_roles"}, name = "unique_role_user"),
	joinColumns = @JoinColumn(name = "id_users", referencedColumnName = "id", table = "users",
	foreignKey = @ForeignKey (name = "fk_users", value = ConstraintMode.CONSTRAINT)),
	inverseJoinColumns = @JoinColumn (name = "id_roles", referencedColumnName = "id", table = "role",
	foreignKey = @ForeignKey(name = "fk_roles", value = ConstraintMode.CONSTRAINT)))
	private List<Role> roles;
	
	public Users() {}

	public Users(Long id, String name, String login, String password, Instant createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.login = login;
		this.password = password;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public List<Phone> getPhones() {
		return phones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// controle de acesso, mapear relacionamento
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return roles;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.login;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
}
