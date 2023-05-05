package com.projetjavaopc.api.repository;

import com.projetjavaopc.api.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    public Users findByEmail(String email);
        
}