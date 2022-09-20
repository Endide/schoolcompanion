package fr.endide.application.data.service;

import fr.endide.application.data.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface CardRepository extends JpaRepository<Cards, UUID> {
    Cards findByEmail(String email);
    void removeCardsByEmail(String email);
    List<Cards> findAllByEmail(String email);
}

