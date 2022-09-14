package fr.endide.application.security;

import fr.endide.application.data.entity.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class StudentUserDetails implements UserDetails {

    private Student student;

    public StudentUserDetails(Student student) {
        this.student = student;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + student.getRoles());
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return student.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return student.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
