package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityAnimatedEnderman;
import mcpecommander.mobultion.entity.entities.skeletons.EntityAnimatedSkeleton;
import mcpecommander.mobultion.entity.entities.spiders.EntityAnimatedSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntityMiniSpider;
import mcpecommander.mobultion.entity.entities.zombies.EntityAnimatedZombie;
import mcpecommander.mobultion.gui.GuiMobBiomes;
import mcpecommander.mobultion.mobConfigs.EndermenConfig;
import mcpecommander.mobultion.mobConfigs.SkeletonsConfig;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import mcpecommander.mobultion.mobConfigs.ZombiesConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawnChanger extends Item {

	public ItemSpawnChanger() {
		this.setRegistryName(Reference.MobultionItems.SPAWNCHANGER.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.SPAWNCHANGER.getUnlocalizedName());

		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
		this.setMaxStackSize(1);

	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (player.world.isRemote && (entity instanceof EntityAnimatedEnderman
				|| entity instanceof EntityAnimatedSkeleton || entity instanceof EntityAnimatedZombie || entity instanceof EntityAnimatedSpider)) {
			if (EntityList.getKey(entity) != null && !(entity instanceof EntityMiniSpider)) {
				ResourceLocation reg = EntityList.getKey(entity);
				GuiMobBiomes gui = new GuiMobBiomes();
				gui.entity = (EntityLiving) entity;
				gui.maxS = String.valueOf(getEntityMaxByName(reg.getResourcePath()));
				gui.minS = String.valueOf(getEntityMinByName(reg.getResourcePath()));
				gui.weightS = String.valueOf(getEntityWeightByName(reg.getResourcePath()));
				String[] temp = getEntityBiomesByName(reg.getResourcePath());
				for (int i = 0; i < temp.length; i++) {
					gui.biomesList.add(temp[i]);
				}
				Minecraft.getMinecraft().displayGuiScreen(gui);
			}else if(entity instanceof EntityMiniSpider) {
				player.sendStatusMessage(new TextComponentString("The Mini spider is not intended to be spawned naturally"), true);
			}
		}
		return true;
	}

	public static int getEntityMinByName(String name) {
		if (name.equals(
				String.valueOf(EndermenConfig.endermen.magma.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.magma.spawnRates.min;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.ice.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.ice.spawnRates.min;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.wandering.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.wandering.spawnRates.min;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.gardener.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.gardener.spawnRates.min;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.glass.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.glass.spawnRates.min;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.corrupted.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.corrupted.spawnRates.min;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.joker.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.joker.spawnRates.min;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.magma.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.magma.spawnRates.min;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.shaman.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.shaman.spawnRates.min;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.sniper.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.sniper.spawnRates.min;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.withering.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.withering.spawnRates.min;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.vampire.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.vampire.spawnRates.min;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.angel.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.angel.spawnRates.min;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.hypno.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.hypno.spawnRates.min;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.magma.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.magma.spawnRates.min;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.mother.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.mother.spawnRates.min;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.sorcerer.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.sorcerer.spawnRates.min;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.speedy.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.speedy.spawnRates.min;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.wither.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.wither.spawnRates.min;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.magma.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.magma.spawnRates.min;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.doctor.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.doctor.spawnRates.min;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.worker.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.worker.spawnRates.min;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.knight.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.knight.spawnRates.min;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.goro.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.goro.spawnRates.min;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.ravenous.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.ravenous.spawnRates.min;
		}
		return -1;
	}

	public static void setEntityMinByName(String name, int min) {
		if (name.equals(
				String.valueOf(EndermenConfig.endermen.magma.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.magma.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.ice.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.ice.spawnRates.min = min;
			return;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.wandering.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.wandering.spawnRates.min = min;
			return;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.gardener.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.gardener.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.glass.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.glass.spawnRates.min = min;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.corrupted.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.corrupted.spawnRates.min = min;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.joker.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.joker.spawnRates.min = min;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.magma.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.magma.spawnRates.min = min;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.shaman.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.shaman.spawnRates.min = min;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.sniper.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.sniper.spawnRates.min = min;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.withering.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.withering.spawnRates.min = min;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.vampire.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.vampire.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.angel.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.angel.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.hypno.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.hypno.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.magma.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.magma.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.mother.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.mother.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.sorcerer.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.sorcerer.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.speedy.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.speedy.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.wither.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.wither.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.magma.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.magma.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.doctor.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.doctor.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.worker.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.worker.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.knight.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.knight.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.goro.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.goro.spawnRates.min = min;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.ravenous.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.ravenous.spawnRates.min = min;
			return;
		}
	}

	public static int getEntityMaxByName(String name) {
		if (name.equals(
				String.valueOf(EndermenConfig.endermen.magma.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.magma.spawnRates.max;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.ice.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.ice.spawnRates.max;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.wandering.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.wandering.spawnRates.max;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.gardener.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.gardener.spawnRates.max;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.glass.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.glass.spawnRates.max;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.corrupted.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.corrupted.spawnRates.max;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.joker.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.joker.spawnRates.max;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.magma.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.magma.spawnRates.max;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.shaman.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.shaman.spawnRates.max;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.sniper.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.sniper.spawnRates.max;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.withering.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.withering.spawnRates.max;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.vampire.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.vampire.spawnRates.max;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.angel.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.angel.spawnRates.max;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.hypno.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.hypno.spawnRates.max;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.magma.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.magma.spawnRates.max;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.mother.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.mother.spawnRates.max;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.sorcerer.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.sorcerer.spawnRates.max;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.speedy.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.speedy.spawnRates.max;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.wither.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.wither.spawnRates.max;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.magma.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.magma.spawnRates.max;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.doctor.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.doctor.spawnRates.max;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.worker.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.worker.spawnRates.max;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.knight.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.knight.spawnRates.max;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.goro.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.goro.spawnRates.max;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.ravenous.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.ravenous.spawnRates.max;
		}
		return -1;
	}

	public static void setEntityMaxByName(String name, int max) {
		if (name.equals(
				String.valueOf(EndermenConfig.endermen.magma.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.magma.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.ice.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.ice.spawnRates.max = max;
			return;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.wandering.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.wandering.spawnRates.max = max;
			return;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.gardener.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.gardener.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.glass.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.glass.spawnRates.max = max;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.corrupted.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.corrupted.spawnRates.max = max;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.joker.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.joker.spawnRates.max = max;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.magma.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.magma.spawnRates.max = max;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.shaman.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.shaman.spawnRates.max = max;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.sniper.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.sniper.spawnRates.max = max;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.withering.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.withering.spawnRates.max = max;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.vampire.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.vampire.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.angel.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.angel.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.hypno.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.hypno.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.magma.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.magma.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.mother.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.mother.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.sorcerer.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.sorcerer.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.speedy.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.speedy.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.wither.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.wither.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.magma.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.magma.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.doctor.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.doctor.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.worker.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.worker.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.knight.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.knight.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.goro.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.goro.spawnRates.max = max;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.ravenous.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.ravenous.spawnRates.max = max;
			return;
		}
	}

	public static int getEntityWeightByName(String name) {
		if (name.equals(
				String.valueOf(EndermenConfig.endermen.magma.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.magma.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.ice.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.ice.spawnRates.weight;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.wandering.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.wandering.spawnRates.weight;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.gardener.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.gardener.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.glass.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.glass.spawnRates.weight;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.corrupted.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.corrupted.spawnRates.weight;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.joker.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.joker.spawnRates.weight;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.magma.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.magma.spawnRates.weight;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.shaman.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.shaman.spawnRates.weight;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.sniper.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.sniper.spawnRates.weight;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.withering.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.withering.spawnRates.weight;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.vampire.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.vampire.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.angel.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.angel.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.hypno.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.hypno.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.magma.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.magma.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.mother.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.mother.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.sorcerer.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.sorcerer.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.speedy.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.speedy.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.wither.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.wither.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.magma.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.magma.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.doctor.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.doctor.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.worker.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.worker.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.knight.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.knight.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.goro.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.goro.spawnRates.weight;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.ravenous.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.ravenous.spawnRates.weight;
		}
		return -1;
	}

	public static void setEntityWeightByName(String name, int weight) {
		if (name.equals(
				String.valueOf(EndermenConfig.endermen.magma.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.magma.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.ice.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.ice.spawnRates.weight = weight;
			return;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.wandering.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.wandering.spawnRates.weight = weight;
			return;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.gardener.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.gardener.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.glass.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.glass.spawnRates.weight = weight;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.corrupted.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.corrupted.spawnRates.weight = weight;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.joker.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.joker.spawnRates.weight = weight;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.magma.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.magma.spawnRates.weight = weight;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.shaman.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.shaman.spawnRates.weight = weight;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.sniper.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.sniper.spawnRates.weight = weight;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.withering.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.withering.spawnRates.weight = weight;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.vampire.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.vampire.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.angel.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.angel.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.hypno.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.hypno.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.magma.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.magma.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.mother.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.mother.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.sorcerer.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.sorcerer.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.speedy.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.speedy.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.wither.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.wither.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.magma.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.magma.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.doctor.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.doctor.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.worker.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.worker.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.knight.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.knight.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.goro.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.goro.spawnRates.weight = weight;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.ravenous.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.ravenous.spawnRates.weight = weight;
			return;
		}
	}

	public static String[] getEntityBiomesByName(String name) {
		if (name.equals(
				String.valueOf(EndermenConfig.endermen.magma.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.magma.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.ice.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.ice.spawnRates.biomes;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.wandering.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.wandering.spawnRates.biomes;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.gardener.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.gardener.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.glass.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			return EndermenConfig.endermen.glass.spawnRates.biomes;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.corrupted.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.corrupted.spawnRates.biomes;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.joker.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.joker.spawnRates.biomes;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.magma.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.magma.spawnRates.biomes;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.shaman.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.shaman.spawnRates.biomes;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.sniper.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.sniper.spawnRates.biomes;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.withering.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.withering.spawnRates.biomes;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.vampire.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			return SkeletonsConfig.skeletons.vampire.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.angel.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.angel.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.hypno.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.hypno.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.magma.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.magma.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.mother.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.mother.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.sorcerer.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.sorcerer.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.speedy.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.speedy.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.wither.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			return SpidersConfig.spiders.wither.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.magma.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.magma.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.doctor.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.doctor.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.worker.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.worker.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.knight.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.knight.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.goro.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.goro.spawnRates.biomes;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.ravenous.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			return ZombiesConfig.zombies.ravenous.spawnRates.biomes;
		}
		return new String[] {};
	}

	public static void setEntityBiomesByName(String name, String... biomes) {
		if (name.equals(
				String.valueOf(EndermenConfig.endermen.magma.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.magma.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.ice.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.ice.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.wandering.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.wandering.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(String
				.valueOf(EndermenConfig.endermen.gardener.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.gardener.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(EndermenConfig.endermen.glass.getClass().getSimpleName().toLowerCase() + "_enderman"))) {
			EndermenConfig.endermen.glass.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.corrupted.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.corrupted.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.joker.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.joker.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.magma.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.magma.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.shaman.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.shaman.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.sniper.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.sniper.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.withering.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.withering.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(String
				.valueOf(SkeletonsConfig.skeletons.vampire.getClass().getSimpleName().toLowerCase() + "_skeleton"))) {
			SkeletonsConfig.skeletons.vampire.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.angel.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.angel.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.hypno.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.hypno.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.magma.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.magma.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.mother.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.mother.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.sorcerer.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.sorcerer.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.speedy.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.speedy.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(SpidersConfig.spiders.wither.getClass().getSimpleName().toLowerCase() + "_spider"))) {
			SpidersConfig.spiders.wither.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.magma.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.magma.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.doctor.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.doctor.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.worker.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.worker.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.knight.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.knight.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.goro.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.goro.spawnRates.biomes = biomes;
			return;
		} else if (name.equals(
				String.valueOf(ZombiesConfig.zombies.ravenous.getClass().getSimpleName().toLowerCase() + "_zombie"))) {
			ZombiesConfig.zombies.ravenous.spawnRates.biomes = biomes;
			return;
		}
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
