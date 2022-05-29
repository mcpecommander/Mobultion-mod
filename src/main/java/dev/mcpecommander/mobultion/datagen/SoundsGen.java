package dev.mcpecommander.mobultion.datagen;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 30/07/2021 inside the package - dev.mcpecommander.mobultion.datagen */
public class SoundsGen extends SoundDefinitionsProvider {

    /**
     * Creates a new instance of this data provider.
     * @param generator The data generator instance provided by the event you are initializing this provider in.
     * @param helper    The existing file helper provided by the event you are initializing this provider in.
     */
    public SoundsGen(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, MODID, helper);
    }

    /**
     * Add your sound events here to generate the sounds.json file for them.
     */
    @Override
    public void registerSounds() {
        this.add(Registration.HEALING_SOUND.get(), SoundDefinition.definition().subtitle("healthpack.heal")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "healing")).volume(0.5d))
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID,"healing1")).volume(0.5)));
        this.add(Registration.HOLY_SOUND.get(), SoundDefinition.definition().subtitle("halo.revive")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "holy")).volume(0.5d)));
        this.add(Registration.IGNITE_SOUND.get(), SoundDefinition.definition().subtitle("sword.ignite")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "ignite")).volume(1d)));
        this.add(Registration.JOKER_SOUND.get(), SoundDefinition.definition().subtitle("joker.ambient")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "joker")).volume(1d)));
        this.add(Registration.BELLS_SOUND.get(), SoundDefinition.definition().subtitle("bells.ringing")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "bells")).volume(1d)));
        this.add(Registration.HARP_SOUND.get(), SoundDefinition.definition().subtitle("harp.shooting")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "harp")).volume(0.5d))
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "harp1")).volume(0.5)));
        this.add(Registration.SPLIT_SOUND.get(), SoundDefinition.definition().subtitle("cross.splitting")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "split")).volume(1d)));
        this.add(Registration.SLASH_SOUND.get(), SoundDefinition.definition().subtitle("cross.shooting")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "slash")).volume(1d)));
        this.add(Registration.ZAP_SOUND.get(), SoundDefinition.definition().subtitle("lightning.zap")
                .with(SoundDefinitionsProvider.sound(new ResourceLocation(MODID, "zap")).volume(1d)));
    }
}
