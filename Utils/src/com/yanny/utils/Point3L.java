package com.yanny.utils;

public class Point3L {
    public long x;
    public long y;
    public long z;

    public Point3L(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format("%d:%d:%d", x,y,z);
    }
}
