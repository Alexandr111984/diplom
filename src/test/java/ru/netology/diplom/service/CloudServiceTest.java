package ru.netology.diplom.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplom.details.UserPrincipal;
import ru.netology.diplom.dto.CloudFileDto;
import ru.netology.diplom.entity.CloudFileEntity;
import ru.netology.diplom.entity.UserEntity;
import ru.netology.diplom.repository.CloudRepository;
import ru.netology.diplom.security.JWTToken;
import ru.netology.diplom.util.CloudManager;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudServiceTest {

    @Mock
    CloudManager cloudManager;

    @Mock
    JWTToken jwtToken;
    @Mock
    CloudRepository cloudRepository;
    @InjectMocks
    CloudService cloudService;
    @Mock
    UserPrincipal userPrincipal;

    UserEntity userEntity;
    CloudFileEntity cloudFileEntity;
    CloudFileDto cloudFileDto;
    private final int USER_ENTITY_ID = 100;
    private final int CLOUD_FILE_ENTITY_ID = 999;
    private final String FILE_NAME = "testFile.pdf";


    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(USER_ENTITY_ID)
                .login("Egor")
                .password("321")
                .build();

        cloudFileEntity = CloudFileEntity.builder()
                .id(CLOUD_FILE_ENTITY_ID)
                .date(Instant.now())
                .size(265L)
                .key(UUID.randomUUID())
                .filename(FILE_NAME)
                .build();

        cloudFileDto = CloudFileDto.builder()
                .filename(FILE_NAME)
                .resource("TESTING".getBytes())
                .build();
    }

    @AfterEach
    void tearDown() {
        userEntity = null;
        cloudFileEntity = null;
    }

    @SneakyThrows
    @Test
    void uploadFileTest() {
        MultipartFile mf = new MockMultipartFile(
                "testFile",
                FILE_NAME,
                MediaType.TEXT_PLAIN_VALUE,
                "testUploadFile".getBytes()
        );


        when(jwtToken.getAuthenticatedUser()).thenReturn(userPrincipal);
        when(cloudManager.upload(any(),any(), any())).thenReturn(true);
        when(cloudRepository.save(any(CloudFileEntity.class))).thenReturn(cloudFileEntity);

        boolean result = cloudService.uploadFile(mf, FILE_NAME);
        Assertions.assertTrue(result);
    }




    @Test
    void getAllFile() {
        var result = cloudService.getAllFile();
        for (CloudFileDto fileDto : result) {
            Assertions.assertEquals(cloudFileDto, fileDto);
        }
    }

}