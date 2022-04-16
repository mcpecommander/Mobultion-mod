package dev.mcpecommander.mobultion.datagen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

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
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables =
            ImmutableList.of(Pair.of(LootTablesGen.EntityTables::new, LootContextParamSets.ENTITY),
                             Pair.of(LootTablesGen.BlockTables::new, LootContextParamSets.BLOCK));

    /**
     * Takes that complicated monstrosity up here to know what classes to run the loot table gen for.
     * @return That thing up there.
     */
    @Nonnull
    @Override
    public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return tables;
    }

    /**
     * Validates that the loot tables are correct I assume. The validate method only exists for the loot table gen for
     * some reason.
     * @param map The map of loot tables that got generated.
     * @param validationtracker The validation tracker that can validate loot tables.
     */
    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext validationtracker) {
        map.forEach((resourceLocation, lootTable) -> LootTables.validate(validationtracker, resourceLocation, lootTable));
    }

    public static class EntityTables extends EntityLoot{

        /**
         * Add loot tables for living entities here. Trying to add a loot table to an entity that doesn't extend living
         * entity will cause a crash.
         */
        @Override
        protected void addTables() {
            this.add(Registration.VAMPIRESKELETON.get(), LootTable.lootTable().withPool(LootPool.lootPool().name("pool1")
                    .setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(Registration.FANG.get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                            .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
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

    public static class BlockTables extends BlockLoot{

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
