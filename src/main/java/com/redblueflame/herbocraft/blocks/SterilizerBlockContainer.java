package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import spinnery.common.handler.BaseScreenHandler;
import spinnery.common.inventory.BaseInventory;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class SterilizerBlockContainer extends BaseScreenHandler {
    public SterilizerBlockEntity entity;
    public static final int STERILIZER_INVENTORY = 1;
    public Text name;
    public PlayerEntity player;
    public SterilizerBlockContainer(int synchronizationID, Text name, PlayerInventory playerInventory, BlockPos pos, World world) {
        super(synchronizationID, playerInventory);
        this.name = name;
        this.player = playerInventory.player;
        this.entity = (SterilizerBlockEntity) world.getBlockEntity(pos);
        initInventory();
    }
    private void initInventory() {
        WInterface mainInterface = getInterface();
        if (entity.inventory == null) {
            entity.inventory = new BaseInventory(1);
        }
        getInventories().put(STERILIZER_INVENTORY, entity.inventory);
        mainInterface.createChild(WSlot::new);
        Collection<WSlot> items = WSlot.addHeadlessArray(mainInterface, 0, STERILIZER_INVENTORY, 1, 1);
        for (WSlot item: items) {
            //noinspection unchecked
            item.accept(HerboCraft.SEEDS);
            item.setWhitelist();
        }
        WSlot.addHeadlessPlayerInventory(mainInterface);
        // init the events
        entity.registerEvents();
    }

}
