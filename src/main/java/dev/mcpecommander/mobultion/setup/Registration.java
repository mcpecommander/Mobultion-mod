package dev.mcpecommander.mobultion.setup;

import dev.mcpecommander.mobultion.blocks.HayHatBlock;
import dev.mcpecommander.mobultion.blocks.SpiderEggBlock;
import dev.mcpecommander.mobultion.blocks.tile.SpiderEggTile;
import dev.mcpecommander.mobultion.effects.CorruptionEffect;
import dev.mcpecommander.mobultion.effects.HypnoEffect;
import dev.mcpecommander.mobultion.effects.JokernessEffect;
import dev.mcpecommander.mobultion.entities.endermen.entities.*;
import dev.mcpecommander.mobultion.entities.skeletons.entities.*;
import dev.mcpecommander.mobultion.entities.spiders.entities.*;
import dev.mcpecommander.mobultion.entities.zombies.entities.*;
import dev.mcpecommander.mobultion.items.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    private static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZER = DeferredRegister.
            create(ForgeRegistries.Keys.DATA_SERIALIZERS, MODID);
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID);

    public static void init() {

        ClientSetup.PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        DATA_SERIALIZER.register(FMLJavaModLoadingContext.get().getModEventBus());
        ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {

        event.put(ANGELSPIDER.get(), AngelSpiderEntity.createAttributes().build());
        event.put(WITCHSPIDER.get(), WitchSpiderEntity.createAttributes().build());
        event.put(HYPNOSPIDER.get(), HypnoSpiderEntity.createAttributes().build());
        event.put(MAGMASPIDER.get(), MagmaSpiderEntity.createAttributes().build());
        event.put(MOTHERSPIDER.get(), MotherSpiderEntity.createAttributes().build());
        event.put(MINISPIDER.get(), MiniSpiderEntity.createAttributes().build());
        event.put(WITHERSPIDER.get(), WitherSpiderEntity.createAttributes().build());
        event.put(WITHERHEADBUG.get(), WitherHeadBugEntity.createAttributes().build());
        event.put(REDEYE.get(), RedEyeEntity.createAttributes().build());

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
        event.put(GARDENERENDERMAN.get(), GardenerEndermanEntity.createAttributes().build());
        SpawnPlacements.register(GARDENERENDERMAN.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
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
    public static final EntityDataSerializer<List<BlockPos>> BLOCKPOS_LIST = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buffer, List<BlockPos> list) {
            buffer.writeInt(list.size());
            for (BlockPos pos : list) {
                buffer.writeBlockPos(pos);
            }
        }

        @Nonnull
        @Override
        public List<BlockPos> read(FriendlyByteBuf buffer) {
            int size = buffer.readInt();
            ArrayList<BlockPos> positions = new ArrayList<>();
            for (int i = 0; i < size; i++) {
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
    public static final RegistryObject<Item> HAYHAT_ITEM = ITEMS.register("hayhatblock", () -> new HayHatBlockItem(HAYHAT.get()));
    public static final RegistryObject<SpiderEggBlock> SPIDEREGG = BLOCKS.register("spidereggblock", SpiderEggBlock::new);
    public static final RegistryObject<Item> SPIDEREGG_ITEM = ITEMS.register("spidereggblock", () -> new BlockItem(SPIDEREGG.get(),
            new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<SpiderEggTile>> SPIDEREGG_TILE = TILE_ENTITIES.register("spidereggtile",
            () -> BlockEntityType.Builder.of(SpiderEggTile::new, SPIDEREGG.get()).build(null));

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
    public static final RegistryObject<FangNecklaceItem> FANGNECKLACE = ITEMS.register("fangnecklaceitem", FangNecklaceItem::new);
    public static final RegistryObject<HypnoEmitterItem> HYPNOEMITTER = ITEMS.register("hypnoemitteritem", HypnoEmitterItem::new);
    public static final RegistryObject<Item> MAGICGOOP = ITEMS.register("magicgoopitem", () -> new Item(new Item.Properties()
            .tab(ModSetup.ITEM_GROUP).stacksTo(16)));
    public static final RegistryObject<FireSwordItem> FIRESWORD = ITEMS.register("firesworditem", FireSwordItem::new);
    public static final RegistryObject<JokerHatItem> JOKERHAT = ITEMS.register("jokerhatitem", JokerHatItem::new);

    public static final RegistryObject<EntityType<AngelSpiderEntity>> ANGELSPIDER = ENTITIES.register("angelspider",
            () -> EntityType.Builder.of(AngelSpiderEntity::new,
            MobCategory.MONSTER).sized(1.4f, 1f).build("angelspider"));
    public static final RegistryObject<Item> ANGELSPIDER_EGG = ITEMS.register("angelspider_egg"
            , () -> new ForgeSpawnEggItem(ANGELSPIDER, 0xFFFFFF, 0xFFFF53,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<WitchSpiderEntity>> WITCHSPIDER = ENTITIES.register("witchspider",
            () -> EntityType.Builder.of(WitchSpiderEntity::new,
            MobCategory.MONSTER).sized(1.4f, 1f).build("witchspider"));
    public static final RegistryObject<Item> WITCHSPIDER_EGG = ITEMS.register("witchspider_egg"
            , () -> new ForgeSpawnEggItem(WITCHSPIDER, 0x6AA84F, 0x12436F,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<HypnoSpiderEntity>> HYPNOSPIDER = ENTITIES.register("hypnospider",
            () -> EntityType.Builder.of(HypnoSpiderEntity::new,
                    MobCategory.MONSTER).sized(1.4f, 1f).build("hypnospider"));
    public static final RegistryObject<Item> HYPNOSPIDER_EGG = ITEMS.register("hypnospider_egg"
            , () -> new ForgeSpawnEggItem(HYPNOSPIDER, 0xDD06DD, 0xF736F7,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<EntityType<HypnoWaveEntity>> HYPNOWAVE = ENTITIES.register("hypnowave",
            () -> EntityType.Builder.of((EntityType.EntityFactory<HypnoWaveEntity>) HypnoWaveEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).build("hypnowave"));

    public static final RegistryObject<EntityType<MagmaSpiderEntity>> MAGMASPIDER = ENTITIES.register("magmsspider",
            () -> EntityType.Builder.of(MagmaSpiderEntity::new,
                    MobCategory.MONSTER).sized(1.4f, 1f).fireImmune().build("magmaspider"));
    public static final RegistryObject<Item> MAGMASPIDER_EGG = ITEMS.register("magmaspider_egg"
            , () -> new ForgeSpawnEggItem(MAGMASPIDER, 0x230E0E, 0xF01414,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<MotherSpiderEntity>> MOTHERSPIDER = ENTITIES.register("motherspider",
            () -> EntityType.Builder.of(MotherSpiderEntity::new,
                    MobCategory.MONSTER).sized(1.4f, 1f).build("motherspider"));
    public static final RegistryObject<Item> MOTHERSPIDER_EGG = ITEMS.register("motherspider_egg"
            , () -> new ForgeSpawnEggItem(MOTHERSPIDER, 0x444444, 0x9D8888,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<MiniSpiderEntity>> MINISPIDER = ENTITIES.register("minispider",
            () -> EntityType.Builder.of(MiniSpiderEntity::new,
                    MobCategory.MONSTER).sized(0.8f, 0.6f).build("minispider"));
    public static final RegistryObject<Item> MINISPIDER_EGG = ITEMS.register("minispider_egg"
            , () -> new ForgeSpawnEggItem(MINISPIDER, 0xFFFFFF, 0xAAAAAA,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<WitherSpiderEntity>> WITHERSPIDER = ENTITIES.register("witherspider",
            () -> EntityType.Builder.of(WitherSpiderEntity::new,
                    MobCategory.MONSTER).sized(1.4f, 1f).build("witherspider"));
    public static final RegistryObject<Item> WITHERSPIDER_EGG = ITEMS.register("witherspider_egg"
            , () -> new ForgeSpawnEggItem(WITHERSPIDER, 0x666666, 0x444444,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<EntityType<WitherHeadBugEntity>> WITHERHEADBUG = ENTITIES.register("witherheadbug",
            () -> EntityType.Builder.of(WitherHeadBugEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f).build("witherheadbug"));
    public static final RegistryObject<EntityType<RedEyeEntity>> REDEYE = ENTITIES.register("redeye",
            () -> EntityType.Builder.of(RedEyeEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f).build("redeye"));
    public static final RegistryObject<EntityType<WitheringWebEntity>> WITHERINGWEB = ENTITIES.register("witheringweb",
            () -> EntityType.Builder.of((EntityType.EntityFactory<WitheringWebEntity>) WitheringWebEntity::new, MobCategory.MISC)
                    .sized(0.1f, 0.1f).build("witheringweb"));

    public static final RegistryObject<EntityType<JokerSkeletonEntity>> JOKERSKELETON = ENTITIES.register("jokerskeleton",
            () -> EntityType.Builder.of(JokerSkeletonEntity::new,
                    MobCategory.MONSTER).sized(0.6F, 1.99F).build("jokerskeleton"));
    public static final RegistryObject<Item> JOKERSKELETON_EGG = ITEMS.register("jokerskeleton_egg"
            , () -> new ForgeSpawnEggItem(JOKERSKELETON, 0xFF0000, 0xFFFF00,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<EntityType<HeartArrowEntity>> HEARTARROW = ENTITIES.register("heartarrow",
            () -> EntityType.Builder.of(HeartArrowEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).build("heartarrow"));

    public static final RegistryObject<EntityType<CorruptedSkeletonEntity>> CORRUPTEDSKELETON = ENTITIES.register("corruptedskeleton",
            () -> EntityType.Builder.of(CorruptedSkeletonEntity::new,
                    MobCategory.MONSTER).sized(0.6F, 1.99F).build("corruptedskeleton"));
    public static final RegistryObject<Item> CORRUPTEDSKELETON_EGG = ITEMS.register("corruptedskeleton_egg"
            , () -> new ForgeSpawnEggItem(CORRUPTEDSKELETON, 0x745F1D, 0x927006,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<VampireSkeletonEntity>> VAMPIRESKELETON = ENTITIES.register("vampireskeleton",
            () -> EntityType.Builder.of(VampireSkeletonEntity::new,
                    MobCategory.MONSTER).sized(0.6F, 1.99F).build("vampireskeleton"));
    public static final RegistryObject<Item> VAMPIRESKELETON_EGG = ITEMS.register("vampireskeleton_egg"
            , () -> new ForgeSpawnEggItem(VAMPIRESKELETON, 0xBB8A8A, 0x540D0D,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<ForestSkeletonEntity>> FORESTSKELETON = ENTITIES.register("forestskeleton",
            () -> EntityType.Builder.of(ForestSkeletonEntity::new,
                    MobCategory.MONSTER).sized(0.6F, 1.99F).build("forestskeleton"));
    public static final RegistryObject<Item> FORESTSKELETON_EGG = ITEMS.register("forestskeleton_egg"
            , () -> new ForgeSpawnEggItem(FORESTSKELETON, 0x38761D, 0x93C47D,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<EntityType<CrossArrowEntity>> CROSSARROW = ENTITIES.register("crossarrow",
            () -> EntityType.Builder.of(CrossArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).build("crossarrow"));

    public static final RegistryObject<EntityType<ShamanSkeletonEntity>> SHAMANSKELETON = ENTITIES.register("shamanskeleton",
            () -> EntityType.Builder.of(ShamanSkeletonEntity::new,
                    MobCategory.MONSTER).sized(0.6F, 1.99F).build("shamanskeleton"));
    public static final RegistryObject<Item> SHAMANSKELETON_EGG = ITEMS.register("shamanskeleton_egg"
            , () -> new ForgeSpawnEggItem(SHAMANSKELETON, 0x050572, 0x741B47,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<EntityType<MiniLightningEntity>> MINILIGHTNING = ENTITIES.register("minilightning",
            () -> EntityType.Builder.of(MiniLightningEntity::new, MobCategory.MISC)
                    .noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE).build("minilightning"));

    public static final RegistryObject<EntityType<MagmaSkeletonEntity>> MAGMASKELETON = ENTITIES.register("magmaskeleton",
            () -> EntityType.Builder.of(MagmaSkeletonEntity::new,
                    MobCategory.MONSTER).sized(0.6F, 1.99F).fireImmune().build("magmaskeleton"));
    public static final RegistryObject<Item> MAGMASKELETON_EGG = ITEMS.register("magmaskeleton_egg"
            , () -> new ForgeSpawnEggItem(MAGMASKELETON, 0x811616, 0xFD1D1D,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<WanderingEndermanEntity>> WANDERINGENDERMAN = ENTITIES.register("wanderingenderman",
            () -> EntityType.Builder.of(WanderingEndermanEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.9F).build("wanderingenderman"));
    public static final RegistryObject<Item> WANDERINGENDERMAN_EGG = ITEMS.register("wanderingenderman_egg"
            , () -> new ForgeSpawnEggItem(WANDERINGENDERMAN, 0x422C01, 0x036303,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<MagmaEndermanEntity>> MAGMAENDERMAN = ENTITIES.register("magmaenderman",
            () -> EntityType.Builder.of(MagmaEndermanEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.9F).fireImmune().build("magmaenderman"));
    public static final RegistryObject<Item> MAGMAENDERMAN_EGG = ITEMS.register("magmaenderman_egg"
            , () -> new ForgeSpawnEggItem(MAGMAENDERMAN, 0xB40A0A, 0xE7E740,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<GlassEndermanEntity>> GLASSENDERMAN = ENTITIES.register("glassenderman",
            () -> EntityType.Builder.of(GlassEndermanEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.9F).build("glassenderman"));
    public static final RegistryObject<Item> GLASSENDERMAN_EGG = ITEMS.register("glassenderman_egg"
            , () -> new ForgeSpawnEggItem(GLASSENDERMAN, 0x2D2C2F, 0x535056,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<EntityType<GlassShotEntity>> GLASSSHOT = ENTITIES.register("glassshot",
            () -> EntityType.Builder.of((EntityType.EntityFactory<GlassShotEntity>) GlassShotEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(8).setUpdateInterval(1).build("glassshot"));

    public static final RegistryObject<EntityType<IceEndermanEntity>> ICEENDERMAN = ENTITIES.register("iceenderman",
            () -> EntityType.Builder.of(IceEndermanEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.9F).build("iceenderman"));
    public static final RegistryObject<Item> ICEENDERMAN_EGG = ITEMS.register("iceenderman_egg"
            , () -> new ForgeSpawnEggItem(ICEENDERMAN, 0xC5F9F9, 0x30ABAB,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<GardenerEndermanEntity>> GARDENERENDERMAN = ENTITIES.register("gardenerenderman",
            () -> EntityType.Builder.of(GardenerEndermanEntity::new,
                    MobCategory.CREATURE).sized(0.7F, 2.9F).build("gardenerenderman"));
    public static final RegistryObject<Item> GARDENERENDERMAN_EGG = ITEMS.register("gardenerenderman_egg"
            , () -> new ForgeSpawnEggItem(GARDENERENDERMAN, 0x1DE11D, 0xF97AD9,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<KnightZombieEntity>> KNIGHTZOMBIE = ENTITIES.register("knightzombie",
            () -> EntityType.Builder.of(KnightZombieEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.0F).build("knightzombie"));
    public static final RegistryObject<Item> KNIGHTZOMBIE_EGG = ITEMS.register("knightzombie_egg"
            , () -> new ForgeSpawnEggItem(KNIGHTZOMBIE, 0xEEEEEE, 0xD0E0E3,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<WorkerZombieEntity>> WORKERZOMBIE = ENTITIES.register("workerzombie",
            () -> EntityType.Builder.of(WorkerZombieEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.0F).build("workerzombie"));
    public static final RegistryObject<Item> WORKERZOMBIE_EGG = ITEMS.register("workerzombie_egg"
            , () -> new ForgeSpawnEggItem(WORKERZOMBIE, 0xFFE599, 0xFFFF00,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<MagmaZombieEntity>> MAGMAZOMBIE = ENTITIES.register("magmazombie",
            () -> EntityType.Builder.of(MagmaZombieEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.0F).fireImmune().build("magmazombie"));
    public static final RegistryObject<Item> MAGMAZOMBIE_EGG = ITEMS.register("magmazombie_egg"
            , () -> new ForgeSpawnEggItem(MAGMAZOMBIE, 0xFFF144, 0xCC0000,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<DoctorZombieEntity>> DOCTORZOMBIE = ENTITIES.register("doctorzombie",
            () -> EntityType.Builder.of(DoctorZombieEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.0F).build("doctorzombie"));
    public static final RegistryObject<Item> DOCTORZOMBIE_EGG = ITEMS.register("doctorzombie_egg"
            , () -> new ForgeSpawnEggItem(DOCTORZOMBIE, 0xFFFFFF, 0xFD1D1D,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<HungryZombieEntity>> HUNGRYZOMBIE = ENTITIES.register("hungryzombie",
            () -> EntityType.Builder.of(HungryZombieEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.0F).build("hungryzombie"));
    public static final RegistryObject<Item> HUNGRYZOMBIE_EGG = ITEMS.register("hungryzombie_egg"
            , () -> new ForgeSpawnEggItem(HUNGRYZOMBIE, 0x6AA84F, 0xD41A1A,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<GoroZombieEntity>> GOROZOMBIE = ENTITIES.register("gorozombie",
            () -> EntityType.Builder.of(GoroZombieEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.0F).build("gorozombie"));
    public static final RegistryObject<Item> GOROZOMBIE_EGG = ITEMS.register("gorozombie_egg"
            , () -> new ForgeSpawnEggItem(GOROZOMBIE, 0x95BD84, 0xCEA937,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<EntityType<GenieZombieEntity>> GENIEZOMBIE = ENTITIES.register("geniezombie",
            () -> EntityType.Builder.of(GenieZombieEntity::new,
                    MobCategory.MONSTER).sized(0.7F, 2.0F).build("geniezombie"));
    public static final RegistryObject<Item> GENIEZOMBIE_EGG = ITEMS.register("geniezombie_egg"
            , () -> new ForgeSpawnEggItem(GENIEZOMBIE, 0xB18D47, 0x463AA5,
                    (new Item.Properties()).tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<MobEffect> JOKERNESS_EFFECT = EFFECTS.register("jokernesseffect", JokernessEffect::new);
    public static final RegistryObject<MobEffect> HYPNO_EFFECT = EFFECTS.register("hypnoeffect", HypnoEffect::new);
    public static final RegistryObject<MobEffect> CORRUPTION_EFFECT = EFFECTS.register("corruptioneffect", CorruptionEffect::new);

    public static final RegistryObject<SoundEvent> HEALING_SOUND = SOUNDS.register("healing", () ->
            new SoundEvent(new ResourceLocation(MODID, "healing")));
    public static final RegistryObject<SoundEvent> HOLY_SOUND = SOUNDS.register("holy", () ->
            new SoundEvent(new ResourceLocation(MODID, "holy")));
    public static final RegistryObject<SoundEvent> IGNITE_SOUND = SOUNDS.register("ignite", () ->
            new SoundEvent(new ResourceLocation(MODID, "ignite")));
    public static final RegistryObject<SoundEvent> JOKER_SOUND = SOUNDS.register("joker", () ->
            new SoundEvent(new ResourceLocation(MODID, "joker")));
    public static final RegistryObject<SoundEvent> BELLS_SOUND = SOUNDS.register("bells", () ->
            new SoundEvent(new ResourceLocation(MODID, "bells")));
    public static final RegistryObject<SoundEvent> HARP_SOUND = SOUNDS.register("harp", () ->
            new SoundEvent(new ResourceLocation(MODID, "harp")));
    public static final RegistryObject<SoundEvent> SPLIT_SOUND = SOUNDS.register("split", () ->
            new SoundEvent(new ResourceLocation(MODID, "split")));
    public static final RegistryObject<SoundEvent> SLASH_SOUND = SOUNDS.register("slash", () ->
            new SoundEvent(new ResourceLocation(MODID, "slash")));

    public static final RegistryObject<Attribute> RANGED_DAMAGE = ATTRIBUTES.register("ranged_damage", () ->
            new RangedAttribute("mobultion.rangedDamage", 5d, 1d, 1024d));


}
