package util;

import java.util.Random;

public class MattyRandom {

    private static Random random;
    private static long seed;

    // this is how the seed is set in Java 1.4
    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    // don't instantiate
    private MattyRandom() {}

    /**
     * Sets the seed of the pseudo-random number generator.
     * This method enables you to produce the same sequence of "random" number for each execution of the program.
     * Ordinarily, you should call this method at most once per program.
     * @param s the seed
     */
    public static void setSeed(long s) {
        seed = s;
        random = new Random(s);
    }

    public static long getSeed() {
        return seed;
    }

    /**
     *
     * @return a random real number uniformly in [0, 1)
     */
    public static double uniform() {
        return random.nextDouble();
    }

    /**
     *
     * @param n integer bound
     * @return a random integer between [0, n)
     */
    public static int uniform(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Argument must be positive: " + n);
        }
        return random.nextInt(n);
    }


}
