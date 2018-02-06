package mcpecommander.mobultion.init;

import mcpecommander.mobultion.items.ItemEnderBlaze;
import mcpecommander.mobultion.items.ItemEnderFlake;
import mcpecommander.mobultion.items.ItemFireSword;
import mcpecommander.mobultion.items.ItemForestBow;
import mcpecommander.mobultion.items.ItemFork;
import mcpecommander.mobultion.items.ItemHammer;
import mcpecommander.mobultion.items.ItemHat;
import mcpecommander.mobultion.items.ItemHealingWand;
import mcpecommander.mobultion.items.ItemHealth;
import mcpecommander.mobultion.items.ItemHeartArrow;
import mcpecommander.mobultion.items.ItemKnife;
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
    }
	

}
