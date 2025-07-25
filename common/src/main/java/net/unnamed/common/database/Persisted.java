package net.unnamed.common.database;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Persisted<T> {
    T instance;
    boolean removed = false;
    boolean updated = false;
    boolean added;

    public Persisted(T instance, boolean added) {
        this.instance = instance;
        this.added = added;
    }
}
