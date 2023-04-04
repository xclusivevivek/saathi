package com.vvsoft.saathi.info.record.controller;

import com.vvsoft.saathi.info.record.dto.InfoRecordDto;
import com.vvsoft.saathi.info.record.service.InfoRecordCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/inforecord")
@Slf4j
public class InfoRecordController {

    private final InfoRecordCrudService infoRecordCrudService;

    @Autowired
    public InfoRecordController(InfoRecordCrudService infoRecordCrudService) {
        this.infoRecordCrudService = infoRecordCrudService;
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    public InfoRecordDto createInfoRecord(@RequestBody InfoRecordDto dto){
        log.info("Processing InfoRecord creation request:{}",dto);
        infoRecordCrudService.create(dto);
        return dto;
    }
}
