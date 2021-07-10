package dev.mcpecommander.mobultion.setup;

import com.mojang.serialization.Codec;
import dev.mcpecommander.mobultion.entities.endermen.renderers.*;
import dev.mcpecommander.mobultion.entities.skeletons.renderers.JokerSkeletonRenderer;
import dev.mcpecommander.mobultion.entities.spiders.renderers.*;
import dev.mcpecommander.mobultion.particles.HealParticle;
import dev.mcpecommander.mobultion.particles.PortalParticle;
import dev.mcpecommander.mobultion.particles.SnowFlakeParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientSetup {

    public static final ParticleType<HealParticle.HealParticleData> HEAL_PARTICLE_TYPE = new ParticleType<HealParticle.HealParticleData>(false, HealParticle.HealParticleData.DESERIALIZER) {
        @Override
        public Codec<HealParticle.HealParticleData> codec() {
            return HealParticle.HealParticleData.CODEC;
        }
    };

    public static final ParticleType<PortalParticle.PortalParticleData> PORTAL_PARTICLE_TYPE = new ParticleType<PortalParticle.PortalParticleData>(false, PortalParticle.PortalParticleData.DESERIALIZER) {
        @Override
        public Codec<PortalParticle.PortalParticleData> codec() {
            return PortalParticle.PortalParticleData.CODEC;
        }
    };

    public static final ParticleType<SnowFlakeParticle.SnowFlakeParticleData> SNOW_FLAKE_PARTICLE_TYPE = new ParticleType<SnowFlakeParticle.SnowFlakeParticleData>(false, SnowFlakeParticle.SnowFlakeParticleData.DESERIALIZER) {
        @Override
        public Codec<SnowFlakeParticle.SnowFlakeParticleData> codec() {
            return SnowFlakeParticle.SnowFlakeParticleData.CODEC;
        }
    };

    //FOR SOME FUCKING FORGE REASON using deferred registry makes the factory load before the sprite is loaded.
    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
        event.getRegistry().register(HEAL_PARTICLE_TYPE.setRegistryName(MODID, "healparticle"));
        event.getRegistry().register(PORTAL_PARTICLE_TYPE.setRegistryName(MODID, "portalparticle"));
        event.getRegistry().register(SNOW_FLAKE_PARTICLE_TYPE.setRegistryName(MODID, "snowflakeparticle"));
    }

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event){
        Minecraft.getInstance().particleEngine.register(HEAL_PARTICLE_TYPE, HealParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(PORTAL_PARTICLE_TYPE, PortalParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(SNOW_FLAKE_PARTICLE_TYPE, SnowFlakeParticle.Factory::new);
    }

    //Register rendering related stuff here.
    @SubscribeEvent
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
        RenderingRegistry.registerEntityRenderingHandler(Registration.WANDERINGENDERMAN.get(),
                WanderingEndermanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.MAGMAENDERMAN.get(),
                MagmaEndermanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.GLASSENDERMAN.get(),
                GlassEndermanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.GLASSSHOT.get(),
                GlassShotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.ICEENDERMAN.get(),
                IceEndermanRenderer::new);
    }


}
