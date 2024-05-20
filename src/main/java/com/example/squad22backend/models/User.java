package com.example.squad22backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Entity(name = "user_table")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user_table")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 40)
    private String id;

    @Column(name = "username", nullable = false, unique = true, length = 32)
    private String username;

    @Column(name = "full_name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registration_info", unique = true)
    private int registration;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private UserRole accountType;

    @Column(name = "activity_status", nullable = false, length = 16)
    private String activityStatus;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.accountType == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

