package ru.netology.diplom.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplom.dto.CloudFileDto;
import ru.netology.diplom.entity.CloudFileEntity;
import ru.netology.diplom.entity.UserEntity;
import ru.netology.diplom.repository.CloudRepository;
import ru.netology.diplom.security.JWTToken;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudService {


    private final JWTToken jwtToken;
    private final CloudRepository cloudRepository;




    @SneakyThrows
    @Transactional()
    public boolean uploadFile(MultipartFile multipartFile, String fileName) {

        log.info("Такого файла нет, можно начинать запись {}", fileName);
        CloudFileEntity cloudFileEntity = CloudFileEntity.builder()
                .fileName(fileName)
                .size(multipartFile.getSize())
                .date(Instant.now())
                .key(UUID.randomUUID())
                .userEntity(
                        UserEntity.builder()
                                .id(jwtToken.getAuthenticatedUser().getId())
                                .login(jwtToken.getAuthenticatedUser().getLogin())
                                .build())
                .build();
        log.info("getUserEntityId есть");

        var cloudId = cloudRepository.save(cloudFileEntity).getFileId();
        if (cloudRepository.findById(cloudId).isPresent()) {
            log.info("Файл {} записан в БД под id '{}'", fileName, cloudId);
        }
        log.info("Файл записан на сервер");

        return true;
    }

    @SneakyThrows
    @Transactional()
    public boolean deleteFile(String fileName) {

        Optional<CloudFileEntity> foundFile = getCloudFileEntity(fileName);

        log.info("ищем");
        int idFoundFile = foundFile.get().getFileId();
        cloudRepository.deleteById(idFoundFile);
        log.info("Произвели удаление из БД файла:  {}", fileName);

        return true;
    }


    @SneakyThrows
    @Transactional()
    public boolean putFile(String fileName, CloudFileDto cloudFileDto) {
        var cloudFile = getCloudFileEntity(fileName);
        if (cloudFile.isEmpty()) {
            fileNotFound("Файл не удалось найти в БД");
        }
        if (getCloudFileEntity(cloudFileDto.getFileName()).isPresent()) {
            fileAlreadyExists("Такой файл существует");
        }
        cloudRepository.updateFileNameById(cloudFileDto.getFileName(), cloudFile.get().getFileId());
        if (getCloudFileEntity(cloudFileDto.getFileName()).isEmpty()) {
            fileNotFound("Не удалось переименовать файл в БД");
        }

        return true;
    }

    public List<CloudFileDto> getAllFile() {
        var cloudFileEntityList = cloudRepository.findAll();
        return cloudFileEntityList.stream()
                .map(file -> CloudFileDto.builder()
                        .fileName(file.getFileName())
                        .key(file.getKey())
                        .date(file.getDate())
                        .size(file.getSize())
                        .build())
                .collect(Collectors.toList());
    }

    private Optional<CloudFileEntity> getCloudFileEntity(String fileName) {
        int userId = jwtToken.getAuthenticatedUser().getId();
        log.info("Получаем ID пользователя по токену: {}", userId);
        log.info("Начинаем искать файл в БД: {}", fileName);
        return cloudRepository.findByUserEntity_CloudFileEntityList_FileName(fileName);
    }

    private static void fileNotFound(String msg) throws FileNotFoundException {
        log.error(msg);
        throw new FileNotFoundException(msg);
    }

    private static void fileAlreadyExists(String msg) throws FileAlreadyExistsException {
        log.error(msg);
        throw new FileAlreadyExistsException(msg);
    }
}
