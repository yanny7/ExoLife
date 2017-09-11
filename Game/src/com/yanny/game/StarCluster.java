package com.yanny.game;

import com.yanny.utils.OpenSimplexNoise;
import com.yanny.utils.Point3L;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarCluster {
    @NotNull private final Galaxy galaxy;
    @NotNull private final OpenSimplexNoise noise;
    @NotNull private final Random random;
    @NotNull private final Point3L location;
    @NotNull private final List<Star> stars;
    private final double density;
    private final long seed;

    StarCluster(@NotNull Galaxy galaxy, long x, long y, long z) {
        this.galaxy = galaxy;

        location = new Point3L(x, y, z);
        seed = Utils.hash(galaxy.getSeed(), x, y, z);
        noise = new OpenSimplexNoise(seed);
        density = noise.eval(x / 100.0, y / 100.0, z / 100.0); //TODO galaxy generator
        random = new Random(seed);

        stars = new ArrayList<>();

        for (int i = 0; i < 8096; i++) {
            double rnd = random.nextDouble();

            if (rnd < density) {
                stars.add(new Star(this, stars.size(), random.nextDouble(), random.nextDouble(), random.nextDouble()));
            }
        }
    }

    @NotNull
    List<Star> getStarList() {
        return stars;
    }

    @Nullable
    Star getStar(int value) {
        if (value >= stars.size()) {
            return null;
        }

        return stars.get(value);
    }

    @NotNull
    Point3L getLocation() {
        return location;
    }

    @NotNull
    Galaxy getGalaxy() {
        return galaxy;
    }

    long getSeed() {
        return seed;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("%s", location);
    }
}
