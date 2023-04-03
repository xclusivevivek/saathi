package com.vvsoft.saathi.info.record;

import java.util.Optional;

public interface InfoRecordRepository {
    InfoRecord create(InfoRecord infoRecord);
    Optional<InfoRecord> find(String recordName);
    void update(InfoRecord result);
    void delete(String recordName);
}
