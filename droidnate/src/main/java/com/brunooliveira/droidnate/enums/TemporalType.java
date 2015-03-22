package com.brunooliveira.droidnate.enums;

/**
 * Created by Bruno on 20/03/2015.
 */
public enum TemporalType {

    DATE("DATE"), TIME("TIME"), DATETIME("DATETIME"), TIMESTAMP("TIMESTAMP");

    private String value;

    private TemporalType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}