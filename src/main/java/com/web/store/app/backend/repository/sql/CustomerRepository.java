package com.web.store.app.backend.repository.sql;

import com.web.store.app.backend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}