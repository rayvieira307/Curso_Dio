package me.dio.curso_dio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.dio.curso_dio.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByAccountNumber(String number);
	
	boolean existsByCardNumber(String number);

}
