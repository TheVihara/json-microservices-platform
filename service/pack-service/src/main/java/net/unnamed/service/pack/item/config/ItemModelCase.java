package net.unnamed.service.pack.item.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ItemModelCase {

    List<String> when;

    ItemModelConfig model;
}
