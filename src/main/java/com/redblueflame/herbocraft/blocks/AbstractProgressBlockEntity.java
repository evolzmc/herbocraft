package com.redblueflame.herbocraft.blocks;

import com.redblueflame.herbocraft.HerboCraftPackets;
import com.redblueflame.herbocraft.utils.PlayerContainerStream;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Tickable;

public abstract class AbstractProgressBlockEntity extends BlockEntity implements Tickable, Progress {
    public boolean isWorking;
    public int targetWork;
    public int currentWork;
    public int state;

    public AbstractProgressBlockEntity(BlockEntityType<?> type) {
        super(type);
        isWorking = false;
        targetWork = 0;
        currentWork = 0;
        state = 0;
    }

    @Override
    public void tick() {
        if (world != null && !world.isClient) {
            // We have to check for the server, is it's on the client, we will have problems.
            if (isWorking && targetWork != 0) {
                currentWork += 1;
                state = (int) (((float)currentWork / targetWork) * 255F);
                if (currentWork >= targetWork) {
                    resetWork();
                    finishWork();
                }
            } else {
                if (getInventory() != null && this.checkWork()) {
                    targetWork = estimateWork();
                    setWorking();
                }
            }
        } else if (isWorking) {
            // On the client, we can add the currentWork for display
            currentWork += 1;
            state = (int) (((float)currentWork / targetWork) * 255F);
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        isWorking = tag.getBoolean("isWorking");
        targetWork = tag.getInt("targetWork");
        currentWork = tag.getInt("currentWork");
        if (targetWork != 0) {
            this.state = currentWork/targetWork;
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putBoolean("isWorking", isWorking);
        tag.putInt("targetWork", targetWork);
        tag.putInt("currentWork", currentWork);
        return tag;
    }

    @Override
    public void resetWork() {
        isWorking = false;
        // Send the update to clients
        if (world != null && !world.isClient) {
            sendStateChangePacket();
        }
        targetWork = 0;
        currentWork = 0;
        state = 0;
    }

    private void setWorking() {
        isWorking = true;
        if (world != null && !world.isClient) {
            sendStateChangePacket();
        }
        currentWork = 0;
    }

    protected void registerEvents() {
        if (getInventory() != null) {
            getInventory().addListener(this::onInventoryUpdate);
        }
    }

    private void onInventoryUpdate(Inventory inventory) {
        if (inventoryUpdate(inventory)) {
            resetWork();
        }
    }
    // region Packets
    private void sendStateChangePacket() {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(pos);
        passedData.writeBoolean(isWorking);
        passedData.writeInt(targetWork);
        PlayerContainerStream.GetPlayerWatchingContainer(world, pos, this.getContainerClass()).forEach(playerEntity ->
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerEntity, HerboCraftPackets.STATE_CHANGE, passedData));
    }

    protected void sendBlockProgressPacket() {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(pos);
        passedData.writeInt(targetWork);
        passedData.writeInt(currentWork);
        PlayerContainerStream.GetPlayerWatchingContainer(world, pos, this.getContainerClass()).forEach(playerEntity ->
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerEntity, HerboCraftPackets.SEND_STATUS, passedData));
    }

    protected void sendBlockProgressPacket(PlayerEntity e) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(pos);
        passedData.writeInt(targetWork);
        passedData.writeInt(currentWork);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(e, HerboCraftPackets.SEND_STATUS, passedData);
    }
    // endregion
}
