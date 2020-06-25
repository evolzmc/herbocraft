package com.redblueflame.herbocraft.entities.renderer;

import com.redblueflame.herbocraft.entities.SnowTurretEntity;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import com.redblueflame.herbocraft.entities.models.SnowTurretEntityModel;
import com.redblueflame.herbocraft.entities.models.TurretBaseEntityModel;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class SnowTurretRenderer extends MobEntityRenderer<SnowTurretEntity, SnowTurretEntityModel> {
    public SnowTurretRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new SnowTurretEntityModel(), 0.5f);
    }

    @Override
    public Identifier getTexture(SnowTurretEntity entity) {
        return new Identifier("herbocraft", "textures/entity/snow_turret/snow_turret.png");
    }
}
