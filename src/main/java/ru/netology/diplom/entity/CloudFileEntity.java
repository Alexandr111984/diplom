package ru.netology.diplom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cloud_file_entity")
public class CloudFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_entity_id")
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
    @JoinColumn(name = "user_entity_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity userEntity;

    @Override
    public String toString() {
        return "CloudFileEntity{" +
                "id=" + id +
                ", userEntityId=" + userEntityId +
                ", filename='" + filename + '\'' +
                ", size=" + size +
                ", date=" + date +
                ", key=" + key +
                ", userEntity=" + userEntity +
                '}';
    }
}
