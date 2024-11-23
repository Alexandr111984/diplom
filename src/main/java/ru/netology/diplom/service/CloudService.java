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
import ru.netology.diplom.util.CloudManager;

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
    private final CloudManager cloudManager;


    @SneakyThrows
    @Transactional()
    public boolean uploadFile(MultipartFile multipartFile, String fileName) {
        Optional<CloudFileEntity> cloudFile = getCloudFileEntity(fileName);
        if (cloudFile.isPresent()) {
            log.info("Такой файл имеется в БД.");

        }

        log.info("Такого файла нет, можно начинать запись {}", fileName);
        CloudFileEntity cloudFileEntity = CloudFileEntity.builder()
                .filename(fileName)
                .size(multipartFile.getSize())
                .date(Instant.now())
                .key(UUID.randomUUID())
                .userEntity(
                        UserEntity.builder()
                                .id(jwtToken.getAuthenticatedUser().getId())
                                .build())
                .build();

        var cloudId = cloudRepository.save(cloudFileEntity).getId();
        if (cloudRepository.findById(cloudId).isPresent()) {
            log.info("Файл {} записан в БД под id '{}'", fileName, cloudId);
        }
        if (!cloudManager.upload(multipartFile.getBytes(),
                cloudFileEntity.getKey().toString(),
                cloudFileEntity.getFilename())) {
            fileNotFound("Не получилось записать файл");
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
            var resourceFromBd = cloudFile.map(cloudManager::getFile).get();
            return CloudFileDto.builder()
                    .filename(filename)
                    .resource(resourceFromBd)
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
        if (foundFile.isEmpty()) {
            String msg = String.format("Файл %s не существует или у вас нет права доступа", filename);
            log.info(msg);
            throw new FileNotFoundException(msg);
        }
        int idFoundFile = foundFile.get().getId();
        cloudRepository.deleteById(idFoundFile);
        log.info("Произвели удаление из БД файла:  {}", filename);
        if (cloudRepository.findById(idFoundFile).isPresent()) {
            fileAlreadyExists("Файл не удалось удалить из БД");
        }
        if (!cloudManager.delete(foundFile.get())) {
            fileAlreadyExists("Файл не удалось удалить с сервера");
        }
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
        if (!cloudManager.renameFileTo(cloudFile.get(), cloudFileDto.getFilename())) {
            fileNotFound("Не удалось переименовать файл на сервере");
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
