package mcpecommander.luggagemod.init;

import mcpecommander.luggagemod.items.ItemArmorJoker;
import mcpecommander.luggagemod.items.ItemCheese;
import mcpecommander.luggagemod.items.ItemCheeseAndCracker;
import mcpecommander.luggagemod.items.ItemCracker;
import mcpecommander.luggagemod.items.ItemFireSword;
import mcpecommander.luggagemod.items.ItemHammer;
import mcpecommander.luggagemod.items.ItemHealingWand;
import mcpecommander.luggagemod.items.ItemHealth;
import mcpecommander.luggagemod.items.ItemHeartArrow;
import mcpecommander.luggagemod.items.multiModel.ItemForestBow;
import mcpecommander.luggagemod.items.multiModel.ItemWand;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	@GameRegistry.ObjectHolder("mlm:item_cheese")
	public static ItemCheese cheese;
	@GameRegistry.ObjectHolder("mlm:item_cracker")
	public static ItemCracker cracker;
	@GameRegistry.ObjectHolder("mlm:item_cheese_and_cracker")
	public static ItemCheeseAndCracker cheese_and_cracker;
	@GameRegistry.ObjectHolder("mlm:item_wand")
	public static ItemWand wand;
	@GameRegistry.ObjectHolder("mlm:item_forest_bow")
	public static ItemForestBow forestBow;
	@GameRegistry.ObjectHolder("mlm:item_healing_wand")
	public static ItemHealingWand healingWand;
	@GameRegistry.ObjectHolder("mlm:item_heart_arrow")
	public static ItemHeartArrow heartArrow;
	@GameRegistry.ObjectHolder("mlm:item_hat")
	public static ItemArmorJoker hat;
	@GameRegistry.ObjectHolder("mlm:item_hammer")
	public static ItemHammer hammer;
	@GameRegistry.ObjectHolder("mlm:item_fire_sword")
	public static ItemFireSword fireSword;
	@GameRegistry.ObjectHolder("mlm:item_health")
	public static ItemHealth health;

	@SideOnly(Side.CLIENT)
    public static void initModels() {
		//cheese = new ItemCheese(2, 2f, false);
		cheese.initModel();
		//cracker = new ItemCracker();
		cracker.initModel();
		//cheese_and_cracker = new ItemCheeseAndCracker();
		cheese_and_cracker.initModel();
		//wand = new ItemWand();
		wand.initModel();
		forestBow.initModel();
		healingWand.initModel();
		heartArrow.initModel();
		hat.initModel();
		hammer.initModel();
		fireSword.initModel();
		health.initModel();
		
    }
	

}
