package com.hyeseong.springsecurity_prct.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyeseong.springsecurity_prct.entity.Enum.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail;

    @JsonIgnore
    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private String userName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="roleId", referencedColumnName = "id")}
    )
    private Set<Authority> authorities;




    @Builder
    public User(String userPassword, String userName, String userEmail, Set<Authority> authorities){
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmail = userEmail;
        this.authorities = authorities;
    }
}
