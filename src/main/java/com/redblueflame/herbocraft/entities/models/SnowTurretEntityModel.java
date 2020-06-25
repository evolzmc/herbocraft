package com.redblueflame.herbocraft.entities.models;

import com.redblueflame.herbocraft.entities.SnowTurretEntity;
import com.redblueflame.herbocraft.entities.TurretBaseEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class SnowTurretEntityModel extends EntityModel<SnowTurretEntity> {
    private final ModelPart Base;
    private final ModelPart Tige;
    private final ModelPart FeuilleO;
    private final ModelPart FeuilleE;
    private final ModelPart FeuilleN;
    private final ModelPart FeuilleS;
    private final ModelPart Socle;
    private final ModelPart Head;
    private final ModelPart tete;
    private final ModelPart corne;
    private final ModelPart cristal;
    private final ModelPart cristal9;
    private final ModelPart cristal8;
    private final ModelPart cristal6;
    private final ModelPart cristal7;
    private final ModelPart cristal5;
    private final ModelPart cristal4;
    private final ModelPart cristal3;
    private final ModelPart cristal2;
    private final ModelPart Canon;
    private final ModelPart Embout;
    private final ModelPart bords;
    private final ModelPart Yeux;

    public SnowTurretEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        Base = new ModelPart(this);
        Base.setPivot(0.0F, 24.0F, 0.0F);
        setRotationAngle(Base, 0, 1.570796F, 0);

        Tige = new ModelPart(this);
        Tige.setPivot(0.0F, 0.0F, 0.0F);
        Base.addChild(Tige);
        Tige.setTextureOffset(12, 31).addCuboid(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);

        FeuilleO = new ModelPart(this);
        FeuilleO.setPivot(0.0F, 0.0F, 0.0F);
        Tige.addChild(FeuilleO);
        FeuilleO.setTextureOffset(30, 5).addCuboid(2.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        FeuilleO.setTextureOffset(12, 26).addCuboid(3.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        FeuilleE = new ModelPart(this);
        FeuilleE.setPivot(0.0F, 0.0F, 0.0F);
        Tige.addChild(FeuilleE);
        setRotationAngle(FeuilleE, 0.0F, 3.1416F, 0.0F);
        FeuilleE.setTextureOffset(24, 28).addCuboid(2.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        FeuilleE.setTextureOffset(0, 23).addCuboid(3.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        FeuilleN = new ModelPart(this);
        FeuilleN.setPivot(0.0F, 0.0F, 0.0F);
        Tige.addChild(FeuilleN);
        setRotationAngle(FeuilleN, 0.0F, 1.5708F, 0.0F);
        FeuilleN.setTextureOffset(9, 28).addCuboid(2.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        FeuilleN.setTextureOffset(20, 21).addCuboid(3.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        FeuilleS = new ModelPart(this);
        FeuilleS.setPivot(0.0F, 0.0F, 0.0F);
        Tige.addChild(FeuilleS);
        setRotationAngle(FeuilleS, 0.0F, -1.5708F, 0.0F);
        FeuilleS.setTextureOffset(27, 6).addCuboid(2.0F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        FeuilleS.setTextureOffset(18, 16).addCuboid(3.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        Socle = new ModelPart(this);
        Socle.setPivot(0.5F, 0.0F, 1.0F);
        Tige.addChild(Socle);
        Socle.setTextureOffset(27, 36).addCuboid(-2.5F, -2.0F, -2.5F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        Socle.setTextureOffset(0, 0).addCuboid(0.5F, -2.0F, -2.5F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        Socle.setTextureOffset(12, 23).addCuboid(-2.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        Socle.setTextureOffset(0, 5).addCuboid(-2.0F, -2.0F, -3.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        Head = new ModelPart(this);
        Head.setPivot(0.0F, -7.0F, 0.0F);
        Base.addChild(Head);


        tete = new ModelPart(this);
        tete.setPivot(-4.0F, -4.0F, 0.0F);
        Head.addChild(tete);
        setRotationAngle(tete, 0.0F, 3.1416F, 0.0F);
        tete.setTextureOffset(0, 16).addCuboid(-7.0F, 3.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);
        tete.setTextureOffset(0, 0).addCuboid(-8.0F, -5.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        corne = new ModelPart(this);
        corne.setPivot(0.0F, 0.0F, 0.0F);
        tete.addChild(corne);


        cristal = new ModelPart(this);
        cristal.setPivot(0.0F, 0.0F, 0.0F);
        corne.addChild(cristal);
        cristal.setTextureOffset(32, 5).addCuboid(-11.0F, -2.0F, -1.0001F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        cristal.setTextureOffset(24, 26).addCuboid(-12.0F, -1.5F, -0.5001F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cristal9 = new ModelPart(this);
        cristal9.setPivot(1.0F, 0.0F, 0.0F);
        corne.addChild(cristal9);
        setRotationAngle(cristal9, 0.0F, 0.0F, -0.2618F);
        cristal9.setTextureOffset(26, 32).addCuboid(-10.517F, -1.8706F, -1.0001F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        cristal9.setTextureOffset(18, 18).addCuboid(-12.0F, -1.5F, -0.5001F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cristal8 = new ModelPart(this);
        cristal8.setPivot(1.0F, 0.75F, 0.0F);
        corne.addChild(cristal8);
        setRotationAngle(cristal8, 0.0F, 0.0F, 0.3491F);
        cristal8.setTextureOffset(32, 19).addCuboid(-11.2349F, -1.9145F, -1.0001F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        cristal8.setTextureOffset(18, 16).addCuboid(-12.0F, -1.5F, -0.5001F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cristal6 = new ModelPart(this);
        cristal6.setPivot(0.5F, 0.0F, 1.5F);
        corne.addChild(cristal6);
        setRotationAngle(cristal6, 0.0F, -0.4363F, 0.0F);
        cristal6.setTextureOffset(34, 34).addCuboid(-11.0F, -2.0F, -1.0001F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        cristal6.setTextureOffset(20, 23).addCuboid(-12.0F, -1.5F, -0.5001F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cristal7 = new ModelPart(this);
        cristal7.setPivot(0.5F, 0.0F, -1.5F);
        corne.addChild(cristal7);
        setRotationAngle(cristal7, 0.0F, 0.4363F, 0.0F);
        cristal7.setTextureOffset(0, 34).addCuboid(-11.0F, -2.0F, -1.0001F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        cristal7.setTextureOffset(0, 23).addCuboid(-12.0F, -1.5F, -0.5001F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cristal5 = new ModelPart(this);
        cristal5.setPivot(0.25F, 1.75F, 1.0F);
        corne.addChild(cristal5);
        setRotationAngle(cristal5, 0.0F, 0.2618F, -0.1745F);
        cristal5.setTextureOffset(36, 0).addCuboid(-11.0F, -2.0F, -1.0001F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        cristal5.setTextureOffset(24, 0).addCuboid(-12.0F, -1.5F, -0.5001F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cristal4 = new ModelPart(this);
        cristal4.setPivot(0.0F, -1.75F, 1.0F);
        corne.addChild(cristal4);
        setRotationAngle(cristal4, 0.0F, 0.2618F, 0.1745F);
        cristal4.setTextureOffset(36, 9).addCuboid(-11.0F, -2.0F, -1.0001F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        cristal4.setTextureOffset(24, 2).addCuboid(-12.0F, -1.5F, -0.5001F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cristal3 = new ModelPart(this);
        cristal3.setPivot(0.0F, -1.75F, 0.0F);
        corne.addChild(cristal3);
        setRotationAngle(cristal3, 0.0F, -0.3491F, 0.1745F);
        cristal3.setTextureOffset(36, 23).addCuboid(-11.0F, -2.0F, -1.0001F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        cristal3.setTextureOffset(24, 5).addCuboid(-12.0F, -1.5F, -0.5001F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cristal2 = new ModelPart(this);
        cristal2.setPivot(0.0F, 1.75F, -0.25F);
        corne.addChild(cristal2);
        setRotationAngle(cristal2, 0.0F, -0.3491F, -0.1745F);
        cristal2.setTextureOffset(36, 27).addCuboid(-11.0F, -2.0F, -1.0001F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        cristal2.setTextureOffset(0, 25).addCuboid(-12.0F, -1.5F, -0.5001F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        Canon = new ModelPart(this);
        Canon.setPivot(9.0F, 0.0F, 0.0001F);
        tete.addChild(Canon);
        setRotationAngle(Canon, 0.0F, 3.1416F, 0.0F);


        Embout = new ModelPart(this);
        Embout.setPivot(9.0F, 0.0F, 0.0F);
        Canon.addChild(Embout);
        Embout.setTextureOffset(0, 28).addCuboid(-3.0F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);

        bords = new ModelPart(this);
        bords.setPivot(-2.0F, 0.0F, 0.0F);
        Embout.addChild(bords);
        bords.setTextureOffset(0, 38).addCuboid(-2.0F, -2.0F, -3.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        bords.setTextureOffset(20, 32).addCuboid(-2.0F, -3.0F, -2.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        bords.setTextureOffset(30, 12).addCuboid(-2.0F, 1.0F, -2.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        bords.setTextureOffset(0, 16).addCuboid(-2.0F, -2.0F, 1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        Yeux = new ModelPart(this);
        Yeux.setPivot(0.0F, 7.0F, 0.0F);
        Head.addChild(Yeux);
        Yeux.setTextureOffset(18, 38).addCuboid(-5.0F, -15.0F, 1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        Yeux.setTextureOffset(6, 38).addCuboid(-5.0F, -15.0F, -3.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setAngles(SnowTurretEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        //setRotationAngle(tete, 0, (headYaw * 0.017453292F) - 1.570796F, (headPitch * 0.017453292F));
    }


    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Base.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pitch = x;
        modelRenderer.yaw = y;
        modelRenderer.roll = z;
    }

}
