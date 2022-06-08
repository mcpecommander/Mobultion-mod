package dev.mcpecommander.mobultion.entities.spiders;

import net.minecraftforge.common.ForgeConfigSpec;

/* McpeCommander created on 30/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders */
public class SpidersConfig {

    //Angel spider config
    public static ForgeConfigSpec.DoubleValue ANGEL_HEALTH;
    public static ForgeConfigSpec.DoubleValue ANGEL_SPEED;
    public static ForgeConfigSpec.DoubleValue ANGEL_HEAL;

    //Hypno spider config
    public static ForgeConfigSpec.DoubleValue HYPNO_HEALTH;
    public static ForgeConfigSpec.DoubleValue HYPNO_SPEED;
    public static ForgeConfigSpec.DoubleValue HYPNO_DAMAGE;
    public static ForgeConfigSpec.IntValue HYPNO_DURATION;

    //Magma spider config
    public static ForgeConfigSpec.DoubleValue MAGMA_HEALTH;
    public static ForgeConfigSpec.DoubleValue MAGMA_SPEED;
    public static ForgeConfigSpec.DoubleValue MAGMA_DAMAGE;
    public static ForgeConfigSpec.IntValue MAGMA_FIRE;

    //Mini spider config
    public static ForgeConfigSpec.DoubleValue MINI_HEALTH;
    public static ForgeConfigSpec.DoubleValue MINI_SPEED;
    public static ForgeConfigSpec.DoubleValue MINI_DAMAGE;

    //Mother spider config
    public static ForgeConfigSpec.DoubleValue MOTHER_HEALTH;
    public static ForgeConfigSpec.DoubleValue MOTHER_SPEED;
    public static ForgeConfigSpec.DoubleValue MOTHER_DAMAGE;
    public static ForgeConfigSpec.IntValue MOTHER_GESTATION;
    public static ForgeConfigSpec.IntValue MOTHER_MAX_MINI_FOLLOWERS;

    //Witch spider config
    public static ForgeConfigSpec.DoubleValue WITCH_HEALTH;
    public static ForgeConfigSpec.DoubleValue WITCH_SPEED;
    public static ForgeConfigSpec.IntValue WITCH_FREEZE;
    public static ForgeConfigSpec.DoubleValue WITCH_FIREBALL_DAMAGE;

    //Wither spider config
    public static ForgeConfigSpec.DoubleValue WITHER_HEALTH;
    public static ForgeConfigSpec.DoubleValue WITHER_SPEED;
    public static ForgeConfigSpec.DoubleValue WITHER_MELEE_DAMAGE;
    public static ForgeConfigSpec.IntValue WITHER_RANGED_EFFECT_DURATION;
    public static ForgeConfigSpec.IntValue WITHER_RANGED_EFFECT_STRENGTH;

    //Red eye config
    public static ForgeConfigSpec.DoubleValue RED_EYE_HEALTH;
    public static ForgeConfigSpec.DoubleValue RED_EYE_SPEED;
    public static ForgeConfigSpec.IntValue RED_EYE_DAMAGE;

    public static void setupSpidersConfig(ForgeConfigSpec.Builder serverBuilder) {
        serverBuilder.comment("Spiders Config").push("Spiders");

        serverBuilder.push("Angel Spider Config").push("Angel Spider");
        ANGEL_HEALTH = serverBuilder.comment("Angel Spider max health").defineInRange("health", 10d, 1d, 300d);
        ANGEL_SPEED = serverBuilder.comment("Angel Spider movement speed").defineInRange("speed", 0.4d, 0.1d, 2d);
        ANGEL_HEAL = serverBuilder.comment("Angel Spider healing amount").defineInRange("healing", 4d, 1d, 20d);
        serverBuilder.pop();

        serverBuilder.push("Hypno Spider Config").push("Hypno Spider");
        HYPNO_HEALTH = serverBuilder.comment("Hypno Spider max health").defineInRange("health", 16d, 1d, 300d);
        HYPNO_SPEED = serverBuilder.comment("Hypno Spider movement speed").defineInRange("speed", 0.2d, 0.1d, 2d);
        HYPNO_DAMAGE = serverBuilder.comment("Hypno Spider projectile damage").defineInRange("damage", 1d, 1d, 20d);
        HYPNO_DURATION = serverBuilder.comment("Hypno Spider projectile effect duration in seconds").defineInRange("duration", 10, 1, 60);
        serverBuilder.pop();

        serverBuilder.push("Magma Spider Config").push("Magma Spider");
        MAGMA_HEALTH = serverBuilder.comment("Magma Spider max health").defineInRange("health", 26d, 1d, 300d);
        MAGMA_SPEED = serverBuilder.comment("Magma Spider movement speed").defineInRange("speed", 0.3d, 0.1d, 2d);
        MAGMA_DAMAGE = serverBuilder.comment("Magma Spider projectile damage").defineInRange("damage", 3d, 1d, 20d);
        MAGMA_FIRE = serverBuilder.comment("Magma Spider fire duration when hitting").defineInRange("duration", 4, 1, 60);
        serverBuilder.pop();

        serverBuilder.push("Mini Spider Config").push("Mini Spider");
        MINI_HEALTH = serverBuilder.comment("Mini Spider max health").defineInRange("health", 14d, 1d, 300d);
        MINI_SPEED = serverBuilder.comment("Mini Spider movement speed").defineInRange("speed", 0.3d, 0.1d, 2d);
        MINI_DAMAGE = serverBuilder.comment("Mini Spider melee damage").defineInRange("damage", 2d, 1d, 20d);
        serverBuilder.pop();

        serverBuilder.push("Mother Spider Config").push("Mother Spider");
        MOTHER_HEALTH = serverBuilder.comment("Mother Spider max health").defineInRange("health", 22d, 1d, 300d);
        MOTHER_SPEED = serverBuilder.comment("Mother Spider movement speed").defineInRange("speed", 0.25d, 0.1d, 2d);
        MOTHER_DAMAGE = serverBuilder.comment("Mother Spider melee damage").defineInRange("damage", 2d, 1d, 20d);
        MOTHER_GESTATION = serverBuilder.comment("Mother Spider gestation time in seconds").defineInRange("gestation", 6, 1, 100);
        MOTHER_MAX_MINI_FOLLOWERS = serverBuilder.comment("Mother Spider's mini spiders max count").defineInRange("followers", 8, 5, 20);
        serverBuilder.pop();

        serverBuilder.push("Witch Spider Config").push("Witch Spider");
        WITCH_HEALTH = serverBuilder.comment("Witch Spider max health").defineInRange("health", 30d, 1d, 300d);
        WITCH_SPEED = serverBuilder.comment("Witch Spider movement speed").defineInRange("speed", 0.3d, 0.1d, 2d);
        WITCH_FREEZE = serverBuilder.comment("Witch Spider freezing duration in seconds").defineInRange("freeze", 8, 1, 20);
        WITCH_FIREBALL_DAMAGE = serverBuilder.comment("Witch Spider fireball damage").defineInRange("damage", 2d, 1d, 20d);
        serverBuilder.pop();

        serverBuilder.push("Wither Spider Config").push("Wither Spider");
        WITHER_HEALTH = serverBuilder.comment("Wither Spider max health").defineInRange("health", 60d, 1d, 300d);
        WITHER_SPEED = serverBuilder.comment("Wither Spider movement speed").defineInRange("speed", 0.3d, 0.1d, 2d);
        WITHER_MELEE_DAMAGE = serverBuilder.comment("Wither Spider melee damage").defineInRange("melee", 6d, 1d, 20d);
        WITHER_RANGED_EFFECT_DURATION = serverBuilder.comment("Witch Spider wither and slowness duration in seconds").defineInRange("duration", 5, 1, 600);
        WITHER_RANGED_EFFECT_STRENGTH = serverBuilder.comment("Witch Spider wither and slowness amplifier").defineInRange("amplifier", 0, 0, 2);
        serverBuilder.pop();

        serverBuilder.push("Red Eye Config").push("Red Eye");
        RED_EYE_HEALTH = serverBuilder.comment("Red Eye max health").defineInRange("health", 6d, 1d, 300d);
        RED_EYE_SPEED = serverBuilder.comment("Red Eye movement speed").defineInRange("speed", 0.3d, 0.1d, 2d);
        RED_EYE_DAMAGE = serverBuilder.comment("Red Eye zapping damage").defineInRange("freeze", 2, 1, 20);
        serverBuilder.pop();

        serverBuilder.pop();
    }

}
