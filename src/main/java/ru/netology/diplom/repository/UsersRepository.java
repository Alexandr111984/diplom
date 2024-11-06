package ru.netology.diplom.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.diplom.entity.UserEntity;

import java.util.Optional;


@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByLogin(@org.hibernate.validator.constraints.NotBlank String login);


}
