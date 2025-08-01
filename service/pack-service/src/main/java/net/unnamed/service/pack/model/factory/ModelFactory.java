package net.unnamed.service.pack.model.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.unnamed.service.pack.model.config.ModelConfig;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.model.ModelTextures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ModelFactory {
    public Model createModel(Key key, ModelConfig modelConfig) {
        return Model.model()
                .key(key)
                .parent(Key.key(modelConfig.getParent()))
                .textures(createTextures(modelConfig.getTextures()))
                .build();
    }

    public ModelTextures createTextures(Map<String, String> textures) {
        List<ModelTexture> layers = new ArrayList<>();
        ModelTexture particle = null;
        Map<String, ModelTexture> variables = new HashMap<>();

        for (Map.Entry<String, String> entry : textures.entrySet()) {
            if (entry.getKey().equals("particle")) {
                particle = ModelTexture.ofKey(Key.key(entry.getValue()));
                continue;
            }

            if (entry.getKey().startsWith("layer")) {
                layers.add(ModelTexture.ofKey(Key.key(entry.getValue())));
                continue;
            }

            variables.put(entry.getKey(), ModelTexture.ofKey(Key.key(entry.getValue())));
        }

        return ModelTextures.of(layers, particle, variables);
    }
}
