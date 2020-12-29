package com.kyurao.sweater.service;

import com.kyurao.sweater.domain.Message;
import com.kyurao.sweater.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public void saveMessage(Message message, String file) {
        message.setFilename(file);
        messageRepository.save(message);
    }

    public void updateMessage(Message message, Message newMessage, String file) {

        if (file != null && !file.isEmpty()) {
            message.setFilename(file);
        }
        message.setTag(newMessage.getTag());
        message.setText(newMessage.getText());

        messageRepository.save(message);
    }
}