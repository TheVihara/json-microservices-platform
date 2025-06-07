package net.unnamed.service.common;

import net.unnamed.common.ClassInstance;

public interface Service extends ClassInstance {
    String getName();
    String getDescription();
    boolean isEnabled();
    void load();
}
