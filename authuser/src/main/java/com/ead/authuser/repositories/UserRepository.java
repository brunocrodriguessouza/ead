package com.ead.authuser.repositories;

import com.ead.authuser.models.UserModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<UserModel, UUID> {

  boolean existsByUsername(String username);
  boolean existsByEmail(String email);

}
