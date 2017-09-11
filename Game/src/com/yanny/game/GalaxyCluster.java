package com.yanny.game;

import com.yanny.utils.OpenSimplexNoise;
import com.yanny.utils.Point3L;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GalaxyCluster {
    @NotNull private final Universe universe;
    @NotNull private final OpenSimplexNoise noise;
    @NotNull private final Random random;
    @NotNull private final Point3L location;
    @NotNull private final List<Galaxy> galaxies;
    private final long seed;
    private final double density;

    GalaxyCluster(@NotNull Universe universe, long x, long y, long z) {
        this.universe = universe;
        location = new Point3L(x, y, z);
        seed = Utils.hash(universe.getSeed(), x, y, z);
        noise = new OpenSimplexNoise(seed);
        density = noise.eval(x / 100.0, y / 100.0, z / 100.0);
        random = new Random(seed);

        galaxies = new ArrayList<>();

        for (int i = 0; i < 8096; i++) {
            double rnd = random.nextDouble();

            if (rnd < density) {
                galaxies.add(new Galaxy(this, galaxies.size(), random.nextDouble(), random.nextDouble(), random.nextDouble()));
            }
        }
    }

    @NotNull
    List<Galaxy> getGalaxyList() {
        return galaxies;
    }

    @Nullable
    Galaxy getGalaxy(int value) {
        if (value >= galaxies.size()) {
            return null;
        }

        return galaxies.get(value);
    }

    long getSeed() {
        return seed;
    }

    @NotNull
    Universe getUniverse() {
        return universe;
    }

    @NotNull
    Point3L getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("%s", location);
    }
}
