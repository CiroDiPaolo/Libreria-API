package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    UserEntity findByUsernameAndEmail(String username, String email);

    UserEntity findByUsernameAndPass(String username, String pass);

    UserEntity findByEmailAndPass(String email, String pass);

    UserEntity findByUsernameAndEmailAndPass(String username, String email, String pass);

    Page<UserEntity> findAll(Pageable pageable);

}
