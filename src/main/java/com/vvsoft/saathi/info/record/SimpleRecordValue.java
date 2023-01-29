package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.info.schema.model.SimpleSchema;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class SimpleRecordValue implements RecordValue{
    private final Map<String,Object> values = new HashMap<>();
    private final SimpleSchema infoSchema;

    private SimpleRecordValue(SimpleSchema infoSchema) {
        this.infoSchema = infoSchema;
    }

    public Map<String,Object> getValues(){
        return values;
    }

    public static SimpleRecordValueBuilder builder(SimpleSchema simpleSchema){
        return new SimpleRecordValueBuilder(simpleSchema);
    }

    @Override
    public SimpleRecordValue copy() {
        SimpleRecordValue recordValue = new SimpleRecordValue(infoSchema.copy());
        recordValue.getValues().putAll(values);
        return recordValue;
    }

    public static class SimpleRecordValueBuilder {
        private final Collection<SimpleField> fields;
        private final Map<String,Object> values = new HashMap<>();
        private final SimpleSchema schema;

        SimpleRecordValueBuilder(SimpleSchema simpleSchema) {
            this.fields = simpleSchema.getFields();
            this.schema = simpleSchema;
            fields.forEach(field -> values.put(field.getKey(),getDefaultValue(field)));
        }

        public SimpleRecordValueBuilder addValue(String key, String value) throws NoSuchFieldException {
            Optional<SimpleField> fieldFound = fields.stream().filter(field -> field.getKey().equals(key)).findFirst();
            if(fieldFound.isEmpty())
                throw new NoSuchFieldException("Field not found:" + key);

            switch (fieldFound.get().getFieldType()) {
                case TEXT:
                    values.put(key, value);
                    break;
                case NUMBER:
                    values.put(key, Long.valueOf(value));
                    break;
                case AMOUNT:
                    values.put(key, Double.valueOf(value));
            }
            return this;
        }

        private Object getDefaultValue(SimpleField field){
            switch (field.getFieldType()) {
                case TEXT:
                    return "";
                case NUMBER:
                    return 0L;
                case AMOUNT:
                    return 0.0;
            }
            return null;
        }


        public SimpleRecordValue build(){
            SimpleRecordValue simpleRecordValue = new SimpleRecordValue(schema);
            simpleRecordValue.getValues().putAll(values);
            return simpleRecordValue;
        }

    }
}
