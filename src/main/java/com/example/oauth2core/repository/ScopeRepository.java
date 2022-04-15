package com.example.oauth2core.repository;

import com.example.oauth2core.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, Long> {

    Scope findByName(String name);
}
