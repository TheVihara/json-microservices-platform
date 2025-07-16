package net.unnamed.service.pack.model.factory;

import net.kyori.adventure.key.Key;
import team.unnamed.creative.model.Model;

public class ModelFactory {
    public Model createModel(String namespace, String value) {
        return Model.model().key(Key.key(namespace, value)).build();
    }
}
