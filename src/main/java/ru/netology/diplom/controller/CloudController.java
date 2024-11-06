package ru.netology.diplom.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
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


    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam String fileName, @NotNull @RequestParam("file") MultipartFile multipartFile) {
        log.info("Получили файл на загрузку: {}", fileName);
        //noinspection SingleStatementInBlock
        if (cloudService.uploadFile(multipartFile, fileName)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(value = "/deleteFile", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteFile(@RequestParam String fileName) {
        log.info("Начинаем искать файл {} для удаления", fileName);
        if (cloudService.deleteFile(fileName)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @RequestMapping(value = "/putFile", method = RequestMethod.PUT)
    public ResponseEntity<Void> putFile(@RequestParam String fileName, @RequestBody CloudFileDto cloudFileDto) {
        log.info("поиск");
        if (cloudService.putFile(fileName, cloudFileDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(value = "/listFile", method = RequestMethod.GET)
    public ResponseEntity<List<CloudFileDto>> getAllFile() {
        log.info("поиск");
        var result = cloudService.getAllFile();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
