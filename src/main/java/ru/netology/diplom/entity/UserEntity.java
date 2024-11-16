package ru.netology.diplom.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.netology.diplom.model.Role;


import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@Table(name = "UserEntity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(nullable = false)
    private String login;


    @Column(name="password")
    private String password;


////        @ElementCollection
////    @Enumerated(EnumType.STRING)
////    @Column(nullable = false, length = 15)
//    private Set<Role> roles;


    @OneToMany(mappedBy = "userEntity",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
