package dev.mcpecommander.mobultion;

import dev.mcpecommander.mobultion.events.ClientEvents;
import dev.mcpecommander.mobultion.events.CommonEvents;
import dev.mcpecommander.mobultion.setup.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

/* Created by McpeCommander on 2021/06/18 */
@Mod(Mobultion.MODID)
public class Mobultion
{
    public static final String MODID = "mobultion";
    public static final boolean DEBUG = false;

    private static final Logger LOGGER = LogManager.getLogger();


    public Mobultion() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        GeckoLibMod.DISABLE_IN_DEV = true;
        GeckoLib.initialize();
        Registration.init();

        FMLJavaModLoadingContext.get().getModEventBus().register(ModSetup.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(ClientSetup.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(Registration.class);
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        MinecraftForge.EVENT_BUS.register(SpawnSetup.class);
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);

    }




}
