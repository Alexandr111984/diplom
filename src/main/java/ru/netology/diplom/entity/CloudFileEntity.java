package ru.netology.diplom.entity;

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
//@ToString
@Table(name = "cloud_file_entity")
public class CloudFileEntity {
    @Id
    @GeneratedValue
    private Integer id;


    @Column(name = "file_Name",  nullable = false)
    private String fileName;

    @Column(name = "file_size")
    private Long size;

    @Column(nullable = false, name = "upload_date")
    private Instant date;

    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(unique = true, name = "file_key")
    private UUID key;


    @ManyToOne
    @JoinColumn(name = "id",insertable=false,updatable=false)
    private UserEntity userEntity;

    @Override
    public String toString() {
        return "CloudFileEntity{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                ", date=" + date +
                ", key=" + key +
                ", userEntity=" + userEntity +
                '}';
    }
}
