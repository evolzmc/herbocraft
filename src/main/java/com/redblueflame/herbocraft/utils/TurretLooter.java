package com.redblueflame.herbocraft.utils;

import com.redblueflame.herbocraft.HerboCraft;
import com.redblueflame.herbocraft.QualityType;
import com.redblueflame.herbocraft.components.LevelComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TurretLooter {
    private static Logger LOGGER = LogManager.getLogger();
    private static Random random;
    public List<TurretDrop> drops;
    public TurretLooter() {
        if (random == null) {
            random = new Random();
        }
    }

    public List<TurretDrop> getDropsForMachine(QualityType quality) {
        List<TurretDrop> prep_drops = new ArrayList<>();
        for (TurretDrop drop: drops) {
            if(quality.greaterOrEquals(drop.minimalType)) {
                Optional<TurretDrop> existing = Arrays
                        .stream(prep_drops.toArray(new TurretDrop[]{}))
                        .filter(x -> drop.id.equals(x.id))
                        .findFirst();
                if (existing.isPresent()) {
                    if (drop.probability > existing.get().probability) {
                        prep_drops.remove(existing.get());
                    } else {
                        continue;
                    }
                }
                prep_drops.add(drop);
            }
        }
        return prep_drops;
    }
    public ItemStack getRandomTurret(QualityType quality) {
        TurretDrop pickedDrop = drawRandomTurret(quality);
        Item pickedItem = Registry.ITEM.get(Identifier.tryParse(pickedDrop.id));
        ItemStack itemStack = new ItemStack(pickedItem, 1);
        // Add cardinal components to it
        Optional<LevelComponent> opt_comp = HerboCraft.LEVELLING.maybeGet(itemStack);
        if (!opt_comp.isPresent()) {
            // This is not a plant that's compatible...
            throw new RuntimeException("This plant is not compatible with the LevelComponent. Please fix that");
        }
        LevelComponent comp = opt_comp.get();
        // Get the levels needed
        comp.resetLevels();
        comp.addLevels(pickedDrop.level.getRandom(random));
        comp.setStability(pickedDrop.stability.getRandom(random));
        return itemStack;
    }
    public TurretDrop drawRandomTurret(QualityType type) {
        List<TurretDrop> drops = getDropsForMachine(type);
        int max = drops.stream().mapToInt(turretDrop -> turretDrop.probability).sum();

        int current = random.nextInt(max+1);
        for (TurretDrop drop: drops) {
            LOGGER.info(drop.probability + " - " + current);
            if (drop.probability >= current) {
                // This is the chosen one
                return drop;
            }
            // Make the probability go down for the next one.
            current -= drop.probability;
        }
        // This shouldn't happen, throw a runtime error
        throw new RuntimeException("No drop was returned! This seems like a logic bug...");
    }

    @Override
    public String toString() {
        return Arrays.toString(drops.toArray());
    }
}
