package fr.endide.application.data.service;

import fr.endide.application.data.entity.Student;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {

}