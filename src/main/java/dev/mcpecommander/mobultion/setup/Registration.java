package dev.mcpecommander.mobultion.setup;

import dev.mcpecommander.mobultion.blocks.TestBlock;
import dev.mcpecommander.mobultion.entities.endermen.entities.GlassEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.entities.GlassShotEntity;
import dev.mcpecommander.mobultion.entities.endermen.entities.MagmaEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.entities.WanderingEndermanEntity;
import dev.mcpecommander.mobultion.entities.skeletons.entities.JokerSkeletonEntity;
import dev.mcpecommander.mobultion.entities.spiders.entities.*;
import dev.mcpecommander.mobultion.items.ThunderStaffItem;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    //private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        //PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    public static final RegistryObject<TestBlock> TESTBLOCK = BLOCKS.register("testblock", TestBlock::new);
    public static final RegistryObject<Item> TESTBLOCK_ITEM = ITEMS.register("testblock", () -> new BlockItem(TESTBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<ThunderStaffItem> THUNDERSTAFF = ITEMS.register("thunderstaffitem", ThunderStaffItem::new);

    private static final EntityType<AngelSpiderEntity> ANGELSPIDER_TYPE = EntityType.Builder.of(AngelSpiderEntity::new, EntityClassification.MONSTER)
            .sized(1.4f, 1f).build("angelspider");
    public static final RegistryObject<EntityType<AngelSpiderEntity>> ANGELSPIDER = ENTITIES.register("angelspider", () -> ANGELSPIDER_TYPE);
    public static final RegistryObject<Item> ANGELSPIDER_EGG = ITEMS.register("angelspider_egg"
            , () -> new SpawnEggItem(ANGELSPIDER_TYPE, 0xFFFFFF, 0xFFFF53,
                    (new Item.Properties()).tab(ItemGroup.TAB_MISC)));

    private static final EntityType<WitchSpiderEntity> WITCHSPIDER_TYPE = EntityType.Builder.of(WitchSpiderEntity::new, EntityClassification.MONSTER)
            .sized(1.4f, 1f).build("witchspider");
    public static final RegistryObject<EntityType<WitchSpiderEntity>> WITCHSPIDER = ENTITIES.register("witchspider", () -> WITCHSPIDER_TYPE);
    public static final RegistryObject<Item> WITCHSPIDER_EGG = ITEMS.register("witchspider_egg"
            , () -> new SpawnEggItem(WITCHSPIDER_TYPE, 0x6AA84F, 0x12436F,
                    (new Item.Properties()).tab(ItemGroup.TAB_MISC)));

    private static final EntityType<HypnoSpiderEntity> HYPNOSPIDER_TYPE = EntityType.Builder.of(HypnoSpiderEntity::new, EntityClassification.MONSTER)
            .sized(1.4f, 1f).build("hypnospider");
    public static final RegistryObject<EntityType<HypnoSpiderEntity>> HYPNOSPIDER = ENTITIES.register("hypnospider", () -> HYPNOSPIDER_TYPE);
    public static final RegistryObject<Item> HYPNOSPIDER_EGG = ITEMS.register("hypnospider_egg"
            , () -> new SpawnEggItem(HYPNOSPIDER_TYPE, 0xDD06DD, 0xF736F7,
                    (new Item.Properties()).tab(ItemGroup.TAB_MISC)));

    private static final EntityType<MagmaSpiderEntity> MAGMASPIDER_TYPE = EntityType.Builder.of(MagmaSpiderEntity::new, EntityClassification.MONSTER)
            .sized(1.4f, 1f).build("magmaspider");
    public static final RegistryObject<EntityType<MagmaSpiderEntity>> MAGMASPIDER = ENTITIES.register("magmsspider", () -> MAGMASPIDER_TYPE);
    public static final RegistryObject<Item> MAGMASPIDER_EGG = ITEMS.register("magmaspider_egg"
            , () -> new SpawnEggItem(MAGMASPIDER_TYPE, 0x230E0E, 0xF01414,
                    (new Item.Properties()).tab(ItemGroup.TAB_MISC)));

    private static final EntityType<WitherSpiderEntity> WITHERSPIDER_TYPE = EntityType.Builder.of(WitherSpiderEntity::new, EntityClassification.MONSTER)
            .sized(1.4f, 1f).build("witherspider");
    public static final RegistryObject<EntityType<WitherSpiderEntity>> WITHERSPIDER = ENTITIES.register("witherspider", () -> WITHERSPIDER_TYPE);
    public static final RegistryObject<Item> WITHERSPIDER_EGG = ITEMS.register("witherspider_egg"
            , () -> new SpawnEggItem(WITHERSPIDER_TYPE, 0x666666, 0x444444,
                    (new Item.Properties()).tab(ItemGroup.TAB_MISC)));

    private static final EntityType<WitherHeadBugEntity> WITHERHEADBUG_TYPE = EntityType.Builder.of(WitherHeadBugEntity::new, EntityClassification.MONSTER)
            .sized(0.7f, 0.7f).build("witherheadbug");
    public static final RegistryObject<EntityType<WitherHeadBugEntity>> WITHERHEADBUG = ENTITIES.register("witherheadbug", () -> WITHERHEADBUG_TYPE);

    private static final EntityType<JokerSkeletonEntity> JOKERSKELETON_TYPE = EntityType.Builder.of(JokerSkeletonEntity::new, EntityClassification.MONSTER)
            .sized(0.6F, 1.99F).build("jokerskeleton");
    public static final RegistryObject<EntityType<JokerSkeletonEntity>> JOKERSKELETON = ENTITIES.register("jokerskeleton", () -> JOKERSKELETON_TYPE);
    public static final RegistryObject<Item> JOKERSKELETON_EGG = ITEMS.register("jokerskeleton_egg"
            , () -> new SpawnEggItem(JOKERSKELETON_TYPE, 0xFF0000, 0xFFFF00,
                    (new Item.Properties()).tab(ItemGroup.TAB_MISC)));

    private static final EntityType<WanderingEndermanEntity> WANDERINGENDERMAN_TYPE = EntityType.Builder.of(WanderingEndermanEntity::new, EntityClassification.MONSTER)
            .sized(0.7F, 2.9F).build("wanderingenderman");
    public static final RegistryObject<EntityType<WanderingEndermanEntity>> WANDERINGENDERMAN = ENTITIES.register("wanderingenderman", () -> WANDERINGENDERMAN_TYPE);
    public static final RegistryObject<Item> WANDERINGENDERMAN_EGG = ITEMS.register("wanderingenderman_egg"
            , () -> new SpawnEggItem(WANDERINGENDERMAN_TYPE, 0x422C01, 0x036303,
                    (new Item.Properties()).tab(ItemGroup.TAB_MISC)));

    private static final EntityType<MagmaEndermanEntity> MAGMAENDERMAN_TYPE = EntityType.Builder.of(MagmaEndermanEntity::new, EntityClassification.MONSTER)
            .sized(0.7F, 2.9F).build("magmaenderman");
    public static final RegistryObject<EntityType<MagmaEndermanEntity>> MAGMAENDERMAN = ENTITIES.register("magmaenderman", () -> MAGMAENDERMAN_TYPE);
    public static final RegistryObject<Item> MAGMAENDERMAN_EGG = ITEMS.register("magmaenderman_egg"
            , () -> new SpawnEggItem(MAGMAENDERMAN_TYPE, 0xB40A0A, 0xE7E740,
                    (new Item.Properties()).tab(ItemGroup.TAB_MISC)));

    private static final EntityType<GlassEndermanEntity> GLASSENDERMAN_TYPE = EntityType.Builder.of(GlassEndermanEntity::new, EntityClassification.MONSTER)
            .sized(0.7F, 2.9F).build("glassenderman");
    public static final RegistryObject<EntityType<GlassEndermanEntity>> GLASSENDERMAN = ENTITIES.register("glassenderman", () -> GLASSENDERMAN_TYPE);
    public static final RegistryObject<Item> GLASSENDERMAN_EGG = ITEMS.register("glassenderman_egg"
            , () -> new SpawnEggItem(GLASSENDERMAN_TYPE, 0x2D2C2F, 0x535056,
                    (new Item.Properties()).tab(ItemGroup.TAB_MISC)));

    private static final EntityType<GlassShotEntity> GLASSESHOT_TYPE = EntityType.Builder.of((EntityType.IFactory<GlassShotEntity>) GlassShotEntity::new, EntityClassification.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(8).build("glassshot");
    public static final RegistryObject<EntityType<GlassShotEntity>> GLASSSHOT = ENTITIES.register("glassshot", () -> GLASSESHOT_TYPE);


}
