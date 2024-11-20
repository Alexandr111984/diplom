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
    public boolean uploadFile(MultipartFile multipartFile, String filename) {

        log.info("Такого файла нет, можно начинать запись {}", filename);
        CloudFileEntity cloudFileEntity = CloudFileEntity.builder()
                .filename(filename)
                .size(multipartFile.getSize())
                .date(Instant.now())
                .key(UUID.randomUUID())
                .userEntity(
                        UserEntity.builder()
                                .id(jwtToken.getAuthenticatedUser().getId())
                                .login(jwtToken.getAuthenticatedUser().getLogin())
                                .build())
                .build();
        log.info("id есть");

        var cloudId = cloudRepository.save(cloudFileEntity).getId();
        if (cloudRepository.findById(cloudId).isPresent()) {
            log.info("Файл {} записан в БД под id '{}'", filename, cloudId);
        }
        log.info("Файл записан на сервер");

        return true;
    }


    @SneakyThrows
    @Transactional
    public CloudFileDto getFile(String filename) {
        Optional<CloudFileEntity> cloudFile = getCloudFileEntity(filename);
        if (cloudFile.isPresent()) {
            log.info("Файл {} найден на диске", filename);
            return CloudFileDto.builder()
                    .filename(filename)
                    .build();
        } else {
            fileNotFound("Файл не удалось найди в БД");
            return null;
        }
    }

    @SneakyThrows
    @Transactional()
    public boolean deleteFile(String filename) {

        Optional<CloudFileEntity> foundFile = getCloudFileEntity(filename);

        log.info("Ищем файл.");
        int idFoundFile = foundFile.get().getId();
        cloudRepository.deleteById(idFoundFile);
        log.info("Произвели удаление из БД файла:  {}", filename);

        return true;
    }


    @SneakyThrows
    @Transactional()
    public boolean putFile(String filename, CloudFileDto cloudFileDto) {
        var cloudFile = getCloudFileEntity(filename);
        if (cloudFile.isEmpty()) {
            fileNotFound("Файл не удалось найти в БД");
        }
        if (getCloudFileEntity(cloudFileDto.getFilename()).isPresent()) {
            fileAlreadyExists("Такой файл существует");
        }
        cloudRepository.updateFilenameByUserEntityId(cloudFileDto.getFilename(), cloudFile.get().getId());
        if (getCloudFileEntity(cloudFileDto.getFilename()).isEmpty()) {
            fileNotFound("Не удалось переименовать файл в БД");
        }

        return true;
    }

    public List<CloudFileDto> getAllFile() {
        var cloudFileEntityList = cloudRepository.findAll();
        return cloudFileEntityList.stream()
                .map(file -> CloudFileDto.builder()
                        .filename(file.getFilename())
                        .key(file.getKey())
                        .date(file.getDate())
                        .size(file.getSize())
                        .build())
                .collect(Collectors.toList());
    }

    private Optional<CloudFileEntity> getCloudFileEntity(String filename) {
        int userId = jwtToken.getAuthenticatedUser().getId();
        log.info("Получаем ID пользователя по токену: {}", userId);
        log.info("Начинаем искать файл в БД: {}", filename);
        return cloudRepository.findByFilename(filename);
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
