package org.example;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static void validate(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (field.isAnnotationPresent(Email.class)) {
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException("Field '" + field.getName() + "' must be a String");
                }
                if (!EMAIL_PATTERN.matcher((String) value).matches()) {
                    throw new IllegalArgumentException("Field '" + field.getName() + "' must be a valid email address");
                }
            }
        }
    }
}
