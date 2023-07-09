package fr.endide.application.data.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.endide.application.data.entity.Topics;

@Repository
public interface TopicsRepository extends JpaRepository<Topics, UUID> {
    List<Topics> findAll();


    Topics getTopicsById(String name);

}
