package net.unnamed.service.command.api;

import java.util.List;

public class CommandInfo {
    private String name;
    private List<String> aliases;
    private String description;
    private String service;
    boolean enabled = true;

    public CommandInfo() {
    }

    public CommandInfo(String name, List<String> aliases, String description, String service, boolean enabled) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.service = service;
        this.enabled = enabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getService() {
        return service;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
