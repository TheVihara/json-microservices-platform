package net.unnamed.service.pack.font.adapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class TrueTypeProviderAdapter {

    String namespace;

    String value;

    float oversample;

    float shiftX;

    float shiftY;

    float size;

    List<String> skip = List.of();

    List<Double> offset = List.of();
}
