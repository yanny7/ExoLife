package com.yanny.game;

import com.yanny.utils.Point3D;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Star {
    @NotNull private StarCluster starCluster;
    @NotNull private Point3D location;
    @NotNull private StarLocation starLocation;
    @NotNull private StarClass starClass;
    @NotNull private String name;
    private long seed;
    private int index;

    Star(@NotNull StarCluster starCluster, int index, double x, double y, double z) {
        this.starCluster = starCluster;
        this.index = index;
        location = new Point3D(x, y, z);

        seed = Utils.hash(starCluster.getSeed(), Double.doubleToLongBits(x), Double.doubleToLongBits(y), Double.doubleToLongBits(z));

        Galaxy galaxy = starCluster.getGalaxy();
        GalaxyCluster galaxyCluster = galaxy.getGalaxyCluster();
        Universe universe = galaxyCluster.getUniverse();
        starLocation = new StarLocation(universe, galaxyCluster, galaxy, starCluster, this);
        starClass = StarClass.generate(new Random(seed));
        name = Utils.buildName(new Random(seed));
    }

    long getSeed() {
        return seed;
    }

    int getIndex() {
        return index;
    }

    @NotNull
    String getName() {
        return name;
    }

    @NotNull
    Point3D getLocation() {
        return location;
    }

    @NotNull
    StarCluster getStarCluster() {
        return starCluster;
    }

    @NotNull
    StarLocation getStarLocation() {
        return starLocation;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("%4d : [%s] : %30s", index, starClass, name);
    }
}
