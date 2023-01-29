package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.NamedEntity;
import com.vvsoft.saathi.info.schema.model.Copyable;
import lombok.Getter;
import lombok.Setter;


public class InfoRecord extends NamedEntity implements Copyable<InfoRecord> {
    @Getter
    @Setter
    private RecordValue recordValue;

    public InfoRecord(String name) {
        super(name);
    }

    @Override
    public InfoRecord copy() {
        InfoRecord infoRecord = new InfoRecord(this.getName());
        infoRecord.setRecordValue(recordValue.copy());
        return infoRecord;
    }
}
