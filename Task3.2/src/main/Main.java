package main;

import main.entity.Data;
import main.propertyloader.PropertyParser;

import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Exception {
        Data data = PropertyParser.getDataFromProperty(Data.class, "resource.properties");
        if (Objects.nonNull(data)) {
            System.out.println(data.getStringValue());
            System.out.println(data.getDateTimeValue().toString());
            System.out.println(data.getIntValue());
        }
    }
}
