package main.propertyloader;

import main.annotation.Property;
import main.entity.Data;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;
import java.beans.PropertyDescriptor;

public class PropertyParser {

    public static <T>T getDataFromProperty(Class<T> cls, String path) throws Exception {
        if (cls.equals(Data.class)) {
            try {
                T t = cls.getDeclaredConstructor().newInstance();
                InputStream inputStream = cls.getClassLoader().getResourceAsStream(path);
                if (Objects.nonNull(inputStream)) {
                    Properties properties = new Properties();
                    properties.load(inputStream);

                    for (Field field : cls.getDeclaredFields()) {
                        Property property = field.getDeclaredAnnotation(Property.class);
                        Object value;
                        String format = null;
                        String name = field.getName();
                        if (Objects.nonNull(property)) {
                            if (!property.name().isEmpty()) {
                                name = property.name();
                            } else
                            format = property.format();
                        }
                        if (Objects.nonNull(format) && !format.isEmpty()) {
                            value = LocalDateTime.parse(properties.getProperty(name),
                                    DateTimeFormatter.ofPattern(format));
                        } else if (field.getType().equals(int.class)) {
                            value = Integer.parseInt(properties.getProperty(name));
                        } else {
                            value = properties.getProperty(name);
                        }
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), cls);
                        propertyDescriptor.getWriteMethod().invoke(t, value);
                    }
                    inputStream.close();
                }
                return t;
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                    | InvocationTargetException | IOException | IntrospectionException e) {
                throw new Exception("Failed to get data from property file");
            }

        }
        return null;
    }
}
