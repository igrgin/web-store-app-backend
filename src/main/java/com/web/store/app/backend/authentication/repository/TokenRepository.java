package com.web.store.app.backend.authentication.repository;

import com.web.store.app.backend.authentication.entity.Token;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
            SELECT t FROM Token t
            INNER JOIN AppUser u ON u.id = t.user.id
            WHERE t.user.id = ?1""")
    List<Token> findAllTokensByUser(Long userId);

    Optional<Token> findByToken(String token);

    @Transactional
    void deleteTokenByToken(String token);

    @Transactional
    void deleteTokensByTokenIn(List<String> token);

    @Nonnull
    List<Token> findAll();
}
