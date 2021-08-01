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
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 26/07/2021 inside the package - dev.mcpecommander.mobultion.datagen */
public class LootTablesGen extends LootTableProvider {

    public LootTablesGen(DataGenerator generator) {
        super(generator);
    }

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables =
            ImmutableList.of(Pair.of(LootTablesGen.EntityTables::new, LootParameterSets.ENTITY));

    @Nonnull
    @Override
    public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return tables;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker) {
        map.forEach((resourceLocation, lootTable) -> LootTableManager.validate(validationtracker, resourceLocation, lootTable));
    }

    public static class EntityTables extends EntityLootTables{

        @Override
        protected void addTables() {
            this.add(Registration.VAMPIRESKELETON.get(), LootTable.lootTable().withPool(LootPool.lootPool().name("pool1")
                    .setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(Registration.FANG.get())
                            .apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F)))
                            .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
        }

        @Nonnull
        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return Lists.newArrayList(Registration.VAMPIRESKELETON.get());
//                    ForgeRegistries.ENTITIES.getValues().stream().filter(entityType ->
//                            entityType.getRegistryName().getNamespace().equals(MODID) &&
//                                    entityType.getCategory() != EntityClassification.MISC)
//                            .collect(Collectors.toList());
        }
    }

    public static class BlockTables extends BlockLootTables{

        @Override
        protected void addTables() {
            dropSelf(Registration.HAYHAT.get());
        }

        @Nonnull
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getValues().stream().filter(blocks ->
                            blocks.getRegistryName().getNamespace().equals(MODID))
                            .collect(Collectors.toList());
        }
    }
}
