package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.info.schema.model.Copyable;
import lombok.Getter;
import lombok.Setter;


public class InfoRecord implements Copyable<InfoRecord> {
    @Getter
    @Setter
    private String id;
    @Getter
    private final String name;
    @Getter
    @Setter
    private RecordValue recordValue;

    public InfoRecord(String name) {
        this.name = name;
        this.id = "";
    }

    @Override
    public InfoRecord copy() {
        InfoRecord infoRecord = new InfoRecord(this.name);
        infoRecord.setRecordValue(recordValue.copy());
        return infoRecord;
    }
}
