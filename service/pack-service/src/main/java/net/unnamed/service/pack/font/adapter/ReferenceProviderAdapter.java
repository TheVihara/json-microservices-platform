package net.unnamed.service.pack.font.adapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ReferenceProviderAdapter {

    String namespace;

    String value;
}
