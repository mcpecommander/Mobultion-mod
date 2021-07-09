package dev.mcpecommander.mobultion.utils;

/* McpeCommander created on 09/07/2021 inside the package - dev.mcpecommander.mobultion.utils */
public class MathCalculations {

    //Copied from p5.js Math.Calculations.js
    public static float map(float number, float start, float stop, float finalStart, float finalStop){
        return (number - start) / (stop - start) * (finalStop - finalStart) + finalStart;
    }
}
