package com.redblueflame.herbocraft.utils;

import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerContainerStream {
    public static List<PlayerEntity> GetPlayerWatchingContainer(World world, BlockPos pos, Class<?> handler) {
        // What is that thing, it's soooo horrible to see
        Stream<PlayerEntity> players = world.getPlayers().stream().map(playerEntity -> playerEntity);
        Stream<PlayerEntity> playerEntityStream = players.filter(playerEntity -> handler.isInstance(playerEntity.currentScreenHandler));
        return playerEntityStream.collect(Collectors.toList());
    }
}
