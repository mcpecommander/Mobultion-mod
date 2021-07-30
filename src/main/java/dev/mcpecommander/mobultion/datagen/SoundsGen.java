package dev.mcpecommander.mobultion.datagen;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 30/07/2021 inside the package - dev.mcpecommander.mobultion.datagen */
public class SoundsGen extends SoundDefinitionsProvider {

    /**
     * Creates a new instance of this data provider.
     *
     * @param generator The data generator instance provided by the event you are initializing this provider in.
     * @param helper    The existing file helper provided by the event you are initializing this provider in.
     */
    public SoundsGen(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, MODID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(Registration.HEALING_SOUND.get(), SoundDefinition.definition().subtitle("healthpack.heal")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "healing")).volume(0.5d))
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID,"healing1")).volume(0.5)));
    }
}
