package com.redblueflame.herbocraft;

import com.redblueflame.herbocraft.items.WateringCanItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class HerboCraftItems {
    public static final ItemGroup HERBO_GROUP = FabricItemGroupBuilder.create(new Identifier("herbocraft", "herbo_group")).icon(() -> new ItemStack(Items.GHAST_TEAR)).build();
    public static final Item WATERING_CAN = new WateringCanItem(new Item.Settings().group(HERBO_GROUP).maxCount(1).maxDamage(50));
}
