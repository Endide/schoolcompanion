package fr.endide.application.data.service;

import fr.endide.application.data.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    Message findAllByTopicSince(String topic, Instant since);
}
