package com.example.data.security.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    private String username;

    private String password;
    private Boolean enabled;

    @OneToMany(mappedBy = "user")
    private Set<Authority> authorities;
}
