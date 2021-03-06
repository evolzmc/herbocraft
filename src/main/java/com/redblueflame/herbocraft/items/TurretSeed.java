package com.redblueflame.herbocraft.items;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.blocks.TurretSeedBlockEntity;
import com.redblueflame.herbocraft.components.Converters;
import com.redblueflame.herbocraft.components.LevelComponent;
import com.redblueflame.herbocraft.components.TurretLevelComponent;
import com.redblueflame.herbocraft.utils.ComponentsHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tag.Tag;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.List;


public class TurretSeed extends BlockItem  {
    public TurretSeed(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public boolean isIn(Tag<Item> tag) {
        return super.isIn(tag);
    }


    @Override
    public boolean postProcessTag(CompoundTag tag) {
        CompoundTag subtag = new CompoundTag();
        subtag = ComponentsHandler.createComponentTag(subtag);
        tag.put("LevelComponent", subtag);
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> lines, TooltipContext ctx) {
        LevelComponent comp = ComponentsHandler.getItemComponent(stack);

        if (comp == null) {
            // HerboCraft.LOGGER.error("The seed does not have the component attached to it !");
            lines.add(new TranslatableText("level_tooltips.unknown").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
            return;
        }
        lines.add(new TranslatableText("level_tooltips.health", comp.getHealth()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        lines.add(new TranslatableText("level_tooltips.attack_speed", numberFormat.format(comp.getAttackSpeed())).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.damage", comp.getDamage()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.durability", Converters.getKeyFromQuality(Converters.getQualityFromDurability(comp.getDurability()))).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        lines.add(new TranslatableText("level_tooltips.stability", Converters.getKeyFromQuality(Converters.getQualityFromStability(comp.getStability()))).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        if (comp.isSterile()) {
            lines.add(new TranslatableText("level_tooltips.sterile.true").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
        }
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World world, PlayerEntity player, ItemStack stack, BlockState state) {
        System.out.println("Adding component!");
        LevelComponent comp = ComponentsHandler.getItemComponent(stack);
        return writeTagToBlockEntity(world, player, pos, stack, comp);
    }

    public static boolean writeTagToBlockEntity(World world, PlayerEntity player, BlockPos pos, ItemStack stack, LevelComponent component) {
        MinecraftServer minecraftServer = world.getServer();
        if (minecraftServer != null) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                if (!world.isClient && blockEntity.copyItemDataRequiresOperator() && (player == null || !player.isCreativeLevelTwoOp())) {
                    return false;
                }

                // Also put the components here
                if (!(blockEntity instanceof TurretSeedBlockEntity)) {
                    System.out.println("There is a problem, the block entity is not the expected one...");
                    blockEntity.markDirty();
                    return true;
                }
                ((TurretSeedBlockEntity) blockEntity).setComponent(component);

                CompoundTag compoundTag2 = blockEntity.toTag(new CompoundTag());
                compoundTag2.putInt("x", pos.getX());
                compoundTag2.putInt("y", pos.getY());
                compoundTag2.putInt("z", pos.getZ());
                blockEntity.fromTag(world.getBlockState(pos), compoundTag2);
                blockEntity.markDirty();
                return true;
            }
        }
        return false;
    }

}