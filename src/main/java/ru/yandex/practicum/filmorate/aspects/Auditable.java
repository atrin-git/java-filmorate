package ru.yandex.practicum.filmorate.aspects;

import ru.yandex.practicum.filmorate.model.Events;
import ru.yandex.practicum.filmorate.model.Operations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    Events eventName();

    Operations operationName();

    String userId() default "";

    String entityId() default "";

    String isDeleting() default "false";
}
