package io.github.hello09x.fakeplayer.entity;

import org.bukkit.Location;

/**
 * @param spawnAt      出生点
 * @param invulnerable 是否无敌
 * @param collidable   是否开启碰撞
 * @param lookAtEntity 是否看向附近实体
 * @param pickupItems  是否拾取物品
 */
public record SpawnOption(
        Location spawnAt,
        boolean invulnerable,
        boolean collidable,
        boolean lookAtEntity,
        boolean pickupItems
) {
}
