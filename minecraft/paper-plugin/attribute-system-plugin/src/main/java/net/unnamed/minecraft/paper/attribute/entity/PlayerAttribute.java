package net.unnamed.minecraft.paper.attribute.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Setter
@RequiredArgsConstructor
public class PlayerAttribute {
    String key;
    double value;
}
