package com.redblueflame.herbocraft;

import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.components.TurretLevelComponent;
import com.redblueflame.herbocraft.entities.BulletEntity;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HerboCraft implements ModInitializer {
    /**
     * Registers the turret with the ID "herbocraft:turret_base"
     *
     * This entity is the standard turret, that shoots projectiles
     */
    //region Entities
    public static final EntityType<TurretBaseEntity> TURRET_BASE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("herbocraft", "turret_base"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TurretBaseEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<BulletEntity> BULLET = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("herbocraft", "bullet"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BulletEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    //endregion
    //region Components
    public static final ComponentType<LevelComponent> LEVELLING =
            ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier("herbocraft", "levelling"), LevelComponent.class);
    //endregion
    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(TURRET_BASE, TurretBaseEntity.createBaseTurretAttributes());

        // Register turret Levelling system
        EntityComponentCallback.event(TurretBaseEntity.class).register((turret, components) -> components.put(LEVELLING, TurretLevelComponent.getRandomStats((short) 5)));
    }

}
