package com.vvsoft.saathi.info.record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.info.schema.model.Copyable;
import lombok.Getter;

public class InfoRecord extends NamedEntity implements Copyable<InfoRecord> {
    @Getter
    private final RecordValue recordValue;

    @JsonCreator
    public InfoRecord(@JsonProperty("name") String name, @JsonProperty("recordValue") RecordValue recordValue) {
        super(name);
        this.recordValue = recordValue;
    }

    @Override
    public InfoRecord copy() {
        InfoRecord infoRecord = new InfoRecord(this.getName(), recordValue.copy());
        infoRecord.setId(this.getId());
        return infoRecord;
    }
}
