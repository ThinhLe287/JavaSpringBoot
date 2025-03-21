package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    long countByRolesNameNot(String roleName);

    Optional<User> findByUsername(String username);

    boolean existsById(String id);

}
