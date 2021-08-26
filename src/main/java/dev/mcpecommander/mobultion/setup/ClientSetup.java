package dev.mcpecommander.mobultion.setup;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import dev.mcpecommander.mobultion.blocks.renderer.SpiderEggRenderer;
import dev.mcpecommander.mobultion.entities.endermen.renderers.*;
import dev.mcpecommander.mobultion.entities.skeletons.entities.CrossArrowEntity;
import dev.mcpecommander.mobultion.entities.skeletons.renderers.BaseSkeletonRenderer;
import dev.mcpecommander.mobultion.entities.skeletons.renderers.HeartArrowRenderer;
import dev.mcpecommander.mobultion.entities.skeletons.renderers.JokerSkeletonRenderer;
import dev.mcpecommander.mobultion.entities.spiders.renderers.*;
import dev.mcpecommander.mobultion.entities.zombies.renderers.*;
import dev.mcpecommander.mobultion.particles.FlowerParticle;
import dev.mcpecommander.mobultion.particles.HealParticle;
import dev.mcpecommander.mobultion.particles.PortalParticle;
import dev.mcpecommander.mobultion.particles.SnowFlakeParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientSetup {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);

    public static final RegistryObject<ParticleType<HealParticle.HealParticleData>> HEAL_PARTICLE_TYPE =
            PARTICLE_TYPES.register("healparticle", () -> new ParticleType<HealParticle.HealParticleData>(false,
                    HealParticle.HealParticleData.DESERIALIZER) {
        @Nonnull
        @Override
        public Codec<HealParticle.HealParticleData> codec() {
            return HealParticle.HealParticleData.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<PortalParticle.PortalParticleData>> PORTAL_PARTICLE_TYPE =
            PARTICLE_TYPES.register("portalparticle", () -> new ParticleType<PortalParticle.PortalParticleData>(false,
                    PortalParticle.PortalParticleData.DESERIALIZER) {
        @Nonnull
        @Override
        public Codec<PortalParticle.PortalParticleData> codec() {
            return PortalParticle.PortalParticleData.CODEC;
        }
    });

    public static final RegistryObject<ParticleType<SnowFlakeParticle.SnowFlakeParticleData>> SNOW_FLAKE_PARTICLE_TYPE =
            PARTICLE_TYPES.register("snowflakeparticle", () -> new ParticleType<SnowFlakeParticle.SnowFlakeParticleData>(false,
                    SnowFlakeParticle.SnowFlakeParticleData.DESERIALIZER) {
        @Nonnull
        @Override
        public Codec<SnowFlakeParticle.SnowFlakeParticleData> codec() {
            return SnowFlakeParticle.SnowFlakeParticleData.CODEC;
        }
    });

    public static final RegistryObject<ParticleType<FlowerParticle.FlowerParticleData>> FLOWER_PARTICLE_TYPE =
            PARTICLE_TYPES.register("flowerparticle", () -> new ParticleType<FlowerParticle.FlowerParticleData>(false,
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

    //Register rendering related stuff here.
    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Registration.HAYHAT.get(), RenderType.translucent());

        ClientRegistry.bindTileEntityRenderer(Registration.SPIDEREGG_TILE.get(), SpiderEggRenderer::new);

        ItemModelsProperties.register(Registration.FORESTBOW.get(), new ResourceLocation(MODID, "pull"),
                (stack, world, entity) -> entity != null && entity.getUseItem() == stack ?
                        (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F : 0);
        ItemModelsProperties.register(Registration.FORESTBOW.get(), new ResourceLocation(MODID, "pulling"),
                (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

        ItemModelsProperties.register(Registration.FANGNECKLACE.get(), new ResourceLocation(MODID, "pull"),
                (stack, world, entity) -> entity != null && entity.getUseItem() == stack ?
                        (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 60.0F : 0);
        ItemModelsProperties.register(Registration.FANGNECKLACE.get(), new ResourceLocation(MODID, "pulling"),
                (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

        RenderingRegistry.registerEntityRenderingHandler(Registration.ANGELSPIDER.get(),
                AngelSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.WITCHSPIDER.get(),
                WitchSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.HYPNOSPIDER.get(),
                HypnoSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.HYPNOWAVE.get(),
                HypnoWaveRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.MAGMASPIDER.get(),
                MagmaSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.MOTHERSPIDER.get(),
                MotherSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.MINISPIDER.get(),
                MiniSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.WITHERSPIDER.get(),
                WitherSpiderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.WITHERHEADBUG.get(),
                WitherHeadBugRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(Registration.JOKERSKELETON.get(),
                JokerSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.HEARTARROW.get(),
                HeartArrowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.CORRUPTEDSKELETON.get(),
                BaseSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.VAMPIRESKELETON.get(),
                BaseSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.FORESTSKELETON.get(),
                BaseSkeletonRenderer::new);
        //Use the same renderer as the vanilla arrow for now.
        //TODO: Decide if I want an animated model or just a texture.
        RenderingRegistry.registerEntityRenderingHandler(Registration.CROSSARROW.get(),
                manager -> new ArrowRenderer<CrossArrowEntity>(manager) {

                    @Override
                    public boolean shouldRender(CrossArrowEntity p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
                        //System.out.println(super.shouldRender(p_225626_1_, p_225626_2_, p_225626_3_, p_225626_5_, p_225626_7_));
                        return super.shouldRender(p_225626_1_, p_225626_2_, p_225626_3_, p_225626_5_, p_225626_7_);
                    }

                    @Override
                    public void render(CrossArrowEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
                        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
                        //System.out.println("hello");
                    }

                    @Nonnull
                    @Override
                    public ResourceLocation getTextureLocation(@Nonnull CrossArrowEntity arrow) {
                        return new ResourceLocation("textures/entity/projectiles/arrow.png");
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(Registration.SHAMANSKELETON.get(),
                BaseSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.MAGMASKELETON.get(),
                BaseSkeletonRenderer::new);

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
        RenderingRegistry.registerEntityRenderingHandler(Registration.GARDENERENDERMAN.get(),
                GardenerEndermanRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(Registration.KNIGHTZOMBIE.get(),
                KnightZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.WORKERZOMBIE.get(),
                WorkerZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.MAGMAZOMBIE.get(),
                MagmaZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.DOCTORZOMBIE.get(),
                DoctorZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.HUNGRYZOMBIE.get(),
                HungryZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.GOROZOMBIE.get(),
                GoroZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.GENIEZOMBIE.get(),
                GenieZombieRenderer::new);
    }


}
