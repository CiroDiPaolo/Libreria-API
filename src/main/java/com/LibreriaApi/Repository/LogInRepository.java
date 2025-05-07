package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogInRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByUsernameAndEmail(String username, String email);

    User findByUsernameAndPass(String username, String pass);

    Optional<User> findByEmailAndPass(String email, String pass);

    User findByUsernameAndEmailAndPass(String username, String email, String pass);

}
