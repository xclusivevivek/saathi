package com.vvsoft.saathi.info.record.crud.api.controller;

import com.vvsoft.saathi.info.record.model.InfoRecord;
import com.vvsoft.saathi.info.record.presentation.InfoRecordDetailDto;
import com.vvsoft.saathi.info.record.presentation.InfoRecordDto;
import com.vvsoft.saathi.info.record.crud.InfoRecordCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/inforecord")
@Slf4j
public class InfoRecordController {

    private final InfoRecordCrudService infoRecordCrudService;

    @Autowired
    public InfoRecordController(InfoRecordCrudService infoRecordCrudService) {
        this.infoRecordCrudService = infoRecordCrudService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InfoRecordDetailDto> getAll(){
        List<InfoRecord> infoRecords = infoRecordCrudService.getAll();
        return infoRecords.stream().map(InfoRecordDetailDto::from).collect(Collectors.toList());
    }

    @PostMapping(path = "create")
    @ResponseStatus(HttpStatus.CREATED)
    public InfoRecordDto createInfoRecord(@RequestBody InfoRecordDto dto){
        log.info("Processing InfoRecord creation request:{}",dto);
        infoRecordCrudService.create(dto);
        return dto;
    }

    @GetMapping(path = "get")
    public ResponseEntity<InfoRecordDetailDto> getInfoRecord(@RequestParam("name") String recordName){
        Optional<InfoRecord> maybeInfoRecord = infoRecordCrudService.read(recordName);
        return maybeInfoRecord.map(infoRecord -> ResponseEntity.ok(InfoRecordDetailDto.from(infoRecord))).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping(path = "update")
    @ResponseStatus(HttpStatus.OK)
    public InfoRecordDetailDto updateInfoRecord(@RequestBody InfoRecordDto dto){
        log.info("Processing InfoRecord update request:{}",dto);
        InfoRecord updatedRecord = infoRecordCrudService.update(dto);
        return InfoRecordDetailDto.from(updatedRecord);
    }

    @DeleteMapping(path = "delete")
    public ResponseEntity<Object> deleteInfoRecord(@RequestParam("name") String recordName){
        log.info("Processing InfoRecord delete request:{}",recordName);
        infoRecordCrudService.delete(recordName);
        return ResponseEntity.noContent().build();
    }

}
