package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.info.schema.model.InfoSchema;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class SimpleRecordValue implements RecordValue{
    private final Map<String,Object> values = new HashMap<>();
    private final InfoSchema infoSchema;

    private SimpleRecordValue(InfoSchema infoSchema) {
        this.infoSchema = infoSchema;
    }

    public Map<String,Object> getValues(){
        return new HashMap<>(values);
    }

    void setValues(Map<String,Object> values){
        this.values.putAll(values);
    }

    @Override
    public Optional<Object> getValue(String field){
        if(values.containsKey(field)){
            return Optional.of(values.get(field));
        }
        return Optional.empty();
    }

    @Override
    public void update(String field, String value) throws NoSuchFieldException {
        Optional<SimpleField> fieldFound = infoSchema.getFieldByKey(field);
        if(fieldFound.isEmpty())
            throw new NoSuchFieldException("Field not found:" + field);
        values.put(field,fieldFound.get().getFieldType().convertToActualType(value));
    }

    public static SimpleRecordValueBuilder builder(InfoSchema infoSchema){
        return new SimpleRecordValueBuilder(infoSchema);
    }

    @Override
    public SimpleRecordValue copy() {
        SimpleRecordValue recordValue = new SimpleRecordValue(infoSchema.copy());
        recordValue.setValues(values);
        return recordValue;
    }

    public static class SimpleRecordValueBuilder {
        private final Map<String,Object> values = new HashMap<>();
        private final InfoSchema schema;

        SimpleRecordValueBuilder(InfoSchema infoSchema) {
            this.schema = infoSchema;
            Collection<SimpleField> fields = infoSchema.getFields();
            fields.forEach(field -> values.put(field.getKey(),getDefaultValue(field)));
        }

        public SimpleRecordValueBuilder addValue(String key, String value) throws NoSuchFieldException {
            Optional<SimpleField> fieldFound = schema.getFieldByKey(key);
            if(fieldFound.isEmpty())
                throw new NoSuchFieldException("Field not found:" + key);
            values.put(key,fieldFound.get().getFieldType().convertToActualType(value));
            return this;
        }

        private Object getDefaultValue(SimpleField field){
            return field.getFieldType().getDefault();
        }


        public SimpleRecordValue build(){
            SimpleRecordValue simpleRecordValue = new SimpleRecordValue(schema);
            simpleRecordValue.setValues(values);
            return simpleRecordValue;
        }

    }
}
