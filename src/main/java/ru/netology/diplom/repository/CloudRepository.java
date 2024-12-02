package ru.netology.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.netology.diplom.entity.CloudFileEntity;

import java.util.Optional;

@Repository
public interface CloudRepository extends JpaRepository<CloudFileEntity, Integer> {


    @Modifying
    @Query("update CloudFileEntity c set c.filename = ?1 where c.id = ?2")
    int updateFilenameByUserEntityId(String filename, Integer id);

    @Query("select c from CloudFileEntity c where c.filename = ?1")
    Optional<CloudFileEntity> findByFilename(String filename);


}
