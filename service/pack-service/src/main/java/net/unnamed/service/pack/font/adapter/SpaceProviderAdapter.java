package net.unnamed.service.pack.font.adapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class SpaceProviderAdapter {

    Map<String, Integer> advances;
}
