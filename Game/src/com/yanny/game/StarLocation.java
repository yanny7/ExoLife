package com.yanny.game;

public class StarLocation {
    private Universe universe;
    private GalaxyCluster galaxyCluster;
    private Galaxy galaxy;
    private StarCluster starCluster;
    private Star star;

    StarLocation(Universe universe, GalaxyCluster galaxyCluster, Galaxy galaxy, StarCluster starCluster, Star star) {
        this.universe = universe;
        this.galaxyCluster = galaxyCluster;
        this.galaxy = galaxy;
        this.starCluster = starCluster;
        this.star = star;
    }

    public static StarLocation getStarLocation(String location) {
        String[] tokens = location.split(":");

        if (tokens.length == 9) {
            Universe universe = new Universe(Long.parseUnsignedLong(tokens[0]));

            GalaxyCluster galaxyCluster = universe.getGalaxyCluster(Long.parseUnsignedLong(tokens[1]), Long.parseUnsignedLong(tokens[2]), Long.parseUnsignedLong(tokens[3]));

            if (galaxyCluster == null) {
                return null;
            }

            Galaxy galaxy = galaxyCluster.getGalaxy((int) Long.parseUnsignedLong(tokens[4]));

            if (galaxy == null) {
                return null;
            }

            StarCluster starCluster = galaxy.getStarCluster((int) Long.parseUnsignedLong(tokens[5]), (int) Long.parseUnsignedLong(tokens[6]), (int) Long.parseUnsignedLong(tokens[7]));

            if (starCluster == null) {
                return null;
            }

            Star star = starCluster.getStar((int) Long.parseUnsignedLong(tokens[8]));

            if (star == null) {
                return null;
            }

            return new StarLocation(universe, galaxyCluster, galaxy, starCluster, star);
        } else {
            return null;
        }
    }

    public Universe getUniverse() {
        return universe;
    }

    public GalaxyCluster getGalaxyCluster() {
        return galaxyCluster;
    }

    public Galaxy getGalaxy() {
        return galaxy;
    }

    public StarCluster getStarCluster() {
        return starCluster;
    }

    public Star getStar() {
        return star;
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s:%s:%s", universe, galaxyCluster, galaxy, starCluster, star);
    }
}
