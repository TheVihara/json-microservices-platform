package net.unnamed.service.pack.texture.factory;

import net.kyori.adventure.key.Key;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.texture.Texture;

import java.io.File;

public class TextureFactory {
    public Texture createTexture(String namespace, String value, String pngFilePath) {
        return Texture.texture()
                .key(Key.key(namespace, value))
                .data(Writable.file(new File(pngFilePath)))
                .build();
    }
}
