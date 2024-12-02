package ru.netology.diplom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(nullable = false)
    private String login;


    @Column(name = "password")
    private String password;


    @OneToMany(mappedBy = "userEntity")
    private List<CloudFileEntity> cloudFileEntityList;

    public UserEntity() {
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", cloudFileEntityList=" + cloudFileEntityList +
                '}';
    }
}
