package io.github.hello09x.fakeplayer.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Reflections {

    public static @Nullable Field getFirstFieldByType(
            @NotNull Class<?> clazz,
            @NotNull Class<?> fieldType,
            boolean includeStatic
    ) {
        for (var field : clazz.getDeclaredFields()) {
            if (includeStatic ^ Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.getType() == fieldType) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    public static @Nullable Field getFirstFieldByAssignFromType(
            @NotNull Class<?> clazz,
            @NotNull Class<?> fieldType,
            boolean includeStatic
    ) {
        for (var field : clazz.getDeclaredFields()) {
            if (includeStatic ^ Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (fieldType.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

}
