package dev.mcpecommander.mobultion.setup;

import dev.mcpecommander.mobultion.Mobultion;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

/* Created by McpeCommander on 2021/06/18 */
@Mod.EventBusSubscriber(modid = Mobultion.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab("mobultion") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(Registration.HAYHAT.get());
        }
    };

    @SubscribeEvent
    public static void init(final FMLCommonSetupEvent event) {

    }

}
