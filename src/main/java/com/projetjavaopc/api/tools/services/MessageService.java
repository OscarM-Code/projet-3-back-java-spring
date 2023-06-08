package com.projetjavaopc.api.tools.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetjavaopc.api.dto.MessageDto;
import com.projetjavaopc.api.error.CreateMessageException;
import com.projetjavaopc.api.models.Messages;
import com.projetjavaopc.api.repository.MessagesRepository;

@Service
public class MessageService {

    @Autowired
    MessagesRepository messagesRepository;

    @Transactional(transactionManager = "transactionManager")
    public Messages createMessage(MessageDto message, Long id) {
        // Créer un objet User à partir de UserDto
        try {
            Messages messageToSend = new Messages();
            messageToSend.setMessage(message.getMessage());
            messageToSend.setCreatedAt(new Date());
            messageToSend.setRentalId(message.getRentalId());
            messageToSend.setUserId(id);
            Messages messageSend = messagesRepository.save(messageToSend);
            return messageSend;
        } catch (Exception e) {
            throw new CreateMessageException("Error creating rental: " + e.getMessage());
        }
    }

}
