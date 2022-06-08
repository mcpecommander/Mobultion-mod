package dev.mcpecommander.mobultion.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.Vec3;

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

    public static void rotateTowardsEntity(Entity lookingEntity, Entity lookingTarget,  float rotationSpeed) {
        Vec3 distanceVector = lookingTarget.position().subtract(lookingEntity.position());
        if (distanceVector.lengthSqr() != 0.0D) {
            lookingEntity.setYRot((float)(Mth.atan2(distanceVector.z, distanceVector.x) * (double)(180F / (float)Math.PI)) + 90.0F);
            lookingEntity.setXRot((float)(Mth.atan2(distanceVector.horizontalDistance(), distanceVector.y) * (double)(180F / (float)Math.PI)) - 90.0F);

            while(lookingEntity.getXRot() - lookingEntity.xRotO < -180.0F) {
                lookingEntity.xRotO -= 360.0F;
            }

            while(lookingEntity.getXRot() - lookingEntity.xRotO >= 180.0F) {
                lookingEntity.xRotO += 360.0F;
            }

            while(lookingEntity.getYRot() - lookingEntity.yRotO < -180.0F) {
                lookingEntity.yRotO -= 360.0F;
            }

            while(lookingEntity.getYRot() - lookingEntity.yRotO >= 180.0F) {
                lookingEntity.yRotO += 360.0F;
            }

            lookingEntity.setXRot(Mth.lerp(rotationSpeed, lookingEntity.xRotO, lookingEntity.getXRot()));
            lookingEntity.setYRot(Mth.lerp(rotationSpeed, lookingEntity.yRotO, lookingEntity.getYRot()));
        }
    }
}
