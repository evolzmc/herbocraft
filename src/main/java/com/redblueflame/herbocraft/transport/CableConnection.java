package com.redblueflame.herbocraft.transport;

import net.minecraft.util.StringIdentifiable;

public enum CableConnection implements StringIdentifiable {
    DISCONNECTED("disconnected"),
    CONNECTED("connected"),
    BLOCK_CONNECTED("block_connected");

    private final String name;

    CableConnection(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }
}
