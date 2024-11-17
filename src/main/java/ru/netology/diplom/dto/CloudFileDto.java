package ru.netology.diplom.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudFileDto {

    @JsonProperty
    private String fileName;
    private Integer userEntityId;
    private Long size;
    private Instant date;
    private UUID key;
    private byte[] resource;

    public CloudFileDto(String fileName) {
        this.fileName = fileName;
    }
    public CloudFileDto(Integer userEntityId) {
        this.userEntityId = userEntityId;
    }
}
