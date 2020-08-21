package com.redblueflame.herbocraft.mixins;

import com.redblueflame.herbocraft.items.TurretSeed;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.xml.crypto.dsig.CanonicalizationMethod;

@Mixin(ItemStack.class)
public class FixTagInit {
    @Shadow
    private CompoundTag tag;

    @Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at=@At(value = "TAIL", target = "Lnet/minecraft/item/ItemStack;updateEmptyState()V"))
    public void addTags(ItemConvertible item, int count, CallbackInfo ci) {
        if (item instanceof TurretSeed) {
            // Create the tag
            if (tag == null) {
                tag = new CompoundTag();
                ((TurretSeed) item).postProcessTag(tag);
            }
        }
    }
}
