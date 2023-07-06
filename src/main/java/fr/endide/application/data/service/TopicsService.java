package fr.endide.application.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.endide.application.data.entity.Topics;

@Service
public class TopicsService {

    private final TopicsRepository topicsRepository;

    @Autowired
    public TopicsService(TopicsRepository topicsRepository) {
        this.topicsRepository = topicsRepository;
    }

    public Topics get(UUID id) {
        return topicsRepository.findById(id).orElse(null);
    }

    public Topics update(Topics topics) {
        return topicsRepository.save(topics);
    }

    public List<Topics> getAllByUser(String email) {
        return new ArrayList<Topics>();
    }
}
