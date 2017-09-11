package com.yanny.game;

import com.sun.istack.internal.NotNull;
import com.yanny.interfaces.GameInterface;
import com.yanny.utils.Location;

import java.util.List;
import java.util.Random;

public class Game implements GameInterface {
    private static final long SEED = 1234567890L;

    @NotNull private final Universe universe;

    public Game() {
        universe = new Universe(SEED);
    }

    @Override
    public Location findRandomLocation() {
        Random random = new Random(System.currentTimeMillis());
        long universeSeed = universe.getSeed();
        GalaxyCluster galaxyCluster;
        List<Galaxy> galaxies;

        do {
            galaxyCluster = universe.getGalaxyCluster(random.nextInt(), random.nextInt(), random.nextInt());
            galaxies = galaxyCluster.getGalaxyList();
        } while (galaxies.isEmpty());

        int galaxyIndex = random.nextInt(galaxies.size());
        Galaxy galaxy = galaxies.get(galaxyIndex);
        StarCluster starCluster;
        List<Star> stars;

        do {
            starCluster = galaxy.getStarCluster(random.nextInt(), random.nextInt(), random.nextInt());
            stars = starCluster.getStarList();
        } while (stars.isEmpty());

        int starIndex = random.nextInt(stars.size());
        return new Location(universeSeed, galaxyCluster.getLocation(), galaxyIndex, starCluster.getLocation(), starIndex);
    }
}
