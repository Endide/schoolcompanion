package fr.endide.application.security;

import fr.endide.application.data.entity.Student;
import fr.endide.application.data.service.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    StudentRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Student student = repository.findByEmail(email);

        if (student == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new StudentUserDetails(student);
    }

}
