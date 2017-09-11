package com.yanny.game;

import com.yanny.utils.Point3D;
import org.jetbrains.annotations.NotNull;

public class Galaxy {
    @NotNull private final GalaxyCluster galaxyCluster;
    @NotNull private final Point3D location;
    private final long seed;
    private final int index;

    Galaxy(@NotNull GalaxyCluster galaxyCluster, int index, double x, double y, double z) {
        this.galaxyCluster = galaxyCluster;
        this.index = index;
        location = new Point3D(x, y, z);

        seed = Utils.hash(galaxyCluster.getSeed(), Double.doubleToLongBits(x), Double.doubleToLongBits(y), Double.doubleToLongBits(z));
    }

    @NotNull
    StarCluster getStarCluster(long x, long y, long z) {
        return new StarCluster(this, x, y, z);
    }

    long getSeed() {
        return seed;
    }

    int getIndex() {
        return index;
    }

    @NotNull
    Point3D getLocation() {
        return location;
    }

    @NotNull
    GalaxyCluster getGalaxyCluster() {
        return galaxyCluster;
    }

    @Override
    public String toString() {
        return String.format("%d", index);
    }
}
