package ru.netology.diplom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "cloud_file_entity")
public class CloudFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userEntityId", unique = true)
    private Integer userEntityId;

    @Column(name = "file_Name",  nullable = false)
    private String fileName;

    @Column(name = "file_size")
    private Long size;

    @Column(nullable = false, name = "upload_date")
    private Instant date;

//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
    //)

    @Column(name = "file_key")
    private UUID key;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userEntityId",insertable=false,updatable=false)
    @JsonIgnore
    private UserEntity userEntity;


}
