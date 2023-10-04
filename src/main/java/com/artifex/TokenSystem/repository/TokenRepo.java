package com.artifex.TokenSystem.repository;

import com.artifex.TokenSystem.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepo extends JpaRepository<Token, Integer> {
    // Custom queries and methods can be added here
    List<Token> findByTokenStatus(int tokenStatus);

    Token deleteByTokenId(int tokenId);
}
