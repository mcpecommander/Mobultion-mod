package dev.mcpecommander.mobultion.datagen;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.datagen */
public class LanguageGenEN extends LanguageProvider {

    public LanguageGenEN(DataGenerator gen) {
        super(gen, MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //Blocks
        this.add(Registration.HAYHAT.get(), "Hay Hat");
        this.add(Registration.SPIDEREGG.get(), "Spider Egg");

        //Items
        this.add(Registration.CORRUPTEDBONE.get(), "Corrupted Bone");
        this.add(Registration.CORRUPTEDBONEMEAL.get(), "Corrupted Bonemeal");
        this.add(Registration.THUNDERSTAFF.get(), "Thunder Staff");
        this.add(Registration.FORESTBOW.get(), "Forest Bow");
        this.add(Registration.HEARTARROW_ITEM.get(), "Heart Arrow");
        this.add(Registration.HEALINGSTAFF.get(), "Restoration Staff");
        this.add(Registration.FANG.get(), "Fang");
        this.add(Registration.HARDHAT.get(), "Hard Hat");
        this.add(Registration.HAMMER.get(), "Hammer");
        this.add(Registration.HEALTHPACK.get(), "Health pack");
        this.add(Registration.KNIFE.get(), "Knife");
        this.add(Registration.FORK.get(), "Fork");
        this.add(Registration.LAMP.get(), "Genie Lamp");
        this.add(Registration.ENDERFLAKE.get(), "Ender Flake");
        this.add(Registration.ENDERBLAZE.get(), "Ender Blaze");
        this.add(Registration.GLASSSHOT_ITEM.get(), "Glass Crystal");
        this.add(Registration.HALO.get(), "Halo");
        this.add(Registration.FLAMINGLEG.get(), "Fiery Leg");
        this.add(Registration.FANGNECKLACE.get(), "Fang Necklace");
        this.add(Registration.HYPNOEMITTER.get(), "Hypnosis Emitter");
        this.add(Registration.MAGICGOOP.get(), "Magic Goop");
        this.add(Registration.FIRESWORD.get(), "Fire Sword");

        //Sounds
        this.add("healthpack.heal", "Healing with Health pack");
        this.add("halo.revive", "Halo reviving");
        this.add("sword.ignite", "Fire sword igniting");

        //Mob effects
        this.add(Registration.JOKERNESS_EFFECT.get(), "Jokerness");
        this.add(Registration.HYPNO_EFFECT.get(), "Hypnosis");

        //Item group
        this.add("itemGroup.mobultion", "Mobultion Tab");



    }
}
