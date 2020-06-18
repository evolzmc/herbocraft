package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.components.LevelComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.OceanMonumentGenerator;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import spinnery.common.inventory.BaseInventory;
import spinnery.common.utility.InventoryUtilities;

import java.util.Optional;

public class SterilizerBlockEntity extends BlockEntity implements Tickable {
    private int workingStage;
    private int finalStage;
    private boolean isWorking;
    public BaseInventory inventory;
    public short progression;

    public SterilizerBlockEntity() {
        super(HerboCraft.STERILIZER_BLOCK_ENTITY);
        workingStage = 0;
        finalStage = 1;
        progression = 0;
    }

    // Make the block work

    // Serialize the BlockEntity
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        // Save the current value of the number to the tag
        tag.putInt("workingStage", workingStage);
        tag.putInt("finalStage", finalStage);
        InventoryUtilities.write(inventory, tag);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        workingStage = tag.getInt("workingStage");
        finalStage = tag.getInt("finalStage");
        inventory = InventoryUtilities.read(tag);
    }

    public void registerEvents() {
        inventory.addListener(sender -> setWork(sender.getStack(0)));
    }
    private boolean isAccepted(LevelComponent component) {
        return !component.isSterile();
    }
    private int estimateWork(LevelComponent component) {
        return 600-(2*component.getStability());
    }
    private void resetWork() {
        workingStage = 0;
        finalStage = 1;
        progression = 0;
        isWorking = false;
    }
    private void setWork(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            resetWork();
            return;
        }
        Optional<LevelComponent> opt_comp = HerboCraft.LEVELLING.maybeGet(itemStack);
        if (!opt_comp.isPresent()) {
            // This is not a plant that's compatible...
            resetWork();
            return;
        }
        LevelComponent comp = opt_comp.get();
        if (!isAccepted(comp)) {
            resetWork();
            return;
        }
        finalStage = estimateWork(comp);
        workingStage = 0;
        isWorking = true;
    }

    private void onFinish(ItemStack itemStack) {
        Optional<LevelComponent> opt_comp = HerboCraft.LEVELLING.maybeGet(itemStack);
        if (!opt_comp.isPresent()) {
            // This is not a plant that's compatible...
            resetWork();
            return;
        }
        LevelComponent comp = opt_comp.get();
        comp.setSterile(true);
    }

    @Override
    public void tick() {
        if (inventory == null) {
            return;
        }
        ItemStack item = inventory.getStack(0);
        if (item == null || !isWorking || item.isEmpty()) {
            resetWork();
            return;
        }
        workingStage += 1;
        progression = (short) (((float) workingStage/finalStage) * 255);
        if (workingStage >= finalStage) {
            resetWork();
            onFinish(item);
        }
    }

}
