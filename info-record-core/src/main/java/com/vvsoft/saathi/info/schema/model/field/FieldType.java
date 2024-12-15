package com.vvsoft.saathi.info.schema.model.field;

public enum FieldType {
    TEXT {
        @Override
        public Object convertToActualType(String value) {
            return value;
        }

        @Override
        public Object getDefault() {
            return "";
        }
    },
    NUMBER {
        @Override
        public Object convertToActualType(String value) {
            return Long.valueOf(value);
        }

        @Override
        public Object getDefault() {
            return 0L;
        }
    },
    AMOUNT {
        @Override
        public Object convertToActualType(String value) {
            return Double.valueOf(value);
        }

        @Override
        public Object getDefault() {
            return 0.0;
        }
    };

    public abstract Object convertToActualType(String value);
    public abstract Object getDefault();
}
