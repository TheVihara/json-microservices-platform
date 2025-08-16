package net.unnamed.service.pack.font.adapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class BitmapProviderAdapter {

    String key;

    String namespace;

    String value;

    int height;

    int width;

    int ascent;

    List<String> characters;

    List<Integer> offset;
}
