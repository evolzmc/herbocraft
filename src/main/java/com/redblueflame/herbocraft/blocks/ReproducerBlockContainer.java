package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import spinnery.common.handler.BaseScreenHandler;
import spinnery.common.inventory.BaseInventory;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;

import java.util.Collection;

public class ReproducerBlockContainer extends BaseScreenHandler {
    public ReproducerBlockEntity entity;
    public static final int GROWTH_CONTROLLER_INVENTORY = 1;
    public Text name;
    public PlayerEntity player;

    public ReproducerBlockContainer(int synchronizationID, Text name, PlayerInventory playerInventory, BlockPos pos, World world) {
        super(synchronizationID, playerInventory);
        this.name = name;
        this.player = playerInventory.player;
        this.entity = (ReproducerBlockEntity) world.getBlockEntity(pos);
        initInventory();
    }

    private void initInventory() {
        WInterface mainInterface = getInterface();
        if (entity.inventory == null) {
            entity.inventory = new BaseInventory(11);
        }
        getInventories().put(GROWTH_CONTROLLER_INVENTORY, entity.inventory);
        mainInterface.createChild(WSlot::new);
        Collection<WSlot> input = WSlot.addHeadlessArray(mainInterface, 0, GROWTH_CONTROLLER_INVENTORY, 2, 1);
        for (WSlot item: input) {
            //noinspection unchecked
            item.accept(HerboCraft.SEEDS);
            item.setWhitelist();
        }
        Collection<WSlot> output = WSlot.addHeadlessArray(mainInterface, 2, GROWTH_CONTROLLER_INVENTORY, 3, 3);
        for (WSlot item: output) {
            item.accept(Items.AIR);
            item.setWhitelist();
        }
        WSlot.addHeadlessPlayerInventory(mainInterface);
        // init the events
        entity.registerEvents();
    }
}