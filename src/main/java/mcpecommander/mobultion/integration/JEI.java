package mcpecommander.mobultion.integration;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModItems;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IFocus.Mode;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@JEIPlugin
public class JEI implements IModPlugin {
	public static IJeiRuntime jeiRuntime = null;
	
	public static void JEIShowGUI(ItemStack stack){
		jeiRuntime.getRecipesGui().show(jeiRuntime.getRecipeRegistry().createFocus(Mode.INPUT, stack));
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		//Hidden until fixed to work with JEI.
		jeiRuntime.getRecipeRegistry()
				.hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(
						ForgeRegistries.RECIPES
								.getValue(new ResourceLocation(Reference.MOD_ID, "fang_necklace_repair")),
						VanillaRecipeCategoryUid.CRAFTING));
		JEI.jeiRuntime = jeiRuntime;
	}
	
	@Override
	public void register(IModRegistry registry) {		
		registry.addIngredientInfo(new ItemStack(ModItems.corruptedBone), ItemStack.class, "mobultion:corrupted_bone.info");
		registry.addIngredientInfo(new ItemStack(ModItems.corruptedBonemeal), ItemStack.class , "mobultion:corrupted_bonemeal.info");
		ItemStack[] knifeStack = {new ItemStack(ModItems.fork), new ItemStack(ModItems.knife)};
		registry.addIngredientInfo(Lists.newArrayList(knifeStack) , ItemStack.class, "mobultion:fork.info");
		registry.addIngredientInfo(new ItemStack(ModItems.enderBlaze), ItemStack.class, "mobultion:ender_blaze.info");
		registry.addIngredientInfo(new ItemStack(ModItems.enderFlake), ItemStack.class, "mobultion:ender_flake.info");
		registry.addIngredientInfo(new ItemStack(ModItems.fang), ItemStack.class, "mobultion:fang.info");
		ItemStack[] necklaceStack = {new ItemStack(ModItems.fangNecklace, 1 , 0), new ItemStack(ModItems.fangNecklace, 1, 1), new ItemStack(ModItems.fangNecklace, 1, 2), new ItemStack(ModItems.fangNecklace, 1, 3), new ItemStack(ModItems.fangNecklace, 1, 4), new ItemStack(ModItems.fangNecklace, 1, 5)};
		registry.addIngredientInfo(Lists.newArrayList(necklaceStack), ItemStack.class, "mobultion:fang_necklace.info");
		registry.addIngredientInfo(new ItemStack(ModItems.fireSword), ItemStack.class, "mobultion:fire_sword.info");
		registry.addIngredientInfo(new ItemStack(ModItems.forestBow), ItemStack.class, "mobultion:forest_bow.info");
		registry.addIngredientInfo(new ItemStack(ModItems.hammer), ItemStack.class, "mobultion:hammer.info");
		registry.addIngredientInfo(new ItemStack(ModItems.hat), ItemStack.class, "mobultion:hat.info");
		registry.addIngredientInfo(new ItemStack(ModItems.healingWand), ItemStack.class, "mobultion:healing_wand.info");
		registry.addIngredientInfo(new ItemStack(ModItems.health), ItemStack.class, "mobultion:health.info");
		registry.addIngredientInfo(new ItemStack(ModItems.heartArrow), ItemStack.class, "mobultion:heart_arrow.info");
		registry.addIngredientInfo(new ItemStack(ModItems.holyShard), ItemStack.class, "mobultion:holy_shard.info");
		registry.addIngredientInfo(new ItemStack(ModItems.hypnoBall), ItemStack.class, "mobultion:hypno_ball.info");
		registry.addIngredientInfo(new ItemStack(ModItems.magmaArrow), ItemStack.class, "mobultion:magma_arrow.info");
		registry.addIngredientInfo(new ItemStack(ModItems.magicTouch), ItemStack.class, "mobultion:magic_touch.info");
		registry.addIngredientInfo(new ItemStack(ModItems.sorcererBreath), ItemStack.class, "mobultion:sorcerer_breath.info");
		registry.addIngredientInfo(new ItemStack(ModItems.witherSpine), ItemStack.class, "mobultion:wither_spine.info");
		registry.addIngredientInfo(new ItemStack(ModItems.spineAsh), ItemStack.class, "mobultion:spine_ash.info");
		registry.addIngredientInfo(new ItemStack(ModItems.pigmanFlesh), ItemStack.class, "mobultion:pigman_flesh.info");
		ItemStack[] armorStack = {new ItemStack(ModItems.pigsheathBoots), new ItemStack(ModItems.pigsheathLeggings), new ItemStack(ModItems.pigsheathTunic), new ItemStack(ModItems.pigsheathHelmet)};
		registry.addIngredientInfo(Lists.newArrayList(armorStack), ItemStack.class, "mobultion:pigsheath_armor.info");
		registry.addIngredientInfo(new ItemStack(ModItems.flamingChip), ItemStack.class, "mobultion:flaming_chip.info");
		registry.addIngredientInfo(new ItemStack(ModItems.netherRuby), ItemStack.class, "mobultion:nether_ruby.info");
		registry.addIngredientInfo(new ItemStack(ModItems.hayHat), ItemStack.class, "mobultion:hay_hat.info");
		registry.addIngredientInfo(new ItemStack(ModItems.thunderWand), ItemStack.class, "mobultion:thunder_wand.info");
		
		for (EntityList.EntityEggInfo info : EntityList.ENTITY_EGGS.values())
        {
			if(info.spawnedID.getResourceDomain().equals(Reference.MOD_ID)){
				ItemStack itemstack = new ItemStack(Items.SPAWN_EGG, 1);
            	ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, info.spawnedID);
            	Entity entity = ForgeRegistries.ENTITIES.getValue(info.spawnedID).newInstance(null);
            	if(entity instanceof EntityLiving){
            		EntityLiving living = (EntityLiving) entity;
            		TextComponentString string = new TextComponentString(" \u2665");
            		string.getStyle().setBold(true).setColor(TextFormatting.RED);
            		String health = "Max Health: " + (living.getMaxHealth()/2f)+ string.getFormattedText();
            		String speed = "Speed: " + living.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
            		String attack = "Attack Damage: " + living.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
            		String information = Reference.MobultionEntities.getEnumByRegistery(info.spawnedID).getInfo();
            		ArrayList<String> spawnList = new ArrayList<>();
            		
            		for(Biome biome : ForgeRegistries.BIOMES.getValuesCollection()){
            			for(Biome.SpawnListEntry entry :biome.getSpawnableList(EnumCreatureType.MONSTER)){
            				if(entry.entityClass == entity.getClass()){
            					spawnList.add(biome.getBiomeName() + " ");
            				}
            			}
            		}
            		if(!spawnList.isEmpty()){
            			registry.addIngredientInfo(itemstack, ItemStack.class, health, speed, attack, " ", information, " ", "Spawns:", spawnList.toString());
            		}else{
            			registry.addIngredientInfo(itemstack, ItemStack.class, health, speed, attack, " ", information, " ", "Does not spawn anywhere");
            		}

            	}
            	
			}
        }
		
		
		
	}

}
