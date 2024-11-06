package ru.netology.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.netology.diplom.entity.CloudFileEntity;

import java.util.Optional;

@Repository
public interface CloudRepository extends JpaRepository<CloudFileEntity, Integer> {



    @Query("""
            select c from CloudFileEntity c inner join c.userEntity.cloudFileEntityList cloudFileEntityList
            where cloudFileEntityList.fileName = ?1""")
    Optional<CloudFileEntity> findByUserEntity_CloudFileEntityList_FileName(String fileName);


    @Modifying
    @Query("update CloudFileEntity c set c.fileName = ?1 where c.id = ?2")
    void updateFileNameById(String fileName, Integer id);


}
