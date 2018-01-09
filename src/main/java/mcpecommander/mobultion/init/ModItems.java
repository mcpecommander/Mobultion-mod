package mcpecommander.mobultion.init;

import mcpecommander.mobultion.items.ItemFireSword;
import mcpecommander.mobultion.items.ItemForestBow;
import mcpecommander.mobultion.items.ItemHammer;
import mcpecommander.mobultion.items.ItemHealingWand;
import mcpecommander.mobultion.items.ItemHealth;
import mcpecommander.mobultion.items.ItemHeartArrow;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	@GameRegistry.ObjectHolder("mmm:item_forest_bow")
	public static ItemForestBow forestBow;
	@GameRegistry.ObjectHolder("mmm:item_healing_wand")
	public static ItemHealingWand healingWand;
	@GameRegistry.ObjectHolder("mmm:item_heart_arrow")
	public static ItemHeartArrow heartArrow;
	@GameRegistry.ObjectHolder("mmm:item_hammer")
	public static ItemHammer hammer;
	@GameRegistry.ObjectHolder("mmm:item_fire_sword")
	public static ItemFireSword fireSword;
	@GameRegistry.ObjectHolder("mmm:item_health")
	public static ItemHealth health;

	@SideOnly(Side.CLIENT)
    public static void initModels() {
		forestBow.initModel();
		healingWand.initModel();
		heartArrow.initModel();
		hammer.initModel();
		fireSword.initModel();
		health.initModel();
		
    }
	

}
