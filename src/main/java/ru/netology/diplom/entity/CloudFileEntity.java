package ru.netology.diplom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "file_name", nullable = false)
    private String filename;

    @Column(name = "file_size")
    private Long size;

    @Column(nullable = false, name = "upload_date")
    private Instant date;


    @Column(name = "file_key")
    private UUID key;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userEntityId", insertable = false, updatable = false)
    @JsonIgnore
    private UserEntity userEntity;


}
