package dev.mcpecommander.mobultion.utils;

import net.minecraft.util.Mth;

/* McpeCommander created on 09/07/2021 inside the package - dev.mcpecommander.mobultion.utils */
public class MathCalculations {

    //Copied from p5.js Math.Calculations.js
    public static double map(double number, double start, double stop, double finalStart, double finalStop){
        double value =(number - start) / (stop - start) * (finalStop - finalStart) + finalStart;

        if (finalStart < finalStop) {
            return Mth.clamp(value, finalStart, finalStop);
        } else {
            return Mth.clamp(value, finalStop, finalStart);
        }
    }
}
