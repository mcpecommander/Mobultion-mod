package dev.mcpecommander.mobultion.datagen;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.datagen */
public class LanguageGenEN extends LanguageProvider {

    public LanguageGenEN(DataGenerator gen) {
        super(gen, MODID, "en_us");
    }

    /**
     * Add the different translations in this method. I am using different classes for different locale while it should be
     * possible to have multiple inner classes multiple locales.
     */
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

        //Mob egg items
        //Spider eggs
        this.addSpawnEgg(Registration.ANGELSPIDER_EGG.get(), "Angel Spider");
        this.addSpawnEgg(Registration.WITCHSPIDER_EGG.get(), "Witch Spider");
        this.addSpawnEgg(Registration.HYPNOSPIDER_EGG.get(), "Hypno Spider");
        this.addSpawnEgg(Registration.MAGMASPIDER_EGG.get(), "Magma Spider");
        this.addSpawnEgg(Registration.MOTHERSPIDER_EGG.get(), "Mother Spider");
        this.addSpawnEgg(Registration.MINISPIDER_EGG.get(), "Mini Spider");
        this.addSpawnEgg(Registration.WITHERSPIDER_EGG.get(), "Wither Spider");
        //Skeleton eggs
        this.addSpawnEgg(Registration.JOKERSKELETON_EGG.get(), "Joker Skeleton");
        this.addSpawnEgg(Registration.CORRUPTEDSKELETON_EGG.get(), "Corrupted Skeleton");
        this.addSpawnEgg(Registration.VAMPIRESKELETON_EGG.get(), "Vampire Skeleton");
        this.addSpawnEgg(Registration.FORESTSKELETON_EGG.get(), "Forest Skeleton");
        this.addSpawnEgg(Registration.SHAMANSKELETON_EGG.get(), "Shaman Skeleton");
        this.addSpawnEgg(Registration.MAGMASKELETON_EGG.get(), "Magma Skeleton");
        //Endermen eggs
        this.addSpawnEgg(Registration.WANDERINGENDERMAN_EGG.get(), "Wandering Enderman");
        this.addSpawnEgg(Registration.MAGMAENDERMAN_EGG.get(), "Magma Enderman");
        this.addSpawnEgg(Registration.GLASSENDERMAN_EGG.get(), "Glass Enderman");
        this.addSpawnEgg(Registration.ICEENDERMAN_EGG.get(), "Ice Enderman");
        this.addSpawnEgg(Registration.GARDENERENDERMAN_EGG.get(), "Gardener Enderman");
        //Zombie eggs
        this.addSpawnEgg(Registration.KNIGHTZOMBIE_EGG.get(), "Zombie Knight");
        this.addSpawnEgg(Registration.WORKERZOMBIE_EGG.get(), "Zombie Worker");
        this.addSpawnEgg(Registration.MAGMAZOMBIE_EGG.get(), "Magma Zombie");
        this.addSpawnEgg(Registration.DOCTORZOMBIE_EGG.get(), "Zombie Doctor");
        this.addSpawnEgg(Registration.HUNGRYZOMBIE_EGG.get(), "Hungry Zombie");
        this.addSpawnEgg(Registration.GOROZOMBIE_EGG.get(), "Zombie Goro");
        this.addSpawnEgg(Registration.GENIEZOMBIE_EGG.get(), "Zombie Genie");

        //Info
        //Spider
        this.add(Registration.ANGELSPIDER_EGG.getId().toString().concat(".info"), "A Healer that heals hurt spiders by firing healing particles.\nWhen killed, it has a really small chance to drop the angelic halo to be used by the player");
        this.add(Registration.WITCHSPIDER_EGG.getId().toString().concat(".info"), "");

        //Mobs
        //Spider
        this.add(Registration.ANGELSPIDER.get(), "Angel Spider");
        this.add(Registration.WITCHSPIDER.get(), "Witch Spider");
        this.add(Registration.HYPNOSPIDER.get(), "Hypno Spider");
        this.add(Registration.MAGMASPIDER.get(), "Magma Spider");
        this.add(Registration.MOTHERSPIDER.get(), "Mother Spider");
        this.add(Registration.MINISPIDER.get(), "Mini Spider");
        this.add(Registration.WITHERSPIDER.get(), "Wither Spider");
        this.add(Registration.WITHERHEADBUG.get(), "Wither Head Bug");
        //Skeleton
        this.add(Registration.JOKERSKELETON.get(), "Joker Skeleton");
        this.add(Registration.CORRUPTEDSKELETON.get(), "Corrupted Skeleton");
        this.add(Registration.VAMPIRESKELETON.get(), "Vampire Skeleton");
        this.add(Registration.FORESTSKELETON.get(), "Forest Skeleton");
        this.add(Registration.SHAMANSKELETON.get(), "Shaman Skeleton");
        this.add(Registration.MAGMASKELETON.get(), "Magma Skeleton");
        //Endermen
        this.add(Registration.WANDERINGENDERMAN.get(), "Wandering Enderman");
        this.add(Registration.MAGMAENDERMAN.get(), "Magma Enderman");
        this.add(Registration.GLASSENDERMAN.get(), "Glass Enderman");
        this.add(Registration.ICEENDERMAN.get(), "Ice Enderman");
        this.add(Registration.GARDENERENDERMAN.get(), "Gardener Enderman");
        //Zombie
        this.add(Registration.KNIGHTZOMBIE.get(), "Zombie Knight");
        this.add(Registration.WORKERZOMBIE.get(), "Zombie Worker");
        this.add(Registration.MAGMAZOMBIE.get(), "Magma Zombie");
        this.add(Registration.DOCTORZOMBIE.get(), "Zombie Doctor");
        this.add(Registration.HUNGRYZOMBIE.get(), "Hungry Zombie");
        this.add(Registration.GOROZOMBIE.get(), "Zombie Goro");
        this.add(Registration.GENIEZOMBIE.get(), "Zombie Genie");
        //Projectiles
        this.add(Registration.HYPNOWAVE.get(), "Hypnosis Wave");
        this.add(Registration.HEARTARROW.get(), "Heart Arrow");
        this.add(Registration.CROSSARROW.get(), "Cross Arrow");
        this.add(Registration.GLASSSHOT.get(), "Glass Shot");

        //Sounds
        this.add("healthpack.heal", "Healing with Health pack");
        this.add("halo.revive", "Halo reviving");
        this.add("sword.ignite", "Fire sword igniting");
        this.add("joker.ambient", "Joker skeleton laughing");
        this.add("bells.ringing", "Hat bells ringing");
        this.add("harp.shooting", "Joker Skeleton shooting");
        this.add("cross.splitting", "Cross arrow splitting");
        this.add("cross.shooting", "Forest bow shooting");

        //Mob effects
        this.add(Registration.JOKERNESS_EFFECT.get(), "Jokerness");
        this.add(Registration.HYPNO_EFFECT.get(), "Hypnosis");
        this.add(Registration.CORRUPTION_EFFECT.get(), "Corruption");

        //Item group
        this.add("itemGroup.mobultion", "Mobultion Tab");

    }

    /**
     * Helper method to add spawn egg localisations a bit faster.
     * @param spawnEgg The spawn egg item.
     * @param name The translation in american english locale
     */
    private void addSpawnEgg(Item spawnEgg, String name){
        this.add(spawnEgg, name.concat(" Spawn Egg"));
    }
}
