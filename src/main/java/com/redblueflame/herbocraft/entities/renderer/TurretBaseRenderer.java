package com.redblueflame.herbocraft.entities.renderer;

import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import com.redblueflame.herbocraft.entities.models.TurretBaseEntityModel;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class TurretBaseRenderer extends MobEntityRenderer<TurretBaseEntity, TurretBaseEntityModel> {

    public TurretBaseRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new TurretBaseEntityModel(), 0.5f);
    }

    @Override
    public Identifier getTexture(TurretBaseEntity entity) {
        return new Identifier("herbocraft", "textures/entity/base_turret/base_turret.png");
    }
}