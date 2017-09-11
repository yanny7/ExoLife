package com.yanny.game;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Random;

public class StarClass {
    private static final double LOG_L_MAX = Math.log(10_000_000);
    private static final double LOG_L_MIN = Math.log(0.000_000_1);

    private double mass; // (0.001 - 400)
    private double radius; // 0? - 2000 : R/Rs = (Ts/T)2(L/Ls)1/2
    private double luminosity; // 0 - 10 000 000 (supernova 8 000 000 000 000)
    private MKClass mkClass;
    private byte mkSubClass;
    private LuminosityClass luminosityClass;
    private int temperature;
    //private double age; // in G years (max: MS lifetime = 10^10 x M/L yrs)

    private StarClass(int temperature, double mass, double radius, double luminosity, MKClass mkClass, byte mkSubClass, LuminosityClass luminosityClass) {
        this.temperature = temperature;
        this.mass = mass;
        this.radius = radius;
        this.luminosity = luminosity;
        this.mkClass = mkClass;
        this.mkSubClass = mkSubClass;
        this.luminosityClass = luminosityClass;
    }

    static StarClass generate(Random random) {
        LuminosityClass luminosityClass;
        MKClass mkClass;
        int temperature;
        double mass;
        double radius;
        double luminosity;
        byte mkSubClass;

        mkClass = MKClass.generate(random);
        temperature = mkClass.minTemp + random.nextInt(mkClass.maxTemp - mkClass.minTemp);
        luminosity = generateLuminosity(temperature, random);
        radius = Math.pow(5778.0 / temperature, 2) * Math.pow(luminosity, 0.5);
        mkSubClass = generateSubClass(temperature, mkClass);
        luminosityClass = LuminosityClass.generate(random);
        mass = generateMass(luminosityClass, random);
        return new StarClass(temperature, mass, radius, luminosity, mkClass, mkSubClass, luminosityClass);
    }

    public int getTemperature() {
        return temperature;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getLuminosity() {
        return luminosity;
    }

    public MKClass getMkClass() {
        return mkClass;
    }

    public byte getMkSubClass() {
        return mkSubClass;
    }

    public LuminosityClass getLuminosityClass() {
        return luminosityClass;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,"M(s)=%7.3f, R(s)=%8.3f, L(s)=%15.7f, T(K)=%5d, type %s%d%-3s", mass, radius, luminosity, temperature, mkClass, mkSubClass, luminosityClass);
    }

    private static double generateLuminosity(int temperature, Random random) {
        double powTemp = Math.pow(5778.0 / temperature, 2);
        // ensure that diameter is less than 1500 Rs
        double logDMax = Math.log(Math.pow(1500 / powTemp, 2));
        double logMax = Math.min(LOG_L_MAX, logDMax);
        // ensure that diameter is no less than 0.001 Rs
        double logDMin = Math.log(Math.pow(0.001 / powTemp, 2));
        double logMin = Math.max(LOG_L_MIN, logDMin);

        return Math.exp(random.nextDouble() * (logMax - logMin) + logMin);
    }

    private static byte generateSubClass(int temperature, MKClass mkClass) {
        double step = (mkClass.maxTemp - mkClass.minTemp) / 10;
        double val = temperature - mkClass.minTemp;
        return (byte) Math.floor(val / step);
    }

    private static double generateMass(LuminosityClass luminosityClass, Random random) {
        double mass; //TODO

        do {
            mass = Math.abs((random.nextGaussian()) / 2.0);

            if (mass > 1) {
                mass = Math.pow(mass, 5);
            }

            mass = mass * (luminosityClass.maxMass / 3.0) + luminosityClass.minMass;
        } while (mass > luminosityClass.maxMass);

        return mass;
    }

    enum MKClass {
        O(0.00003, 30000,  250000),
        B( 0.0013, 10000,   30000),
        A(  0.006,  7500,   10000),
        F(   0.03,  6000,    7500),
        G(  0.076,  5200,    6000),
        K(  0.121,  3700,    5200),
        M( 0.6645,  2400,    3700),
        L(   0.09,  1300,    2400),
        T(  0.011,   500,    1300),
        Y(0.00007,   200,     500),
        X( 0.0001,     0,       0),
        ;

        private static final RandomDist<MKClass> RND;

        public final double probability;
        public final int minTemp;
        public final int maxTemp;

        static {
            RND = new RandomDist<>();

            for (MKClass mkClass : values()) {
                RND.add(mkClass, mkClass.probability);
            }
        }

        MKClass(double probability, int minTemp, int maxTemp) {
            this.probability = probability;
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
        }

        static MKClass generate(Random random) {
            return RND.random(random);
        }
    }

    enum LuminosityClass {
        D         (   0.05, 0.08,  1.4, 0.08,    1,  1E-5,     100, EnumSet.of(MKClass.O, MKClass.B, MKClass.A, MKClass.F, MKClass.G, MKClass.K)),
        VI        (   0.01,  0.1,   10, 0.01,    1, 0.001,      10, EnumSet.of(MKClass.G, MKClass.K, MKClass.M)),
        V         (0.88289,  0.2,   50,  0.3,   10,     0, 1000000, EnumSet.of(MKClass.O, MKClass.B, MKClass.A, MKClass.F, MKClass.G, MKClass.K, MKClass.M, MKClass.L, MKClass.T, MKClass.Y)),
        IV        (   0.01,  0.5,   60,    5,   25,   0.5,    2000, EnumSet.of(MKClass.A, MKClass.F, MKClass.G, MKClass.K)),
        III       (   0.04,    1,   80,   10,   50,    10,   10000, EnumSet.of(MKClass.O, MKClass.B, MKClass.A, MKClass.F, MKClass.G, MKClass.K, MKClass.M)),
        II        (  0.005,    5,  100,   15,  100,   100,  100000, EnumSet.of(MKClass.O, MKClass.B, MKClass.A, MKClass.F, MKClass.G, MKClass.K, MKClass.M)),
        Ib        (  0.001,   10,  150,   25,  200,   500, 1000000, EnumSet.of(MKClass.O, MKClass.B, MKClass.A, MKClass.F, MKClass.G, MKClass.K, MKClass.M)),
        Ia        ( 0.0001,   10,  200,   30, 1500,  1000, 5000000, EnumSet.of(MKClass.O, MKClass.B, MKClass.A, MKClass.F, MKClass.G, MKClass.K, MKClass.M)),
        I0        (0.00001,   40,  400,  100, 2000, 10000,     1E9, EnumSet.of(MKClass.O, MKClass.B, MKClass.A, MKClass.F, MKClass.G, MKClass.K, MKClass.M)),
        BLACK_HOLE(  0.001,    3, 1E12, 3E-5,  1E5,     0,       0, EnumSet.of(MKClass.X)),
        ;

        private static final RandomDist<LuminosityClass> RND;

        private final double probability;
        public final double minMass;
        public final double maxMass;
        public final double minRadius;
        public final double maxRadius;
        public final double minLuminosity;
        public final double maxLuminosity;
        public final EnumSet<MKClass> mkClasses;

        static {
            RND = new RandomDist<>();

            for (LuminosityClass lClass : values()) {
                RND.add(lClass, lClass.probability);
            }
        }

        LuminosityClass(double probability, double minMass, double maxMass, double minRadius, double maxRadius, double minLuminosity, double maxLuminosity, EnumSet<MKClass> mkClasses) {
            this.probability = probability;
            this.minMass = minMass;
            this.maxMass = maxMass;
            this.minRadius = minRadius;
            this.maxRadius = maxRadius;
            this.mkClasses = mkClasses;
            this.minLuminosity = minLuminosity;
            this.maxLuminosity = maxLuminosity;
        }

        static LuminosityClass generate(Random random) {
            return RND.random(random); //TODO
        }
    }
}
