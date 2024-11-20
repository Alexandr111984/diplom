package ru.netology.diplom.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplom.dto.CloudFileDto;
import ru.netology.diplom.service.CloudService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor


public class CloudController {
    private final CloudService cloudService;


    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam String filename, @NotNull @RequestParam("file") MultipartFile multipartFile) {
        log.info("Получили файл на загрузку: {}", filename);
        //noinspection SingleStatementInBlock
        if (cloudService.uploadFile(multipartFile, filename)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public ResponseEntity<String> getFile(@RequestParam String filename) {
        CloudFileDto cloudFileDto = cloudService.getFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cloudFileDto.getFilename() + "\"")
                .body(cloudFileDto.getFilename());
    }


    @RequestMapping(value = "/file", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteFile(@RequestParam String filename) {
        log.info("Начинаем искать файл {} для удаления", filename);
        if (cloudService.deleteFile(filename)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public ResponseEntity<Void> putFile(@RequestParam String filename, @RequestBody CloudFileDto cloudFileDto) {
        log.info("поиск");
        if (cloudService.putFile(filename, cloudFileDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<CloudFileDto>> getAllFile() {
        log.info("поиск");
        var result = cloudService.getAllFile();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
