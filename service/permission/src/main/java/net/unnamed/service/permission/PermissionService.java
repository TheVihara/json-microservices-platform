package net.unnamed.service.permission;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.extension.Extension;
import net.unnamed.service.common.PlatformService;

public class PermissionService extends PlatformService implements Extension {
    private final LuckPerms luckPerms;

    public PermissionService(LuckPerms luckPerms) {
        super(
                "Permission",
                "Manages permissions with LuckPerms integration"
        );
        this.luckPerms = luckPerms;
    }


    @Override
    public void unload() {

    }

    @Override
    public void onLoad() {

    }
}
