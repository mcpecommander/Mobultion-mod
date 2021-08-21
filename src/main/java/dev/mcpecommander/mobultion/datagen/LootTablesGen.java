package dev.mcpecommander.mobultion.datagen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/* McpeCommander created on 26/07/2021 inside the package - dev.mcpecommander.mobultion.datagen */
public class LootTablesGen extends LootTableProvider {

    public LootTablesGen(DataGenerator generator) {
        super(generator);
    }

    /**
     * The most complicated list of a pair of a loot parameter set and supplier of consumer of biconsumer of resource
     * location and loot table builder.
     * Just copy it and change according to the names of your inner loot table classes.
     */
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables =
            ImmutableList.of(Pair.of(LootTablesGen.EntityTables::new, LootParameterSets.ENTITY),
                             Pair.of(LootTablesGen.BlockTables::new, LootParameterSets.BLOCK));

    /**
     * Takes that complicated monstrosity up here to know what classes to run the loot table gen for.
     * @return That thing up there.
     */
    @Nonnull
    @Override
    public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return tables;
    }

    /**
     * Validates that the loot tables are correct I assume. The validate method only exists for the loot table gen for
     * some reason.
     * @param map The map of loot tables that got generated.
     * @param validationtracker The validation tracker that can validate loot tables.
     */
    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker) {
        map.forEach((resourceLocation, lootTable) -> LootTableManager.validate(validationtracker, resourceLocation, lootTable));
    }

    public static class EntityTables extends EntityLootTables{

        /**
         * Add loot tables for living entities here. Trying to add a loot table to an entity that doesn't extend living
         * entity will cause a crash.
         */
        @Override
        protected void addTables() {
            this.add(Registration.VAMPIRESKELETON.get(), LootTable.lootTable().withPool(LootPool.lootPool().name("pool1")
                    .setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(Registration.FANG.get())
                            .apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F)))
                            .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
        }

        /**
         * The list of entities that this data generator is trying to generate loot tables for. If an entity is missing or
         * doesn't have a table in the addTables, it will crash the data gen.
         * @return An iterable of the all entities that should have loot tables generated for.
         */
        @Nonnull
        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return Lists.newArrayList(Registration.VAMPIRESKELETON.get());
            //TODO finish the rest of the entities loot tables.

//                    ForgeRegistries.ENTITIES.getValues().stream().filter(entityType ->
//                            entityType.getRegistryName().getNamespace().equals(MODID) &&
//                                    entityType.getCategory() != EntityClassification.MISC)
//                            .collect(Collectors.toList());
        }
    }

    public static class BlockTables extends BlockLootTables{

        /**
         * Add loot tables for blocks here.
         */
        @Override
        protected void addTables() {
            dropSelf(Registration.HAYHAT.get());
        }

        /**
         * The list of blocks that this data generator is trying to generate loot tables for. If a block is missing or
         * doesn't have a table in the addTables, it will crash the data gen.
         * @return An iterable of the all blocks that should have loot tables generated for.
         */
        @Nonnull
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Lists.newArrayList(Registration.HAYHAT.get());
        }
    }
}
