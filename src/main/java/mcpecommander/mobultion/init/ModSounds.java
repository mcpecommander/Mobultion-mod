package mcpecommander.mobultion.init;

import mcpecommander.mobultion.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModSounds {
	private static ResourceLocation loc = new ResourceLocation(Reference.MOD_ID, "fire_arrow_shot");
	public static SoundEvent fire_arrow_shot = new SoundEvent(loc).setRegistryName(loc);
	private static ResourceLocation loc2 = new ResourceLocation(Reference.MOD_ID, "entity_respawn");
	public static SoundEvent entity_respawn = new SoundEvent(loc2).setRegistryName(loc2);
	private static ResourceLocation loc3 = new ResourceLocation(Reference.MOD_ID, "magma_remains_death");
	public static SoundEvent magma_remains_death = new SoundEvent(loc3).setRegistryName(loc3);
	private static ResourceLocation loc4 = new ResourceLocation(Reference.MOD_ID, "forest_skeleton_shoot");
	public static SoundEvent forest_skeleton_shoot = new SoundEvent(loc4).setRegistryName(loc4);
	private static ResourceLocation loc5 = new ResourceLocation(Reference.MOD_ID, "joker_ambient");
	public static SoundEvent joker_ambient = new SoundEvent(loc5).setRegistryName(loc5);
	private static ResourceLocation loc6 = new ResourceLocation(Reference.MOD_ID, "ravenous_eating");
	public static SoundEvent ravenous_eating = new SoundEvent(loc6).setRegistryName(loc6);
	private static ResourceLocation loc7 = new ResourceLocation(Reference.MOD_ID, "burp");
	public static SoundEvent burp = new SoundEvent(loc7).setRegistryName(loc7);
	private static ResourceLocation loc8 = new ResourceLocation(Reference.MOD_ID, "shield_block");
	public static SoundEvent shield_block = new SoundEvent(loc8).setRegistryName(loc8);
	private static ResourceLocation loc9 = new ResourceLocation(Reference.MOD_ID, "vampire_bite");
	public static SoundEvent vampire_bite = new SoundEvent(loc9).setRegistryName(loc9);
	private static ResourceLocation loc10 = new ResourceLocation(Reference.MOD_ID, "bat_morph");
	public static SoundEvent bat_morph = new SoundEvent(loc10).setRegistryName(loc10);
	private static ResourceLocation loc11 = new ResourceLocation(Reference.MOD_ID, "puke");
	public static SoundEvent puke = new SoundEvent(loc11).setRegistryName(loc11);
	private static ResourceLocation loc12 = new ResourceLocation(Reference.MOD_ID, "spit");
	public static SoundEvent spit = new SoundEvent(loc12).setRegistryName(loc12);
}
