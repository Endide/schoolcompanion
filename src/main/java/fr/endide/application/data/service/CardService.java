package fr.endide.application.data.service;

import fr.endide.application.data.entity.Cards;
import fr.endide.application.data.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {

        private final CardRepository repository;

        @Autowired
        public CardService(CardRepository repository) {
            this.repository = repository;
        }

        public Optional<Cards> get(UUID id) {
            return repository.findById(id);
        }
        public Cards getByEmail(String email) {
            return repository.findByEmail(email);
        }
        public List<Cards> getAllByEmail(String email) {
            return repository.findAllByEmail(email);
        }
        public Cards update(Cards entity) {
            return repository.save(entity);
        }

        public void delete(UUID id) {
            repository.deleteById(id);
        }

        public Page<Cards> list(Pageable pageable) {
            return repository.findAll(pageable);
        }

        public int count() {
            return (int) repository.count();
        }

}
