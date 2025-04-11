package com.example.demo.enums;

public enum Status {
    TO_DO("to do"),
    IN_PROGRESS("in progress"),
    DONE("done");

    private final String dbValue;

    Status(String dbValue) {
        this.dbValue = dbValue;
    }

    @Override
    public String toString() {
        return dbValue;
    }

    public static Status fromString(String value) {
        for (Status s : Status.values()) {
            if (s.dbValue.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
