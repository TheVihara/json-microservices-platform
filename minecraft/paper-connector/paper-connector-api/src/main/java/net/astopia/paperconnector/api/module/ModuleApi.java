package net.astopia.paperconnector.api.module;

public interface ModuleApi {
    void registerModule(Module module);
    void unregisterModule(String id);
    void enableModule(String id);
    void disableModule(String id);
    void restartModule(String id);
}
