package com.redblueflame.herbocraft.items;

import com.redblueflame.herbocraft.HerboCraft;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class WateringCanItem extends Item {
    public WateringCanItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
//        BlockPos pos = context.getBlockPos();
//        BlockState state = context.getWorld().getBlockState(pos);
//        Block block = state.getBlock();
//
//        if (block instanceof CropBlock) {
//            CropBlock cropBlock = (CropBlock) block;
//            cropBlock.randomTick(state, context.getWorld().getServer().getW, pos, this.random);
//        }

        if (context.getWorld().isClient()) {
            BlockPos pos = context.getBlockPos();
            ItemStack itemStack = context.getStack();

            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeBlockPos(pos);
            passedData.writeItemStack(itemStack);
            ClientSidePacketRegistry.INSTANCE.sendToServer(HerboCraft.WATERING_CAN_USAGE_PACKET, passedData);
        }

        return super.useOnBlock(context);
    }
}
