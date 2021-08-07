package dev.mcpecommander.mobultion.setup;

import dev.mcpecommander.mobultion.blocks.HayHatBlock;
import dev.mcpecommander.mobultion.effects.JokernessEffect;
import dev.mcpecommander.mobultion.entities.endermen.entities.*;
import dev.mcpecommander.mobultion.entities.skeletons.entities.*;
import dev.mcpecommander.mobultion.entities.spiders.entities.*;
import dev.mcpecommander.mobultion.entities.zombies.entities.*;
import dev.mcpecommander.mobultion.items.*;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, MODID);
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    private static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZER = DeferredRegister.
            create(ForgeRegistries.DATA_SERIALIZERS, MODID);

    public static void init() {

        ClientSetup.PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        DATA_SERIALIZER.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {

        event.put(ANGELSPIDER.get(), AngelSpiderEntity.createAttributes().build());
        event.put(WITCHSPIDER.get(), WitchSpiderEntity.createAttributes().build());
        event.put(HYPNOSPIDER.get(), HypnoSpiderEntity.createAttributes().build());
        event.put(MAGMASPIDER.get(), MagmaSpiderEntity.createAttributes().build());
        event.put(WITHERSPIDER.get(), WitherSpiderEntity.createAttributes().build());
        event.put(WITHERHEADBUG.get(), WitherHeadBugEntity.createAttributes().build());

        event.put(JOKERSKELETON.get(), JokerSkeletonEntity.createAttributes().build());
        event.put(CORRUPTEDSKELETON.get(), CorruptedSkeletonEntity.createAttributes().build());
        event.put(VAMPIRESKELETON.get(), VampireSkeletonEntity.createAttributes().build());
        event.put(FORESTSKELETON.get(), ForestSkeletonEntity.createAttributes().build());
        event.put(SHAMANSKELETON.get(), ShamanSkeletonEntity.createAttributes().build());
        event.put(MAGMASKELETON.get(), MagmaSkeletonEntity.createAttributes().build());

        event.put(WANDERINGENDERMAN.get(), WanderingEndermanEntity.createAttributes().build());
        event.put(MAGMAENDERMAN.get(), MagmaEndermanEntity.createAttributes().build());
        event.put(GLASSENDERMAN.get(), GlassEndermanEntity.createAttributes().build());
        event.put(ICEENDERMAN.get(), IceEndermanEntity.createAttributes().build());
        event.put(GARDENERENDERMAN.get(), IceEndermanEntity.createAttributes().build());
        EntitySpawnPlacementRegistry.register(GARDENERENDERMAN.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                GardenerEndermanEntity::checkMobSpawnRules);

        event.put(KNIGHTZOMBIE.get(), KnightZombieEntity.createAttributes().build());
        event.put(WORKERZOMBIE.get(), WorkerZombieEntity.createAttributes().build());
        event.put(MAGMAZOMBIE.get(), MagmaZombieEntity.createAttributes().build());
        event.put(DOCTORZOMBIE.get(), DoctorZombieEntity.createAttributes().build());
        event.put(HUNGRYZOMBIE.get(), HungryZombieEntity.createAttributes().build());
        event.put(GOROZOMBIE.get(), GoroZombieEntity.createAttributes().build());
        event.put(GENIEZOMBIE.get(), GenieZombieEntity.createAttributes().build());
    }

    //This is not inlined in the deferred register because .get() returns a generic IDataSerializer.
    public static final IDataSerializer<List<BlockPos>> BLOCKPOS_LIST = new IDataSerializer<List<BlockPos>>() {
        @Override
        public void write(PacketBuffer buffer, List<BlockPos> list) {
            buffer.writeInt(list.size());
            for(BlockPos pos : list){
                buffer.writeBlockPos(pos);
            }
        }

        @Nonnull
        @Override
        public List<BlockPos> read(PacketBuffer buffer) {
            int size = buffer.readInt();
            ArrayList<BlockPos> positions = new ArrayList<>();
            for(int i = 0; i < size; i++){
                positions.add(buffer.readBlockPos());
            }
            return positions;
        }

        @Nonnull
        @Override
        public List<BlockPos> copy(@Nonnull List<BlockPos> list) {
            return list;
        }
    };
    private static final RegistryObject<DataSerializerEntry> BLOCKPOS_LIST_REGISTER = DATA_SERIALIZER.register("blocklist",
            () -> new DataSerializerEntry(BLOCKPOS_LIST));

    public static final RegistryObject<HayHatBlock> HAYHAT = BLOCKS.register("hayhatblock", HayHatBlock::new);
    public static final RegistryObject<Item> HAYHATBLOCK_ITEM = ITEMS.register("hayhatblock", () -> new HayHatBlockItem(HAYHAT.get()));

    public static final RegistryObject<ThunderStaffItem> THUNDERSTAFF = ITEMS.register("thunderstaffitem", ThunderStaffItem::new);
    public static final RegistryObject<ForestBowItem> FORESTBOW = ITEMS.register("forestbowitem", ForestBowItem::new);
    public static final RegistryObject<HeartArrowItem> HEARTARROW_ITEM = ITEMS.register("heartarrowitem", HeartArrowItem::new);
    public static final RegistryObject<Item> CORRUPTEDBONE = ITEMS.register("corruptedboneitem",
            () -> new Item(new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<CorruptedBoneMealItem> CORRUPTEDBONEMEAL = ITEMS.register("corruptedbonemealitem",
            CorruptedBoneMealItem::new);
    public static final RegistryObject<HealingStaffItem> HEALINGSTAFF = ITEMS.register("healingstaffitem", HealingStaffItem::new);
    public static final RegistryObject<Item> FANG = ITEMS.register("fangitem", () ->
            new Item(new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<HardHatItem> HARDHAT = ITEMS.register("hardhatitem", HardHatItem::new);
    public static final RegistryObject<HammerItem> HAMMER = ITEMS.register("hammeritem", HammerItem::new);
    public static final RegistryObject<HealthPackItem> HEALTHPACK = ITEMS.register("healthpackitem", HealthPackItem::new);
    public static final RegistryObject<Item> KNIFE = ITEMS.register("knifeitem", () ->
            new Item(new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> FORK = ITEMS.register("forkitem", () ->
            new Item(new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<LampItem> LAMP = ITEMS.register("lampitem", LampItem::new);
    public static final RegistryObject<EnderFlakeItem> ENDERFLAKE = ITEMS.register("enderflakeitem", EnderFlakeItem::new);
    public static final RegistryObject<EnderBlazeItem> ENDERBLAZE = ITEMS.register("enderblazeitem", EnderBlazeItem::new);
    public static final RegistryObject<GlassShotItem> GLASSSHOT_ITEM = ITEMS.register("glassshotitem", GlassShotItem::new);
    public static final RegistryObject<HaloItem> HALO = ITEMS.register("haloitem", HaloItem::new);
    public static final RegistryObject<Item> FLAMINGLEG = ITEMS.register("flaminglegitem", () ->
            new Item(new Item.Properties().tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<AngelSpiderEntity> ANGELSPIDER_TYPE = EntityType.Builder.of(AngelSpiderEntity::new,
                    EntityClassification.MONSTER).sized(1.4f, 1f).build("angelspider");
    public static final RegistryObject<EntityType<AngelSpiderEntity>> ANGELSPIDER = ENTITIES.register("angelspider",
            () -> ANGELSPIDER_TYPE);
    public static final RegistryObject<Item> ANGELSPIDER_EGG = ITEMS.register("angelspider_egg"
            , () -> new SpawnEggItem(ANGELSPIDER_TYPE, 0xFFFFFF, 0xFFFF53,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<WitchSpiderEntity> WITCHSPIDER_TYPE = EntityType.Builder.of(WitchSpiderEntity::new,
                    EntityClassification.MONSTER).sized(1.4f, 1f).build("witchspider");
    public static final RegistryObject<EntityType<WitchSpiderEntity>> WITCHSPIDER = ENTITIES.register("witchspider",
            () -> WITCHSPIDER_TYPE);
    public static final RegistryObject<Item> WITCHSPIDER_EGG = ITEMS.register("witchspider_egg"
            , () -> new SpawnEggItem(WITCHSPIDER_TYPE, 0x6AA84F, 0x12436F,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<HypnoSpiderEntity> HYPNOSPIDER_TYPE = EntityType.Builder.of(HypnoSpiderEntity::new,
                    EntityClassification.MONSTER).sized(1.4f, 1f).build("hypnospider");
    public static final RegistryObject<EntityType<HypnoSpiderEntity>> HYPNOSPIDER = ENTITIES.register("hypnospider",
            () -> HYPNOSPIDER_TYPE);
    public static final RegistryObject<Item> HYPNOSPIDER_EGG = ITEMS.register("hypnospider_egg"
            , () -> new SpawnEggItem(HYPNOSPIDER_TYPE, 0xDD06DD, 0xF736F7,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<MagmaSpiderEntity> MAGMASPIDER_TYPE = EntityType.Builder.of(MagmaSpiderEntity::new,
                    EntityClassification.MONSTER).sized(1.4f, 1f).build("magmaspider");
    public static final RegistryObject<EntityType<MagmaSpiderEntity>> MAGMASPIDER = ENTITIES.register("magmsspider",
            () -> MAGMASPIDER_TYPE);
    public static final RegistryObject<Item> MAGMASPIDER_EGG = ITEMS.register("magmaspider_egg"
            , () -> new SpawnEggItem(MAGMASPIDER_TYPE, 0x230E0E, 0xF01414,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<WitherSpiderEntity> WITHERSPIDER_TYPE = EntityType.Builder.of(WitherSpiderEntity::new,
                    EntityClassification.MONSTER).sized(1.4f, 1f).build("witherspider");
    public static final RegistryObject<EntityType<WitherSpiderEntity>> WITHERSPIDER = ENTITIES.register("witherspider",
            () -> WITHERSPIDER_TYPE);
    public static final RegistryObject<Item> WITHERSPIDER_EGG = ITEMS.register("witherspider_egg"
            , () -> new SpawnEggItem(WITHERSPIDER_TYPE, 0x666666, 0x444444,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<WitherHeadBugEntity>> WITHERHEADBUG = ENTITIES.register("witherheadbug",
            () -> EntityType.Builder.of(WitherHeadBugEntity::new, EntityClassification.MONSTER)
                    .sized(0.7f, 0.7f).build("witherheadbug"));

    private static final EntityType<JokerSkeletonEntity> JOKERSKELETON_TYPE = EntityType.Builder.of(JokerSkeletonEntity::new,
                    EntityClassification.MONSTER).sized(0.6F, 1.99F).build("jokerskeleton");
    public static final RegistryObject<EntityType<JokerSkeletonEntity>> JOKERSKELETON = ENTITIES.register("jokerskeleton",
            () -> JOKERSKELETON_TYPE);
    public static final RegistryObject<Item> JOKERSKELETON_EGG = ITEMS.register("jokerskeleton_egg"
            , () -> new SpawnEggItem(JOKERSKELETON_TYPE, 0xFF0000, 0xFFFF00,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<EntityType<HeartArrowEntity>> HEARTARROW = ENTITIES.register("heartarrow",
            () -> EntityType.Builder.of(HeartArrowEntity::new, EntityClassification.MISC)
            .sized(0.5F, 0.5F).build("heartarrow"));

    private static final EntityType<CorruptedSkeletonEntity> CORRUPTEDSKELETON_TYPE = EntityType.Builder.of(CorruptedSkeletonEntity::new,
                    EntityClassification.MONSTER).sized(0.6F, 1.99F).build("corruptedskeleton");
    public static final RegistryObject<EntityType<CorruptedSkeletonEntity>> CORRUPTEDSKELETON = ENTITIES.register("corruptedskeleton",
            () -> CORRUPTEDSKELETON_TYPE);
    public static final RegistryObject<Item> CORRUPTEDSKELETON_EGG = ITEMS.register("corruptedskeleton_egg"
            , () -> new SpawnEggItem(CORRUPTEDSKELETON_TYPE, 0x745F1D, 0x927006,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<VampireSkeletonEntity> VAMPIRESKELETON_TYPE = EntityType.Builder.of(VampireSkeletonEntity::new,
                    EntityClassification.MONSTER).sized(0.6F, 1.99F).build("vampireskeleton");
    public static final RegistryObject<EntityType<VampireSkeletonEntity>> VAMPIRESKELETON = ENTITIES.register("vampireskeleton",
            () -> VAMPIRESKELETON_TYPE);
    public static final RegistryObject<Item> VAMPIRESKELETON_EGG = ITEMS.register("vampireskeleton_egg"
            , () -> new SpawnEggItem(VAMPIRESKELETON_TYPE, 0xBB8A8A, 0x540D0D,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<ForestSkeletonEntity> FORESTSKELETON_TYPE = EntityType.Builder.of(ForestSkeletonEntity::new,
                    EntityClassification.MONSTER).sized(0.6F, 1.99F).build("forestskeleton");
    public static final RegistryObject<EntityType<ForestSkeletonEntity>> FORESTSKELETON = ENTITIES.register("forestskeleton",
            () -> FORESTSKELETON_TYPE);
    public static final RegistryObject<Item> FORESTSKELETON_EGG = ITEMS.register("forestskeleton_egg"
            , () -> new SpawnEggItem(FORESTSKELETON_TYPE, 0x38761D, 0x93C47D,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<ShamanSkeletonEntity> SHAMANSKELETON_TYPE = EntityType.Builder.of(ShamanSkeletonEntity::new,
                    EntityClassification.MONSTER).sized(0.6F, 1.99F).build("shamanskeleton");
    public static final RegistryObject<EntityType<ShamanSkeletonEntity>> SHAMANSKELETON = ENTITIES.register("shamanskeleton",
            () -> SHAMANSKELETON_TYPE);
    public static final RegistryObject<Item> SHAMANSKELETON_EGG = ITEMS.register("shamanskeleton_egg"
            , () -> new SpawnEggItem(SHAMANSKELETON_TYPE, 0x050572, 0x741B47,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<MagmaSkeletonEntity> MAGMASKELETON_TYPE = EntityType.Builder.of(MagmaSkeletonEntity::new,
                    EntityClassification.MONSTER).sized(0.6F, 1.99F).build("magmaskeleton");
    public static final RegistryObject<EntityType<MagmaSkeletonEntity>> MAGMASKELETON = ENTITIES.register("magmaskeleton",
            () -> MAGMASKELETON_TYPE);
    public static final RegistryObject<Item> MAGMASKELETON_EGG = ITEMS.register("magmaskeleton_egg"
            , () -> new SpawnEggItem(MAGMASKELETON_TYPE, 0x811616, 0xFD1D1D,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<WanderingEndermanEntity> WANDERINGENDERMAN_TYPE = EntityType.Builder.of(WanderingEndermanEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.9F).build("wanderingenderman");
    public static final RegistryObject<EntityType<WanderingEndermanEntity>> WANDERINGENDERMAN = ENTITIES.register("wanderingenderman",
            () -> WANDERINGENDERMAN_TYPE);
    public static final RegistryObject<Item> WANDERINGENDERMAN_EGG = ITEMS.register("wanderingenderman_egg"
            , () -> new SpawnEggItem(WANDERINGENDERMAN_TYPE, 0x422C01, 0x036303,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<MagmaEndermanEntity> MAGMAENDERMAN_TYPE = EntityType.Builder.of(MagmaEndermanEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.9F).build("magmaenderman");
    public static final RegistryObject<EntityType<MagmaEndermanEntity>> MAGMAENDERMAN = ENTITIES.register("magmaenderman",
            () -> MAGMAENDERMAN_TYPE);
    public static final RegistryObject<Item> MAGMAENDERMAN_EGG = ITEMS.register("magmaenderman_egg"
            , () -> new SpawnEggItem(MAGMAENDERMAN_TYPE, 0xB40A0A, 0xE7E740,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<GlassEndermanEntity> GLASSENDERMAN_TYPE = EntityType.Builder.of(GlassEndermanEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.9F).build("glassenderman");
    public static final RegistryObject<EntityType<GlassEndermanEntity>> GLASSENDERMAN = ENTITIES.register("glassenderman",
            () -> GLASSENDERMAN_TYPE);
    public static final RegistryObject<Item> GLASSENDERMAN_EGG = ITEMS.register("glassenderman_egg"
            , () -> new SpawnEggItem(GLASSENDERMAN_TYPE, 0x2D2C2F, 0x535056,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<GlassShotEntity>> GLASSSHOT = ENTITIES.register("glassshot",
            () -> EntityType.Builder.of((EntityType.IFactory<GlassShotEntity>) GlassShotEntity::new, EntityClassification.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(8).setUpdateInterval(1).build("glassshot"));

    private static final EntityType<IceEndermanEntity> ICEENDERMAN_TYPE = EntityType.Builder.of(IceEndermanEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.9F).build("iceenderman");
    public static final RegistryObject<EntityType<IceEndermanEntity>> ICEENDERMAN = ENTITIES.register("iceenderman",
            () -> ICEENDERMAN_TYPE);
    public static final RegistryObject<Item> ICEENDERMAN_EGG = ITEMS.register("iceenderman_egg"
            , () -> new SpawnEggItem(ICEENDERMAN_TYPE, 0xC5F9F9, 0x30ABAB,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<GardenerEndermanEntity> GARDENERENDERMAN_TYPE = EntityType.Builder.of(GardenerEndermanEntity::new,
                    EntityClassification.CREATURE).sized(0.7F, 2.9F).build("gardenerenderman");
    public static final RegistryObject<EntityType<GardenerEndermanEntity>> GARDENERENDERMAN = ENTITIES.register("gardenerenderman",
            () -> GARDENERENDERMAN_TYPE);
    public static final RegistryObject<Item> GARDENERENDERMAN_EGG = ITEMS.register("gardenerenderman_egg"
            , () -> new SpawnEggItem(GARDENERENDERMAN_TYPE, 0x1DE11D, 0xF97AD9,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<KnightZombieEntity> KNIGHTZOMBIE_TYPE = EntityType.Builder.of(KnightZombieEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.0F).build("knightzombie");
    public static final RegistryObject<EntityType<KnightZombieEntity>> KNIGHTZOMBIE = ENTITIES.register("knightzombie",
            () -> KNIGHTZOMBIE_TYPE);
    public static final RegistryObject<Item> KNIGHTZOMBIE_EGG = ITEMS.register("knightzombie_egg"
            , () -> new SpawnEggItem(KNIGHTZOMBIE_TYPE, 0xEEEEEE, 0xD0E0E3,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<WorkerZombieEntity> WORKERZOMBIE_TYPE = EntityType.Builder.of(WorkerZombieEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.0F).build("workerzombie");
    public static final RegistryObject<EntityType<WorkerZombieEntity>> WORKERZOMBIE = ENTITIES.register("workerzombie",
            () -> WORKERZOMBIE_TYPE);
    public static final RegistryObject<Item> WORKERZOMBIE_EGG = ITEMS.register("workerzombie_egg"
            , () -> new SpawnEggItem(WORKERZOMBIE_TYPE, 0xFFE599, 0xFFFF00,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<MagmaZombieEntity> MAGMAZOMBIE_TYPE = EntityType.Builder.of(MagmaZombieEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.0F).build("magmazombie");
    public static final RegistryObject<EntityType<MagmaZombieEntity>> MAGMAZOMBIE = ENTITIES.register("magmazombie",
            () -> MAGMAZOMBIE_TYPE);
    public static final RegistryObject<Item> MAGMAZOMBIE_EGG = ITEMS.register("magmazombie_egg"
            , () -> new SpawnEggItem(MAGMAZOMBIE_TYPE, 0xFFF144, 0xCC0000,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<DoctorZombieEntity> DOCTORZOMBIE_TYPE = EntityType.Builder.of(DoctorZombieEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.0F).build("doctorzombie");
    public static final RegistryObject<EntityType<DoctorZombieEntity>> DOCTORZOMBIE = ENTITIES.register("doctorzombie",
            () -> DOCTORZOMBIE_TYPE);
    public static final RegistryObject<Item> DOCTORZOMBIE_EGG = ITEMS.register("doctorzombie_egg"
            , () -> new SpawnEggItem(DOCTORZOMBIE_TYPE, 0xFFFFFF, 0xFD1D1D,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<HungryZombieEntity> HUNGRYZOMBIE_TYPE = EntityType.Builder.of(HungryZombieEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.0F).build("hungryzombie");
    public static final RegistryObject<EntityType<HungryZombieEntity>> HUNGRYZOMBIE = ENTITIES.register("hungryzombie",
            () -> HUNGRYZOMBIE_TYPE);
    public static final RegistryObject<Item> HUNGRYZOMBIE_EGG = ITEMS.register("hungryzombie_egg"
            , () -> new SpawnEggItem(HUNGRYZOMBIE_TYPE, 0x6AA84F, 0xD41A1A,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<GoroZombieEntity> GOROZOMBIE_TYPE = EntityType.Builder.of(GoroZombieEntity::new,
                    EntityClassification.MONSTER).sized(0.7F, 2.0F).build("gorozombie");
    public static final RegistryObject<EntityType<GoroZombieEntity>> GOROZOMBIE = ENTITIES.register("gorozombie",
            () -> GOROZOMBIE_TYPE);
    public static final RegistryObject<Item> GOROZOMBIE_EGG = ITEMS.register("gorozombie_egg"
            , () -> new SpawnEggItem(GOROZOMBIE_TYPE, 0x95BD84, 0xCEA937,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    private static final EntityType<GenieZombieEntity> GENIEZOMBIE_TYPE = EntityType.Builder.of(GenieZombieEntity::new,
            EntityClassification.MONSTER).sized(0.7F, 2.0F).build("geniezombie");
    public static final RegistryObject<EntityType<GenieZombieEntity>> GENIEZOMBIE = ENTITIES.register("geniezombie",
            () -> GENIEZOMBIE_TYPE);
    public static final RegistryObject<Item> GENIEZOMBIE_EGG = ITEMS.register("geniezombie_egg"
            , () -> new SpawnEggItem(GENIEZOMBIE_TYPE, 0xB18D47, 0x463AA5,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<Effect> JOKERNESS_EFFECT = EFFECTS.register("jokernesseffect", JokernessEffect::new);

    public static final RegistryObject<SoundEvent> HEALING_SOUND = SOUNDS.register("healing", () ->
            new SoundEvent(new ResourceLocation(MODID, "healing")));
    public static final RegistryObject<SoundEvent> HOLY_SOUND = SOUNDS.register("holy", () ->
            new SoundEvent(new ResourceLocation(MODID, "holy")));


}
