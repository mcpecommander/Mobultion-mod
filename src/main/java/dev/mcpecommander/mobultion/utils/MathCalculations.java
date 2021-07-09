package dev.mcpecommander.mobultion.utils;

import net.minecraft.util.math.MathHelper;

/* McpeCommander created on 09/07/2021 inside the package - dev.mcpecommander.mobultion.utils */
public class MathCalculations {

    //Copied from p5.js Math.Calculations.js
    public static double map(double number, double start, double stop, double finalStart, double finalStop){
        double value =(number - start) / (stop - start) * (finalStop - finalStart) + finalStart;

        if (finalStart < finalStop) {
            return MathHelper.clamp(value, finalStart, finalStop);
        } else {
            return MathHelper.clamp(value, finalStop, finalStart);
        }
    }
}
