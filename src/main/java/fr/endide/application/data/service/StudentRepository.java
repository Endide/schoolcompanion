package fr.endide.application.data.service;

import java.util.UUID;

import fr.endide.application.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Student findByEmail(String email);
    Student removeStudentByEmail(String email);
}

