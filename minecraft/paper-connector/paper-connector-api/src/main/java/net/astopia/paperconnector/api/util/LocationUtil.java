package net.astopia.paperconnector.api.util;

import lombok.experimental.UtilityClass;
import net.unnamed.common.position.WorldPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
public class LocationUtil {
    public WorldPosition toWorldPosition(Location location) {
        return WorldPosition.builder()
                .worldName(location.getWorld().getName())
                .x(location.getX())
                .y(location.getY())
                .z(location.getZ())
                .yaw(location.getYaw())
                .pitch(location.getPitch())
                .build();
    }

    public Location toLocation(WorldPosition worldPosition) {
        World world = Bukkit.getWorld(worldPosition.getWorldName());

        if (world == null) {
            return null;
        }

        if (worldPosition.getYaw() != 0 || worldPosition.getPitch() != 0) {
            return new Location(world, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), worldPosition.getYaw(), worldPosition.getPitch());
        }

        return new Location(world, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
    }
}
