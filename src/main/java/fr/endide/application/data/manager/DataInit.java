package fr.endide.application.data.manager;

import fr.endide.application.data.entity.Cards;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.service.CardRepository;
import fr.endide.application.data.service.StudentRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements ApplicationRunner {
    StudentRepository repository;
    CardRepository cardRepository;

    @Autowired
    public DataInit(StudentRepository repository, CardRepository cardRepository) {
        this.repository = repository;
        this.cardRepository = cardRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (repository.count() == 0) {
            Student student = new Student();
            student.setUsername("admin");
            student.setFirstName("FirstAdmin");
            student.setLastName("LastAdmin");
            student.setEmail("admin@schoolcompanion.com");
            student.setSchoolLevel("School");
            student.setHashedPassword("$2a$12$ds/SQnIxDwin1CSSJsf9mu71XzJc8GRpmXuOFbgwsHI2J4X4MzmAa");
            student.setRoles("ADMIN");
            repository.save(student);
            Cards cards = new Cards();
            cards.setName("Trimestre 1");
            cards.setDescription("Un eleve serieux mais avec un manque de participation");
            cards.setEmail("admin@schoolcompanion.com");
            cardRepository.save(cards);
        }
    }
}
