package com.redblueflame.herbocraft.entities.renderer;

import com.redblueflame.herbocraft.entities.SnowTurretEntity;
import com.redblueflame.herbocraft.entities.WitherTurretEntity;
import com.redblueflame.herbocraft.entities.models.SnowTurretEntityModel;
import com.redblueflame.herbocraft.entities.models.WitherTurretEntityModel;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class WitherTurretRenderer extends MobEntityRenderer<WitherTurretEntity, WitherTurretEntityModel> {

    public WitherTurretRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new WitherTurretEntityModel(), 0.5f);
    }

    @Override
    public Identifier getTexture(WitherTurretEntity entity) {
        return new Identifier("herbocraft", "textures/entity/wither_turret/wither_turret.png");
    }
}