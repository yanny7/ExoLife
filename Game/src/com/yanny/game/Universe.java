package com.yanny.game;

import org.jetbrains.annotations.NotNull;

class Universe {
    private final long seed;

    Universe(long seed) {
        this.seed = seed;
    }

    @NotNull
    GalaxyCluster getGalaxyCluster(long x, long y, long z) {
        return new GalaxyCluster(this, x, y, z);
    }

    long getSeed() {
        return seed;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("%d", seed);
    }
}
