package dev.mcpecommander.mobultion.setup;

import dev.mcpecommander.mobultion.blocks.TestBlock;
import dev.mcpecommander.mobultion.entities.spiders.entities.*;
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



}
