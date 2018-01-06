package mcpecommander.luggagemod.init;

import mcpecommander.luggagemod.Reference;
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
}
