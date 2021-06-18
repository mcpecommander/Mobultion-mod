package dev.mcpecommander.mobultion.setup;

import dev.mcpecommander.mobultion.Mobultion;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/* Created by McpeCommander on 2021/06/18 */
@Mod.EventBusSubscriber(modid = Mobultion.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup ITEM_GROUP = new ItemGroup("mytutorial") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.TESTBLOCK.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {

    }

}
