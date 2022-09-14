package fr.endide.application.data.manager;

import fr.endide.application.data.Role;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.service.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
            Set<Role> roles = new HashSet<Role>(10, (float) 0.8);
            roles.add(Role.ADMIN);
            student.setRoles(roles);
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("icon/avatar_default.png").getFile());
            byte[] fileContent = Files.readAllBytes(file.toPath());
            student.setProfilePicture(fileContent);
            repository.save(student);
        }
    }
}
