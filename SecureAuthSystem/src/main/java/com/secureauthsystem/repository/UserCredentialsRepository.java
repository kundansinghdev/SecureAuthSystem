package com.secureauthsystem.repository;

import com.secureauthsystem.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for UserCredentials, providing CRUD operations and custom queries.
 */
@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {

    Optional<UserCredentials> findByUsername(String username);
    
    Optional<UserCredentials> findByEmail(String email);
    
    Optional<UserCredentials> findByMobileNumber(String mobileNumber); // This should match the entity field name
}
