package com.projetjavaopc.api.repository;

import com.projetjavaopc.api.models.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Messages, Long> {

        
}