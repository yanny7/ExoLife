package com.yanny.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class Location {
    public static final int BYTES = 64;

    @NotNull private final Point3L galaxyCluster;
    @NotNull private final Point3L starCluster;
    private final long universe;
    private final int galaxy;
    private final int star;

    public Location(long universe, @NotNull Point3L galaxyCluster, int galaxy, @NotNull Point3L starCluster, int star) {
        this.universe = universe;
        this.galaxyCluster = galaxyCluster;
        this.galaxy = galaxy;
        this.starCluster = starCluster;
        this.star = star;
    }

    @NotNull
    public byte[] toByte() throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteOutputStream);

        dos.writeLong(universe);
        dos.writeLong(galaxyCluster.x);
        dos.writeLong(galaxyCluster.y);
        dos.writeLong(galaxyCluster.z);
        dos.writeInt(galaxy);
        dos.writeLong(starCluster.x);
        dos.writeLong(starCluster.y);
        dos.writeLong(starCluster.z);
        dos.writeInt(star);

        byte[] data = byteOutputStream.toByteArray();

        dos.close();
        return data;
    }

    public static Location fromBytes(byte[] data) throws IOException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(byteInputStream);
        long x, y, z;
        long universe = dis.readLong();
        x = dis.readLong();
        y = dis.readLong();
        z = dis.readLong();
        Point3L galaxyCluster = new Point3L(x, y, z);
        int galaxy = dis.readInt();
        x = dis.readLong();
        y = dis.readLong();
        z = dis.readLong();
        Point3L starCluster = new Point3L(x, y, z);
        int star = dis.readInt();
        dis.close();
        return new Location(universe, galaxyCluster, galaxy, starCluster, star);
    }
}
