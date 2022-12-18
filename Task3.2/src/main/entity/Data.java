package main.entity;

import main.annotation.Property;

import java.time.LocalDateTime;

public class Data {

    private String stringValue;

    @Property(format = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTimeValue;

    @Property(name = "test")
    private int intValue;

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public LocalDateTime getDateTimeValue() {
        return dateTimeValue;
    }

    public void setDateTimeValue(LocalDateTime dateTimeValue) {
        this.dateTimeValue = dateTimeValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

}
