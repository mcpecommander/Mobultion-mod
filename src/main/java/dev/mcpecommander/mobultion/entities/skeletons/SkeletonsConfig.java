package dev.mcpecommander.mobultion.entities.skeletons;

import net.minecraftforge.common.ForgeConfigSpec;

/* McpeCommander created on 29/08/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons */
public class SkeletonsConfig {

    //Corrupted Skeleton Values
    public static ForgeConfigSpec.DoubleValue CORRUPTED_HEALTH;
    public static ForgeConfigSpec.DoubleValue CORRUPTED_SPEED;
    public static ForgeConfigSpec.DoubleValue CORRUPTED_DAMAGE;
    public static ForgeConfigSpec.IntValue CORRUPTED_RADIUS;
    //Forest Skeleton Values
    public static ForgeConfigSpec.DoubleValue FOREST_HEALTH;
    public static ForgeConfigSpec.DoubleValue FOREST_SPEED;
    public static ForgeConfigSpec.DoubleValue FOREST_DAMAGE;
    public static ForgeConfigSpec.IntValue FOREST_RADIUS;
    public static ForgeConfigSpec.IntValue FOREST_ACCURACY;
    //Joker Skeleton Values
    public static ForgeConfigSpec.DoubleValue JOKER_HEALTH;
    public static ForgeConfigSpec.DoubleValue JOKER_SPEED;
    public static ForgeConfigSpec.DoubleValue JOKER_DAMAGE;
    public static ForgeConfigSpec.IntValue JOKER_RADIUS;
    public static ForgeConfigSpec.IntValue JOKER_ACCURACY;
    //Magma Skeleton Values
    public static ForgeConfigSpec.DoubleValue MAGMA_HEALTH;
    public static ForgeConfigSpec.DoubleValue MAGMA_SPEED;
    public static ForgeConfigSpec.DoubleValue MAGMA_DAMAGE;
    public static ForgeConfigSpec.IntValue MAGMA_RADIUS;
    public static ForgeConfigSpec.IntValue MAGMA_ACCURACY;
    //TODO: Add a config for setting entities on fire with the magma arrow.
    //Shaman Skeleton Values
    public static ForgeConfigSpec.DoubleValue SHAMAN_HEALTH;
    public static ForgeConfigSpec.DoubleValue SHAMAN_SPEED;
    public static ForgeConfigSpec.DoubleValue SHAMAN_HEALING;
    public static ForgeConfigSpec.IntValue SHAMAN_RADIUS;
    //Vampire Skeleton Values
    public static ForgeConfigSpec.DoubleValue VAMPIRE_HEALTH;
    public static ForgeConfigSpec.DoubleValue VAMPIRE_SPEED;
    public static ForgeConfigSpec.DoubleValue VAMPIRE_DAMAGE;
    public static ForgeConfigSpec.IntValue VAMPIRE_RADIUS;

    public static void setupSkeletonsConfig(ForgeConfigSpec.Builder serverBuilder){
        serverBuilder.comment("Skeletons Config").push("Skeletons");

        serverBuilder.push("Corrupted Skeleton Config").push("Corrupted Skeleton");
        CORRUPTED_HEALTH = serverBuilder.comment("Corrupted Skeleton max health").defineInRange("health", 35d, 1d, 300d);
        CORRUPTED_SPEED = serverBuilder.comment("Corrupted Skeleton movement speed").defineInRange("speed", 0.4d, 0.1d, 2d);
        CORRUPTED_DAMAGE = serverBuilder.comment("Corrupted Skeleton melee damage").defineInRange("damage", 6d, 0d, 40d);
        CORRUPTED_RADIUS = serverBuilder.comment("Corrupted Skeleton follow range").defineInRange("range", 20, 4, 200);
        serverBuilder.pop();

        serverBuilder.push("Forest Skeleton Config").push("Forest Skeleton");
        FOREST_HEALTH = serverBuilder.comment("Forest Skeleton max health").defineInRange("health", 35d, 1d, 300d);
        FOREST_SPEED = serverBuilder.comment("Forest Skeleton movement speed").defineInRange("speed", 0.4d, 0.1d, 2d);
        FOREST_DAMAGE = serverBuilder.comment("Forest Skeleton ranged damage").defineInRange("damage", 3d, 0d, 40d);
        FOREST_RADIUS = serverBuilder.comment("Forest Skeleton follow range").defineInRange("range", 20, 4, 200);
        FOREST_ACCURACY = serverBuilder.comment("Forest Skeleton shooting accuracy", "The higher the number, the worse the aim is.")
                .defineInRange("accuracy", 0, 0, 8);
        serverBuilder.pop();

        serverBuilder.push("Joker Skeleton Config").push("Joker Skeleton");
        JOKER_HEALTH = serverBuilder.comment("Joker Skeleton max health").defineInRange("health", 12d, 1d, 300d);
        JOKER_SPEED = serverBuilder.comment("Joker Skeleton movement speed").defineInRange("speed", 0.6d, 0.1d, 2d);
        JOKER_DAMAGE = serverBuilder.comment("Joker Skeleton ranged damage").defineInRange("damage", 0.5d, 0d, 40d);
        JOKER_RADIUS = serverBuilder.comment("Joker Skeleton follow range").defineInRange("range", 50, 4, 200);
        JOKER_ACCURACY = serverBuilder.comment("Joker Skeleton shooting accuracy", "The higher the number, the worse the aim is")
                .defineInRange("accuracy", 6, 0, 8);
        serverBuilder.pop();

        serverBuilder.push("Magma Skeleton Config").push("Magma Skeleton");
        MAGMA_HEALTH = serverBuilder.comment("Magma Skeleton max health").defineInRange("health", 24d, 1d, 300d);
        MAGMA_SPEED = serverBuilder.comment("Magma Skeleton movement speed").defineInRange("speed", 0.4d, 0.1d, 2d);
        MAGMA_DAMAGE = serverBuilder.comment("Magma Skeleton ranged damage").defineInRange("damage", 1.5d, 0d, 40d);
        MAGMA_RADIUS = serverBuilder.comment("Magma Skeleton follow range").defineInRange("range", 16, 4, 200);
        MAGMA_ACCURACY = serverBuilder.comment("Magma Skeleton shooting accuracy", "The higher the number, the worse the aim is")
                .defineInRange("accuracy", 3, 0, 8);
        serverBuilder.pop();

        serverBuilder.push("Shaman Skeleton Config").push("Shaman Skeleton");
        SHAMAN_HEALTH = serverBuilder.comment("Shaman Skeleton max health").defineInRange("health", 18d, 1d, 300d);
        SHAMAN_SPEED = serverBuilder.comment("Shaman Skeleton movement speed").defineInRange("speed", 0.3d, 0.1d, 2d);
        SHAMAN_HEALING = serverBuilder.comment("Shaman Skeleton healing amount").defineInRange("healing", 4d, 0d, 40d);
        SHAMAN_RADIUS = serverBuilder.comment("Shaman Skeleton follow range").defineInRange("range", 64, 4, 200);
        serverBuilder.pop();

        serverBuilder.push("Vampire Skeleton Config").push("Vampire Skeleton");
        VAMPIRE_HEALTH = serverBuilder.comment("Vampire Skeleton max health").defineInRange("health", 30d, 1d, 300d);
        VAMPIRE_SPEED = serverBuilder.comment("Vampire Skeleton movement speed").defineInRange("speed", 0.5d, 0.1d, 2d);
        VAMPIRE_DAMAGE = serverBuilder.comment("Vampire Skeleton melee damage").defineInRange("damage", 6d, 0d, 40d);
        VAMPIRE_RADIUS = serverBuilder.comment("Vampire Skeleton follow range").defineInRange("range", 26, 4, 200);
        serverBuilder.pop();

        serverBuilder.pop();

    }

}
