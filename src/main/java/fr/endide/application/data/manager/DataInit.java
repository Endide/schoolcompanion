package fr.endide.application.data.manager;

import fr.endide.application.data.entity.Student;
import fr.endide.application.data.service.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements ApplicationRunner {
    StudentRepository repository;

    @Autowired
    public DataInit(StudentRepository repository) {
        this.repository = repository;
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
            student.setHashedPassword("$2a$12$QkcsWXBzjfXn/x/vmkudceaYOu2YBFcpRuXubIPd6iJFamHNEJNki");
            student.setRoles("ADMIN");
            repository.save(student);
        }
    }
}
