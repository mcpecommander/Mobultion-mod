package dev.mcpecommander.mobultion;

import dev.mcpecommander.mobultion.entities.skeletons.entities.JokerSkeletonEntity;
import dev.mcpecommander.mobultion.entities.spiders.entities.*;
import dev.mcpecommander.mobultion.setup.ClientSetup;
import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

/* Created by McpeCommander on 2021/06/18 */
@Mod(Mobultion.MODID)
public class Mobultion
{
    public static final String MODID = "mobultion";

    private static final Logger LOGGER = LogManager.getLogger();

    public Mobultion() {

        GeckoLib.initialize();
        Registration.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerEntityAttributes);

    }

    public void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(Registration.ANGELSPIDER.get(), AngelSpiderEntity.createAttributes().build());
        event.put(Registration.WITCHSPIDER.get(), WitchSpiderEntity.createAttributes().build());
        event.put(Registration.HYPNOSPIDER.get(), HypnoSpiderEntity.createAttributes().build());
        event.put(Registration.MAGMASPIDER.get(), MagmaSpiderEntity.createAttributes().build());
        event.put(Registration.WITHERSPIDER.get(), WitherSpiderEntity.createAttributes().build());
        event.put(Registration.WITHERHEADBUG.get(), WitherHeadBugEntity.createAttributes().build());
        event.put(Registration.JOKERSKELETON.get(), JokerSkeletonEntity.createAttributes().build());
    }


}
