package com.yanny.game;

import java.util.HashMap;
import java.util.Random;

public class RandomDist<T> {
    private HashMap<T, Double> distribution;
    private double distSum;

    public RandomDist() {
        distribution = new HashMap<>();
    }

    public void add(T value, double distribution) {
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }

        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public T random(Random random) {
        double rand = random.nextDouble();
        double ratio = 1.0f / distSum;
        double tempDist = 0;

        for (T i : distribution.keySet()) {
            tempDist += distribution.get(i);

            if (rand / ratio <= tempDist) {
                return i;
            }
        }

        if (distribution.isEmpty()) {
            return null;
        } else {
            return distribution.entrySet().iterator().next().getKey();
        }
    }
}
