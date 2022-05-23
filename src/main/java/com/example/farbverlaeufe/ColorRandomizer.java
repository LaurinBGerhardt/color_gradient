package com.example.farbverlaeufe;

import java.util.Random;

/**
 * (modified) from https://stackoverflow.com/questions/3680637/generate-a-random-double-in-a-range
 * A custom randomizer only here to adjust the colors of the pixels within reasonable range
 */
public class ColorRandomizer {
    static Random rand = new Random();
    //the values are selected so that the colors don't turn dark quite as quickly:
    static double  MAX = 1.55;
    static double MIN = 0.91;

    static double adjust() {
        double adjusted_value = rand.nextDouble() * (MAX - MIN) + MIN;
        if (adjusted_value > 1) return 1;
        else if (adjusted_value <= 0) return 0.2; //so that it doesn't turn dark quite as quickly
        else return adjusted_value;
    }
}
