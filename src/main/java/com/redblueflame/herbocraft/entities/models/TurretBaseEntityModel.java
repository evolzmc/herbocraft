package com.redblueflame.herbocraft.entities.models;

import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class TurretBaseEntityModel extends EntityModel<TurretBaseEntity> {
    // Used for testing
    private final ModelPart Base;
    private final ModelPart Tige;
    private final ModelPart FeuilleO;
    private final ModelPart FeuilleE;
    private final ModelPart FeuilleN;
    private final ModelPart FeuilleS;
    private final ModelPart Socle;
    private final ModelPart tete;
    private final ModelPart head;
    private final ModelPart Canon;
    private final ModelPart Embout;
    private final ModelPart bords;
    private final ModelPart Yeux;
    public TurretBaseEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        Base = new ModelPart(this);
        Base.setPivot(0.0F, 24.0F, 0.0F);


        Tige = new ModelPart(this);
        Tige.setPivot(0.0F, 0.0F, 0.0F);
        Base.addChild(Tige);
        Tige.addCuboid("Tige", -1.0F, -7.0F, -1.0F, 2, 7, 2, 0.0F, 0, 0);

        FeuilleO = new ModelPart(this);
        FeuilleO.setPivot(0.0F, 0.0F, 0.0F);
        Tige.addChild(FeuilleO);
        FeuilleO.addCuboid("FeuilleO", 2.0F, -1.0F, -0.5F, 1, 1, 1, 0.0F, 18, 18);
        FeuilleO.addCuboid("FeuilleO", 3.0F, -1.0F, -2.0F, 4, 1, 4, 0.0F, 18, 16);
        FeuilleO.addCuboid("FeuilleO", 3.0F, -1.0F, -2.0F, 4, 1, 4, 0.0F, 18, 16);

        FeuilleE = new ModelPart(this);
        FeuilleE.setPivot(0.0F, 0.0F, 0.0F);
        Tige.addChild(FeuilleE);
        setRotationAngle(FeuilleE, 0.0F, 3.1416F, 0.0F);
        FeuilleE.addCuboid("FeuilleE", 2.0F, -1.0F, -0.5F, 1, 1, 1, 0.0F, 18, 18);
        FeuilleE.addCuboid("FeuilleE", 3.0F, -1.0F, -2.0F, 4, 1, 4, 0.0F, 18, 16);
        FeuilleE.addCuboid("FeuilleE", 3.0F, -1.0F, -2.0F, 4, 1, 4, 0.0F, 18, 16);

        FeuilleN = new ModelPart(this);
        FeuilleN.setPivot(0.0F, 0.0F, 0.0F);
        Tige.addChild(FeuilleN);
        setRotationAngle(FeuilleN, 0.0F, 1.5708F, 0.0F);
        FeuilleN.addCuboid("FeuilleN", 2.0F, -1.0F, -0.5F, 1, 1, 1, 0.0F, 18, 18);
        FeuilleN.addCuboid("FeuilleN", 3.0F, -1.0F, -2.0F, 4, 1, 4, 0.0F, 18, 16);

        FeuilleS = new ModelPart(this);
        FeuilleS.setPivot(0.0F, 0.0F, 0.0F);
        Tige.addChild(FeuilleS);
        setRotationAngle(FeuilleS, 0.0F, -1.5708F, 0.0F);
        FeuilleS.addCuboid("FeuilleS", 2.0F, -1.0F, -0.5F, 1, 1, 1, 0.0F, 18, 18);
        FeuilleS.addCuboid("FeuilleS", 3.0F, -1.0F, -2.0F, 4, 1, 4, 0.0F, 18, 16);

        Socle = new ModelPart(this);
        Socle.setPivot(0.5F, 0.0F, 1.0F);
        Tige.addChild(Socle);
        Socle.addCuboid("Socle", -2.5F, -2.0F, -2.5F, 1, 2, 3, 0.0F, 8, 23);
        Socle.addCuboid("Socle", 0.5F, -2.0F, -2.5F, 1, 2, 3, 0.0F, 8, 23);
        Socle.addCuboid("Socle", -2.0F, -2.0F, 0.0F, 3, 2, 1, 0.0F, 13, 23);
        Socle.addCuboid("Socle", -2.0F, -2.0F, -3.0F, 3, 2, 1, 0.0F, 13, 23);

        tete = new ModelPart(this);
        tete.setPivot(0.0F, -7.0F, 0.0F);
        Base.addChild(tete);


        Canon = new ModelPart(this);
        Canon.setPivot(0.0F, 7.0F, 0.0F);
        tete.addChild(Canon);


        Embout = new ModelPart(this);
        Embout.setPivot(-4.0F, -11.0F, 0.0F);
        Canon.addChild(Embout);
        Embout.addCuboid("Embout", -3.0F, -1.5F, -1.5F, 3, 3, 3, 0.0F, 0, 23);

        bords = new ModelPart(this);
        bords.setPivot(-2.0F, 0.0F, 0.0F);
        Embout.addChild(bords);
        bords.addCuboid("bords", -2.0F, -2.0F, -3.0F, 1, 4, 2, 0.0F, 0, 16);
        bords.addCuboid("bords", -2.0F, -3.0F, -2.0F, 1, 2, 4, 0.0F, 20, 21);
        bords.addCuboid("bords", -2.0F, 1.0F, -2.0F, 1, 2, 4, 0.0F, 20, 21);
        bords.addCuboid("bords", -2.0F, -2.0F, 1.0F, 1, 4, 2, 0.0F, 0, 16);

        Yeux = new ModelPart(this);
        Yeux.setPivot(0.0F, 7.0F, 0.0F);
        tete.addChild(Yeux);
        Yeux.addCuboid("Yeux", -5.0F, -15.0F, 1.0F, 1, 2, 2, 0.0F, 24, 0);
        Yeux.addCuboid("Yeux", -5.0F, -15.0F, -3.0F, 1, 2, 2, 0.0F, 24, 0);

        head = new ModelPart(this);
        head.setPivot(0.0F, 0.0F, 0.0F);
        tete.addChild(head);
        head.addCuboid("head", -3.0F, -1.0F, -3.0F, 6, 1, 6, 0.0F, 0, 16);
        head.addCuboid("head", -4.0F, -9.0F, -4.0F, 8, 8, 8, 0.0F, 0, 0);
    }
    @Override
    public void setAngles(TurretBaseEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        tete.yaw = (headYaw * 0.017453292F) - 1.570796F;
        tete.roll = (headPitch * 0.017453292F);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        // translate model down
        matrices.translate(0, 0, 0);
        // render cube
        Base.render(matrices, vertices, light, overlay);
    }
    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pitch = x;
        modelRenderer.yaw = y;
        modelRenderer.roll = z;
    }
}
