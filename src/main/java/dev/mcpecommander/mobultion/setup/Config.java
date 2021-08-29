package dev.mcpecommander.mobultion.setup;

import dev.mcpecommander.mobultion.entities.endermen.EndermenConfig;
import net.minecraftforge.common.ForgeConfigSpec;

/* Created by McpeCommander on 2021/06/18 */
public class Config {

    public static ForgeConfigSpec SERVER_CONFIG;

    static{
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        EndermenConfig.setupEndermenConfig(SERVER_BUILDER);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

}
