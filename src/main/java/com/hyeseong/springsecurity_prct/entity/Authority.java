package com.hyeseong.springsecurity_prct.entity;

import com.hyeseong.springsecurity_prct.entity.Enum.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    private Long id;
    @Column(name ="authorityName", length = 50)
    private String authorityName;
}
