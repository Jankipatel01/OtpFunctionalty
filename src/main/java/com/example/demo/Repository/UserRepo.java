package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	User findByUsername(String username);

	

	User findFirstByUsername(String username);

    @Modifying
	@Query("update User u set u.otp=:otp where u.username=:username")
	void updateOPT(String otp, String username);


}
