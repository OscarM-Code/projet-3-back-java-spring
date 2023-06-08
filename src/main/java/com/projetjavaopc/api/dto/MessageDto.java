package com.projetjavaopc.api.dto;

import com.projetjavaopc.api.models.Messages;

public class MessageDto {
    private Long rental_id;
    private String message;

    // Les constructeurs, les getters et les setters sont omis pour la brièveté

    public Long getRentalId() {
        return rental_id;
    }

    public void setRentalId(Long rental_id) {
        this.rental_id = rental_id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    // Ajouter une méthode de conversion d'un objet User en UserDto
    public static MessageDto from(Messages mess) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(mess.getMessage());
        messageDto.setRentalId(mess.getRentalId());
        return messageDto;
    }
}
