package mcpecommander.mobultion.init;

import mcpecommander.mobultion.items.ItemCorruptedBone;
import mcpecommander.mobultion.items.ItemCorruptedBonemeal;
import mcpecommander.mobultion.items.ItemEnderBlaze;
import mcpecommander.mobultion.items.ItemEnderFlake;
import mcpecommander.mobultion.items.ItemEnderGlassShot;
import mcpecommander.mobultion.items.ItemFang;
import mcpecommander.mobultion.items.ItemFangNecklace;
import mcpecommander.mobultion.items.ItemFireSword;
import mcpecommander.mobultion.items.ItemFlamingChip;
import mcpecommander.mobultion.items.ItemForestBow;
import mcpecommander.mobultion.items.ItemFork;
import mcpecommander.mobultion.items.ItemHammer;
import mcpecommander.mobultion.items.ItemHat;
import mcpecommander.mobultion.items.ItemHayHat;
import mcpecommander.mobultion.items.ItemHealingWand;
import mcpecommander.mobultion.items.ItemHealth;
import mcpecommander.mobultion.items.ItemHeartArrow;
import mcpecommander.mobultion.items.ItemHolyShard;
import mcpecommander.mobultion.items.ItemHypnoBall;
import mcpecommander.mobultion.items.ItemKnife;
import mcpecommander.mobultion.items.ItemMagicTouch;
import mcpecommander.mobultion.items.ItemMagmaArrow;
import mcpecommander.mobultion.items.ItemNetherRuby;
import mcpecommander.mobultion.items.ItemPigmanFlesh;
import mcpecommander.mobultion.items.ItemSorcererBreath;
import mcpecommander.mobultion.items.ItemSpawnChanger;
import mcpecommander.mobultion.items.ItemSpineAsh;
import mcpecommander.mobultion.items.ItemThunderWand;
import mcpecommander.mobultion.items.ItemWitherSpine;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathBoots;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathHelmet;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathLeggings;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathTunic;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	@GameRegistry.ObjectHolder("mobultion:item_forest_bow")
	public static ItemForestBow forestBow;
	@GameRegistry.ObjectHolder("mobultion:item_healing_wand")
	public static ItemHealingWand healingWand;
	@GameRegistry.ObjectHolder("mobultion:item_heart_arrow")
	public static ItemHeartArrow heartArrow;
	@GameRegistry.ObjectHolder("mobultion:item_hammer")
	public static ItemHammer hammer;
	@GameRegistry.ObjectHolder("mobultion:item_fire_sword")
	public static ItemFireSword fireSword;
	@GameRegistry.ObjectHolder("mobultion:item_health")
	public static ItemHealth health;
	@GameRegistry.ObjectHolder("mobultion:item_fork")
	public static ItemFork fork;
	@GameRegistry.ObjectHolder("mobultion:item_knife")
	public static ItemKnife knife;
	@GameRegistry.ObjectHolder("mobultion:item_hat")
	public static ItemHat hat;
	@GameRegistry.ObjectHolder("mobultion:item_ender_flake")
	public static ItemEnderFlake enderFlake;
	@GameRegistry.ObjectHolder("mobultion:item_ender_blaze")
	public static ItemEnderBlaze enderBlaze;
	@GameRegistry.ObjectHolder("mobultion:item_corrupted_bone")
	public static ItemCorruptedBone corruptedBone;
	@GameRegistry.ObjectHolder("mobultion:item_corrupted_bonemeal")
	public static ItemCorruptedBonemeal corruptedBonemeal;
	@GameRegistry.ObjectHolder("mobultion:item_holy_shard")
	public static ItemHolyShard holyShard;
	@GameRegistry.ObjectHolder("mobultion:item_hypno_ball")
	public static ItemHypnoBall hypnoBall;
	@GameRegistry.ObjectHolder("mobultion:item_fang_necklace")
	public static ItemFangNecklace fangNecklace;
	@GameRegistry.ObjectHolder("mobultion:item_fang")
	public static ItemFang fang;
	@GameRegistry.ObjectHolder("mobultion:item_magma_arrow")
	public static ItemMagmaArrow magmaArrow;
	@GameRegistry.ObjectHolder("mobultion:item_magic_touch")
	public static ItemMagicTouch magicTouch;
	@GameRegistry.ObjectHolder("mobultion:item_sorcerer_breath")
	public static ItemSorcererBreath sorcererBreath;
	@GameRegistry.ObjectHolder("mobultion:item_spine_ash")
	public static ItemSpineAsh spineAsh;
	@GameRegistry.ObjectHolder("mobultion:item_wither_spine")
	public static ItemWitherSpine witherSpine;
	@GameRegistry.ObjectHolder("mobultion:item_flaming_chip")
	public static ItemFlamingChip flamingChip;
	@GameRegistry.ObjectHolder("mobultion:item_nether_ruby")
	public static ItemNetherRuby netherRuby;
	@GameRegistry.ObjectHolder("mobultion:item_pigman_flesh")
	public static ItemPigmanFlesh pigmanFlesh;
	@GameRegistry.ObjectHolder("mobultion:item_pigsheath_tunic")
	public static ItemPigsheathTunic pigsheathTunic;
	@GameRegistry.ObjectHolder("mobultion:item_pigsheath_helmet")
	public static ItemPigsheathHelmet pigsheathHelmet;
	@GameRegistry.ObjectHolder("mobultion:item_pigsheath_leggings")
	public static ItemPigsheathLeggings pigsheathLeggings;
	@GameRegistry.ObjectHolder("mobultion:item_pigsheath_boots")
	public static ItemPigsheathBoots pigsheathBoots;
	@GameRegistry.ObjectHolder("mobultion:item_thunder_wand")
	public static ItemThunderWand thunderWand;
	@GameRegistry.ObjectHolder("mobultion:item_hay_hat")
	public static ItemHayHat hayHat;
	@GameRegistry.ObjectHolder("mobultion:item_ender_glass_shot")
	public static ItemEnderGlassShot enderGlassShot;
	@GameRegistry.ObjectHolder("mobultion:item_spawn_changer")
	public static ItemSpawnChanger spawnChanger;


	@SideOnly(Side.CLIENT)
    public static void initModels() {
		forestBow.initModel();
		healingWand.initModel();
		heartArrow.initModel();
		hammer.initModel();
		fireSword.initModel();
		health.initModel();
		fork.initModel();
		knife.initModel();
		hat.initModel();
		enderFlake.initModel();
		enderBlaze.initModel();
		enderGlassShot.initModel();
		corruptedBone.initModel();
		corruptedBonemeal.initModel();
		holyShard.initModel();
		hypnoBall.initModel();
		fangNecklace.initModel();
		fang.initModel();
		magmaArrow.initModel();
		magicTouch.initModel();
		sorcererBreath.initModel();
		witherSpine.initModel();
		spineAsh.initModel();
		pigsheathTunic.initModel();
		pigsheathHelmet.initModel();
		pigsheathBoots.initModel();
		pigsheathLeggings.initModel();
		flamingChip.initModel();
		netherRuby.initModel();
		pigmanFlesh.initModel();
		thunderWand.initModel();
		hayHat.initModel();
		spawnChanger.initModel();
    }
	

}
