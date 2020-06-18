package com.redblueflame.herbocraft.items;

import com.redblueflame.herbocraft.HerboCraftPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class WateringCanItem extends ToolItem {
    public WateringCanItem(Settings settings) {
        super(ToolMaterials.IRON, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            BlockPos pos = context.getBlockPos();

            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeBlockPos(pos);
            passedData.writeBoolean(context.getHand().equals(Hand.MAIN_HAND));
            passedData.writeInt(1); // Range
            ClientSidePacketRegistry.INSTANCE.sendToServer(HerboCraftPackets.WATERING_CAN_USAGE_PACKET, passedData);
        }

        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        int damage = itemStack.getDamage();
        BlockHitResult hitResult = rayTrace(world, user, RayTraceContext.FluidHandling.SOURCE_ONLY);

        if (damage > 0 && hitResult.getType().equals(HitResult.Type.BLOCK)) {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();

            if (block instanceof FluidDrainable) {
                Fluid fluid = ((FluidDrainable) block).tryDrainFluid(world, blockPos, blockState);
                world.setBlockState(blockPos, blockState);

                if (fluid == Fluids.WATER) {
                    user.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                    damage -= 2;
                    if (damage <= 0) damage = 0;
                    user.getStackInHand(hand).setDamage(damage);
                }
            }
        }

        return super.use(world, user, hand);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }
}
