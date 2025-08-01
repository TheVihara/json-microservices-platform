package net.unnamed.service.pack.factory;

import team.unnamed.creative.ResourcePack;

public class PackFactory {
    public ResourcePack createPack() {
        ResourcePack resourcePack = ResourcePack.resourcePack();
        resourcePack.packMeta(61, "Official Astopia Network Resource Pack");

        return resourcePack;
    }
}
