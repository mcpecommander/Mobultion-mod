package dev.mcpecommander.mobultion.setup;

import com.mojang.serialization.Codec;
import dev.mcpecommander.mobultion.blocks.renderer.SpiderEggRenderer;
import dev.mcpecommander.mobultion.entities.endermen.renderers.*;
import dev.mcpecommander.mobultion.entities.skeletons.entities.CrossArrowEntity;
import dev.mcpecommander.mobultion.entities.skeletons.renderers.BaseSkeletonRenderer;
import dev.mcpecommander.mobultion.entities.skeletons.renderers.HeartArrowRenderer;
import dev.mcpecommander.mobultion.entities.skeletons.renderers.JokerSkeletonRenderer;
import dev.mcpecommander.mobultion.entities.skeletons.renderers.MiniLightningRenderer;
import dev.mcpecommander.mobultion.entities.spiders.renderers.*;
import dev.mcpecommander.mobultion.entities.zombies.renderers.*;
import dev.mcpecommander.mobultion.particles.FlowerParticle;
import dev.mcpecommander.mobultion.particles.HealParticle;
import dev.mcpecommander.mobultion.particles.PortalParticle;
import dev.mcpecommander.mobultion.particles.SnowFlakeParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientSetup {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);

    public static final RegistryObject<ParticleType<HealParticle.HealParticleData>> HEAL_PARTICLE_TYPE =
            PARTICLE_TYPES.register("healparticle", () -> new ParticleType<>(false,
                    HealParticle.HealParticleData.DESERIALIZER) {
                @Nonnull
                @Override
                public Codec<HealParticle.HealParticleData> codec() {
                    return HealParticle.HealParticleData.CODEC;
                }
            });
    public static final RegistryObject<ParticleType<PortalParticle.PortalParticleData>> PORTAL_PARTICLE_TYPE =
            PARTICLE_TYPES.register("portalparticle", () -> new ParticleType<>(false,
                    PortalParticle.PortalParticleData.DESERIALIZER) {
                @Nonnull
                @Override
                public Codec<PortalParticle.PortalParticleData> codec() {
                    return PortalParticle.PortalParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<SnowFlakeParticle.SnowFlakeParticleData>> SNOW_FLAKE_PARTICLE_TYPE =
            PARTICLE_TYPES.register("snowflakeparticle", () -> new ParticleType<>(false,
                    SnowFlakeParticle.SnowFlakeParticleData.DESERIALIZER) {
                @Nonnull
                @Override
                public Codec<SnowFlakeParticle.SnowFlakeParticleData> codec() {
                    return SnowFlakeParticle.SnowFlakeParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<FlowerParticle.FlowerParticleData>> FLOWER_PARTICLE_TYPE =
            PARTICLE_TYPES.register("flowerparticle", () -> new ParticleType<>(false,
                    FlowerParticle.FlowerParticleData.DESERIALIZER) {
                @Nonnull
                @Override
                public Codec<FlowerParticle.FlowerParticleData> codec() {
                    return FlowerParticle.FlowerParticleData.CODEC;
                }
            });


    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event){
        Minecraft.getInstance().particleEngine.register(HEAL_PARTICLE_TYPE.get(), HealParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(PORTAL_PARTICLE_TYPE.get(), PortalParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(SNOW_FLAKE_PARTICLE_TYPE.get(), SnowFlakeParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(FLOWER_PARTICLE_TYPE.get(), FlowerParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {

        event.registerBlockEntityRenderer(Registration.SPIDEREGG_TILE.get(), SpiderEggRenderer::new);

        event.registerEntityRenderer(Registration.ANGELSPIDER.get(),
                AngelSpiderRenderer::new);
        event.registerEntityRenderer(Registration.WITCHSPIDER.get(),
                WitchSpiderRenderer::new);
        event.registerEntityRenderer(Registration.HYPNOSPIDER.get(),
                HypnoSpiderRenderer::new);
        event.registerEntityRenderer(Registration.HYPNOWAVE.get(),
                HypnoWaveRenderer::new);
        event.registerEntityRenderer(Registration.MAGMASPIDER.get(),
                MagmaSpiderRenderer::new);
        event.registerEntityRenderer(Registration.MOTHERSPIDER.get(),
                MotherSpiderRenderer::new);
        event.registerEntityRenderer(Registration.MINISPIDER.get(),
                MiniSpiderRenderer::new);
        event.registerEntityRenderer(Registration.WITHERSPIDER.get(),
                WitherSpiderRenderer::new);
        event.registerEntityRenderer(Registration.WITHERHEADBUG.get(),
                WitherHeadBugRenderer::new);

        event.registerEntityRenderer(Registration.JOKERSKELETON.get(),
                JokerSkeletonRenderer::new);
        event.registerEntityRenderer(Registration.HEARTARROW.get(),
                HeartArrowRenderer::new);
        event.registerEntityRenderer(Registration.CORRUPTEDSKELETON.get(),
                BaseSkeletonRenderer::new);
        event.registerEntityRenderer(Registration.VAMPIRESKELETON.get(),
                BaseSkeletonRenderer::new);
        event.registerEntityRenderer(Registration.FORESTSKELETON.get(),
                BaseSkeletonRenderer::new);

        //Use the same renderer as the vanilla arrow for now.
        //TODO: Decide if I want an animated model or just a texture.
        event.registerEntityRenderer(Registration.CROSSARROW.get(),
                manager -> new ArrowRenderer<>(manager) {
                    @Nonnull
                    @Override
                    public ResourceLocation getTextureLocation(@Nonnull CrossArrowEntity arrow) {
                        return new ResourceLocation("textures/entity/projectiles/arrow.png");
                    }
                });
        event.registerEntityRenderer(Registration.SHAMANSKELETON.get(),
                BaseSkeletonRenderer::new);
        event.registerEntityRenderer(Registration.MINILIGHTNING.get(),
                MiniLightningRenderer::new);
        event.registerEntityRenderer(Registration.MAGMASKELETON.get(),
                BaseSkeletonRenderer::new);

        event.registerEntityRenderer(Registration.WANDERINGENDERMAN.get(),
                WanderingEndermanRenderer::new);
        event.registerEntityRenderer(Registration.MAGMAENDERMAN.get(),
                MagmaEndermanRenderer::new);
        event.registerEntityRenderer(Registration.GLASSENDERMAN.get(),
                GlassEndermanRenderer::new);
        event.registerEntityRenderer(Registration.GLASSSHOT.get(),
                GlassShotRenderer::new);
        event.registerEntityRenderer(Registration.ICEENDERMAN.get(),
                IceEndermanRenderer::new);
        event.registerEntityRenderer(Registration.GARDENERENDERMAN.get(),
                GardenerEndermanRenderer::new);

        event.registerEntityRenderer(Registration.KNIGHTZOMBIE.get(),
                KnightZombieRenderer::new);
        event.registerEntityRenderer(Registration.WORKERZOMBIE.get(),
                WorkerZombieRenderer::new);
        event.registerEntityRenderer(Registration.MAGMAZOMBIE.get(),
                MagmaZombieRenderer::new);
        event.registerEntityRenderer(Registration.DOCTORZOMBIE.get(),
                DoctorZombieRenderer::new);
        event.registerEntityRenderer(Registration.HUNGRYZOMBIE.get(),
                HungryZombieRenderer::new);
        event.registerEntityRenderer(Registration.GOROZOMBIE.get(),
                GoroZombieRenderer::new);
        event.registerEntityRenderer(Registration.GENIEZOMBIE.get(),
                GenieZombieRenderer::new);
    }

    //Register rendering related stuff here.
    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Registration.HAYHAT.get(), RenderType.translucent());

        ItemProperties.register(Registration.FORESTBOW.get(), new ResourceLocation(MODID, "pull"),
                (stack, world, entity, id) -> entity != null && entity.getUseItem() == stack ?
                        (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F : 0);
        ItemProperties.register(Registration.FORESTBOW.get(), new ResourceLocation(MODID, "pulling"),
                (stack, world, entity, id) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

        ItemProperties.register(Registration.FANGNECKLACE.get(), new ResourceLocation(MODID, "pull"),
                (stack, world, entity, id) -> entity != null && entity.getUseItem() == stack ?
                        (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 60.0F : 0);
        ItemProperties.register(Registration.FANGNECKLACE.get(), new ResourceLocation(MODID, "pulling"),
                (stack, world, entity, id) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);


    }


}
