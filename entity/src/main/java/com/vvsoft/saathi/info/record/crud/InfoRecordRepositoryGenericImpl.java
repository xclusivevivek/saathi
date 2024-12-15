package com.vvsoft.saathi.info.record.crud;

import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.info.record.model.InfoRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class InfoRecordRepositoryGenericImpl implements InfoRecordRepository{
    private final GenericDao<InfoRecord> dao;

    public InfoRecordRepositoryGenericImpl(GenericDao<InfoRecord> dao){
        this.dao = dao;
    }

    @Override
    public InfoRecord create(InfoRecord infoRecord) {
        InfoRecord result = dao.create(infoRecord);
        log.info("Created InfoRecord : {}",result.getName());
        return result;
    }

    @Override
    public Optional<InfoRecord> find(String recordName) {
        return dao.read(recordName);
    }

    @Override
    public void update(InfoRecord infoRecord) {
        dao.update(infoRecord);
        log.info("Updated InfoRecord : {}",infoRecord.getName());
    }

    @Override
    public void delete(String recordName) {
        dao.delete(recordName);
        log.info("Deleted InfoRecord : {}",recordName);
    }

    @Override
    public List<InfoRecord> findAll() {
        return dao.getAll();
    }
}
