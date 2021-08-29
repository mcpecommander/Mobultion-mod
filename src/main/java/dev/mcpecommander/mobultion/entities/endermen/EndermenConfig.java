package dev.mcpecommander.mobultion.entities.endermen;

import net.minecraftforge.common.ForgeConfigSpec;

/* McpeCommander created on 29/08/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen */
public class EndermenConfig {

    //Gardener Enderman values
    public static ForgeConfigSpec.DoubleValue GARDENER_HEALTH;
    public static ForgeConfigSpec.DoubleValue GARDENER_SPEED;
    public static ForgeConfigSpec.IntValue GARDENER_RADIUS;
    //Glass Enderman values
    public static ForgeConfigSpec.DoubleValue GLASS_HEALTH;
    public static ForgeConfigSpec.DoubleValue GLASS_SPEED;
    public static ForgeConfigSpec.DoubleValue GLASS_DAMAGE;
    public static ForgeConfigSpec.IntValue GLASS_RADIUS;
    //Ice Enderman values
    public static ForgeConfigSpec.DoubleValue ICE_HEALTH;
    public static ForgeConfigSpec.DoubleValue ICE_SPEED;
    public static ForgeConfigSpec.DoubleValue ICE_DAMAGE;
    public static ForgeConfigSpec.IntValue ICE_RADIUS;
    //Magma Enderman values
    public static ForgeConfigSpec.DoubleValue MAGMA_HEALTH;
    public static ForgeConfigSpec.DoubleValue MAGMA_SPEED;
    public static ForgeConfigSpec.DoubleValue MAGMA_DAMAGE;
    public static ForgeConfigSpec.IntValue MAGMA_RADIUS;
    public static ForgeConfigSpec.IntValue MAGMA_FIRE;
    //Wandering Enderman values
    public static ForgeConfigSpec.DoubleValue WANDERING_HEALTH;
    public static ForgeConfigSpec.DoubleValue WANDERING_SPEED;
    public static ForgeConfigSpec.DoubleValue WANDERING_DAMAGE;
    public static ForgeConfigSpec.IntValue WANDERING_RADIUS;
    //Glass shot values
    public static ForgeConfigSpec.DoubleValue SHOT_DAMAGE;

    public static void setupEndermenConfig(ForgeConfigSpec.Builder serverBuilder){
        serverBuilder.comment("Endermen Config").push("Endermen");

        serverBuilder.comment("Gardener Enderman Config").push("Gardener Enderman");
        GARDENER_HEALTH = serverBuilder.comment("Gardener Enderman max health").defineInRange("health", 20d, 1d, 300d);
        GARDENER_SPEED = serverBuilder.comment("Gardener Enderman movement speed").defineInRange("speed", 0.4d, 0.1, 2d);
        GARDENER_RADIUS = serverBuilder.comment("Gardener Enderman search radius").defineInRange("radius", 32, 4, 200);
        serverBuilder.pop();

        serverBuilder.comment("Glass Enderman Config").push("Glass Enderman");
        GLASS_HEALTH = serverBuilder.comment("Glass Enderman max health").defineInRange("health", 28d, 1d, 300d);
        GLASS_SPEED = serverBuilder.comment("Glass Enderman movement speed").defineInRange("speed", 0.3d, 0.1, 2d);
        GLASS_RADIUS = serverBuilder.comment("Glass Enderman follow range").defineInRange("range", 64, 4, 200);
        GLASS_DAMAGE = serverBuilder.comment("Glass Enderman glass shot damage").defineInRange("damage", 8d, 0d, 40d);
        serverBuilder.pop();

        serverBuilder.comment("Ice Enderman Config").push("Ice Enderman");
        ICE_HEALTH = serverBuilder.comment("Ice Enderman max health").defineInRange("health", 40d, 1d, 300d);
        ICE_SPEED = serverBuilder.comment("Ice Enderman movement speed").defineInRange("speed", 0.3d, 0.1, 2d);
        ICE_RADIUS = serverBuilder.comment("Ice Enderman follow range").defineInRange("range", 64, 4, 200);
        ICE_DAMAGE = serverBuilder.comment("Ice Enderman melee damage").defineInRange("damage", 7d, 0d, 40d);
        serverBuilder.pop();

        serverBuilder.comment("Magma Enderman Config").push("Magma Enderman");
        MAGMA_HEALTH = serverBuilder.comment("Magma Enderman max health").defineInRange("health", 40d, 1d, 300d);
        MAGMA_SPEED = serverBuilder.comment("Magma Enderman movement speed").defineInRange("speed", 0.5d, 0.1, 2d);
        MAGMA_RADIUS = serverBuilder.comment("Magma Enderman follow range").defineInRange("range", 64, 4, 200);
        MAGMA_DAMAGE = serverBuilder.comment("Magma Enderman melee damage").defineInRange("damage", 7d, 0d, 40d);
        MAGMA_FIRE = serverBuilder.comment("How many seconds to light target on fire").defineInRange("fire", 5, 1, 300);
        serverBuilder.pop();

        serverBuilder.comment("Wandering Enderman Config").push("Wandering Enderman");
        WANDERING_HEALTH = serverBuilder.comment("Wandering Enderman max health").defineInRange("health", 30d, 1d, 300d);
        WANDERING_SPEED = serverBuilder.comment("Wandering Enderman movement speed").defineInRange("speed", 0.3d, 0.1, 2d);
        WANDERING_RADIUS = serverBuilder.comment("Wandering Enderman follow range").defineInRange("range", 64, 4, 200);
        WANDERING_DAMAGE = serverBuilder.comment("Wandering Enderman lightning damage").defineInRange("damage", 5d, 0d, 40d);
        serverBuilder.pop();

        serverBuilder.comment("Glass Shot Config").push("Glass Shot");
        SHOT_DAMAGE = serverBuilder.comment("Glass shot damage when shot by the player").defineInRange("damage", 5d, 1d, 40d);
        serverBuilder.pop();

        serverBuilder.pop();
    }

}
