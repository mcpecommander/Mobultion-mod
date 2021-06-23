package dev.mcpecommander.mobultion.setup;

import dev.mcpecommander.mobultion.Mobultion;
import dev.mcpecommander.mobultion.entities.skeletons.renderers.JokerSkeletonRenderer;
import dev.mcpecommander.mobultion.entities.spiders.renderers.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/* Created by McpeCommander on 2021/06/18 */
@Mod.EventBusSubscriber(modid = Mobultion.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientSetup {

    //Register rendering related stuff here.
    public static void init(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Registration.TESTBLOCK.get(), RenderType.translucent());
        RenderingRegistry.registerEntityRenderingHandler(Registration.ANGELSPIDER.get(),
                AngelSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.WITCHSPIDER.get(),
                WitchSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.HYPNOSPIDER.get(),
                HypnoSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.MAGMASPIDER.get(),
                MagmaSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.WITHERSPIDER.get(),
                WitherSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.WITHERHEADBUG.get(),
                WitherHeadBugRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.JOKERSKELETON.get(),
                JokerSkeletonRenderer::new);
        //Minecraft.getInstance().particleEngine.register(Registration.HEAL_PARTICLE.get(), HealParticle.Factory::new);
    }
}
