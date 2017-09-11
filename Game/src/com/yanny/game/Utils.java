package com.yanny.game;

import java.util.Random;

public class Utils {
    private Utils(){}

    public static long hash(long seed, long x, long y, long z) {
        return seed * 31L + x * 31L + y * 2147483647L + z * 2305843009213693951L;
    }

    public static String buildName(Random random) {
        StringBuilder stringBuilder = new StringBuilder();

        if (Math.abs(random.nextGaussian()) < 1.5) {
            for (int i = 0; i < 3 + Math.round(Math.abs(random.nextGaussian()) * 10); i++) {
                stringBuilder.append((char) (random.nextInt(26) + (i == 0 ? 'A' : 'a')));
            }

            if (Math.abs(random.nextGaussian()) > 1) {
                stringBuilder.append(' ');

                for (int i = 0; i < 1 + Math.round(Math.abs(random.nextGaussian()) * 3 + 0.4); i++) {
                    stringBuilder.append((char) (random.nextInt(26) + (i == 0 ? 'A' : 'a')));
                }
            }
        } else {
            for (int i = 0; i < 3 + Math.round(Math.abs(random.nextGaussian())); i++) {
                stringBuilder.append((char) (random.nextInt(26) + 'A'));
            }

            stringBuilder.append('-');

            for (int i = 0; i < 3 + Math.round(Math.abs(random.nextGaussian()) * 2); i++) {
                stringBuilder.append((char) (random.nextInt(10) + '0'));
            }
        }

        return stringBuilder.toString();
    }


}
