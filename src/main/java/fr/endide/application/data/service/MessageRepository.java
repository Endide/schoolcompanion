package fr.endide.application.data.service;

import com.vaadin.collaborationengine.CollaborationMessage;
import fr.endide.application.data.entity.Cards;
import fr.endide.application.data.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    Message findByStudentMail(String email);
    void removeCardsByStudentMail(String email);
    boolean existsCardsByStudentMail(String email);
    List<Message> findAllByTopicAndDateAfterOrDate(String topic, Instant since, Instant date);
}
