package mcpecommander.luggagemod.events;

import java.util.List;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.entity.entities.zombies.EntityDoctorZombie;
import mcpecommander.luggagemod.entity.entityAI.zombiesAI.EntityAIMoveToNearestDoctor;
import mcpecommander.luggagemod.init.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.ThrowableImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonEvent {
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent e) {
		EntityLivingBase entity = e.getEntityLiving();
		if (entity.getEntityData().getBoolean("is")) {
			if (entity.getRNG().nextInt(2) == 0) {
				e.setCanceled(true);
				entity.setHealth(entity.getMaxHealth());
				entity.playSound(SoundEvents.ITEM_TOTEM_USE, 1f, 1f);
			}
			entity.getEntityData().setBoolean("is", false);
		}
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent e) {
		// for testing.
		// if(e.getEntityLiving() instanceof EntityPlayer){
		// EntityPlayer player = (EntityPlayer) e.getEntityLiving();
		// System.out.println(player.getHeldItemMainhand() + " " +
		// player.getHeldItemMainhand().getTagCompound());
		// }
		if (e.getEntityLiving() != null && !e.getEntityLiving().isDead) {
			if (e.getEntityLiving() instanceof EntityPlayer) {
				if (!e.getEntityLiving().isPotionActive(ModPotions.potionJokerness)) {
					ModPotions.potionJokerness.isReady = false;
				}
			}
		}
		if(e.getEntityLiving() instanceof EntityZombie){
			EntityZombie zombie = (EntityZombie) e.getEntityLiving();
			EntityAIMoveToNearestDoctor task = new EntityAIMoveToNearestDoctor(zombie, 1.2D, 16D);
			if(!zombie.tasks.taskEntries.contains(task)){
				zombie.tasks.addTask(1, task);
			}
		}
	}

	@SubscribeEvent
	public static void onAttack(AttackEntityEvent e) {
		if (!e.getEntityPlayer().getHeldItemMainhand().isEmpty()) {
			if (e.getEntityPlayer().getCooldownTracker()
					.hasCooldown(e.getEntityPlayer().getHeldItemMainhand().getItem())) {
				e.setCanceled(true);
			}
		}

	}

	@SubscribeEvent
	public static void onImpact(ThrowableImpactEvent e) {
		if (e.getEntityThrowable() instanceof EntityPotion) {
			EntityPotion potion = (EntityPotion) e.getEntityThrowable();
			if (!potion.world.isRemote) {
				ItemStack itemstack = potion.getPotion();
				PotionType potiontype = PotionUtils.getPotionFromItem(itemstack);
				List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
				if (potiontype == PotionTypes.WATER && list.isEmpty()) {
					if (e.getRayTraceResult().entityHit != null) {
						e.getRayTraceResult().entityHit.extinguish();
					}
				}
			}
		}
	}

	// @SubscribeEvent
	// public static void onJoining(EntityJoinWorldEvent e){
	// if(e.getEntity() instanceof EntitySkeletontemp){
	// if(e.getWorld().isRemote){
	// ((EntitySkeletontemp)
	// e.getEntity()).getAnimationHandler().startAnimation(Reference.MOD_ID,
	// "skeleton_spawn", 0, (IAnimated) e.getEntity());
	// }
	// }
	// }

	@SubscribeEvent
	public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MOD_ID)) {
			ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
		}
	}

}
