package com.community.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "auth_user")
@Getter
@Setter
@Entity
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user;

    @Enumerated(EnumType.STRING)
    private AuthRole auth;
}
