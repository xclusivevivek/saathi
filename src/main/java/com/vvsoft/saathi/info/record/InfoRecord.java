package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.info.schema.model.Copyable;
import lombok.Getter;
import lombok.Setter;


public class InfoRecord extends NamedEntity implements Copyable<InfoRecord> {
    @Getter
    private final RecordValue recordValue;

    public InfoRecord(String name,RecordValue recordValue) {
        super(name);
        this.recordValue = recordValue;
    }



    @Override
    public InfoRecord copy() {
        return new InfoRecord(this.getName(),recordValue.copy());
    }
}
