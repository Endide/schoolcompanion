package fr.endide.application.data.service;

import com.vaadin.collaborationengine.CollaborationMessage;
import fr.endide.application.data.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class MessageService {
    private final MessageRepository repository;

    @Autowired
    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public Optional<Message> get(UUID id) {
        return repository.findById(id);
    }
    public Message getByEmail(String email) {
        return repository.findByStudentMail(email);
    }
    public boolean exists(String email) {
        return repository.existsCardsByStudentMail(email);
    }
    public List<Message> findAllByTopicSince(String topic, Instant since) {
        return repository.findAllByTopicAndDateAfterOrDate(topic, since, since);
    }

    public Message update(Message entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Message> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
