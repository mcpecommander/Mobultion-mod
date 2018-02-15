package mcpecommander.mobultion.events;

import java.util.List;
import java.util.UUID;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.mites.EntityWoodMite;
import mcpecommander.mobultion.entity.entities.skeletons.EntityCorruptedSkeleton;
import mcpecommander.mobultion.entity.entities.spiders.EntityAnimatedSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntityMiniSpider;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIMoveToNearestDoctor;
import mcpecommander.mobultion.init.ModEnchantments;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.init.ModPotions;
import mcpecommander.mobultion.mobConfigs.MitesConfig;
import mcpecommander.mobultion.mobConfigs.SkeletonsConfig;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import mcpecommander.mobultion.mobConfigs.ZombiesConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class CommonEvent {
	private static final UUID BLESSED_ID = UUID.fromString("020E6DFB-87AE-9653-9556-561210E291A0");
	private static final AttributeModifier UNBLESSED = (new AttributeModifier(BLESSED_ID, "un_blessed", -0.1D, 0)).setSaved(true);
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent e) {
		EntityLivingBase entity = e.getEntityLiving();
		if (entity instanceof EntitySpider || entity instanceof EntityAnimatedSpider) {
			if (entity.getAttributeMap().getAttributeInstanceByName("generic.blessed").getAttributeValue() > 0.0d ) {
				if(entity.getRNG().nextInt(SpidersConfig.spiders.angel.reviveChance) == 0){
					entity.setHealth(entity.getMaxHealth());
					entity.playSound(SoundEvents.ITEM_TOTEM_USE, 1f, 1f);
					entity.removeActivePotionEffect(ModPotions.potionBlessed);
					entity.getAttributeMap().getAttributeInstanceByName("generic.blessed").removeAllModifiers();
					entity.getAttributeMap().getAttributeInstanceByName("generic.blessed").applyModifier(UNBLESSED);
				}
			}
		}
		if (ZombiesConfig.zombies.magma.alwaysLava && e.getSource().getTrueSource() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) e.getSource().getTrueSource();
			if (player.getHeldItemMainhand().getItem() == ModItems.fireSword) {
				player.getHeldItemMainhand().damageItem(25, player);
				if (player.world.isAirBlock(entity.getPosition())
						&& entity.world.isSideSolid(entity.getPosition().add(0, -1, 0), EnumFacing.UP)) {
					player.world.setBlockState(entity.getPosition(), Blocks.FLOWING_LAVA.getDefaultState(), 3);
				}
			}
		}
		
		if(entity instanceof EntityPlayerMP){
			EntityPlayerMP player = (EntityPlayerMP) entity;
			if(!player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() && EnchantmentHelper.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD)).keySet().contains(ModEnchantments.blessed)){
				if(!player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() && EnchantmentHelper.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)).keySet().contains(ModEnchantments.blessed)){
					if(!player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() && EnchantmentHelper.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS)).keySet().contains(ModEnchantments.blessed)){
						if(!player.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty() && EnchantmentHelper.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.FEET)).keySet().contains(ModEnchantments.blessed)){
							if(player.getRNG().nextFloat() > 1.2f){
								Reference.removeEnchant(Enchantment.getEnchantmentID(ModEnchantments.blessed), player.getItemStackFromSlot(EntityEquipmentSlot.FEET));
							}
							if(player.getRNG().nextFloat() > 1.2f){
								Reference.removeEnchant(Enchantment.getEnchantmentID(ModEnchantments.blessed), player.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
							}
							if(player.getRNG().nextFloat() > 1.2f){
								Reference.removeEnchant(Enchantment.getEnchantmentID(ModEnchantments.blessed), player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
							}
							if(player.getRNG().nextFloat() > 1.2f){
								Reference.removeEnchant(Enchantment.getEnchantmentID(ModEnchantments.blessed), player.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
							}
							player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.8f, 1f);
							player.setHealth(20f);
							e.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onConstruction(EntityConstructing e){
		if(e.getEntity() instanceof EntitySpider || e.getEntity() instanceof EntityAnimatedSpider){
			((EntityLivingBase) e.getEntity()).getAttributeMap().registerAttribute((new RangedAttribute((IAttribute)null, "generic.blessed", 0.0D, -100D, 100D)).setShouldWatch(true));
		}
	}
	
	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.RightClickBlock e) {
		TileEntity tile = e.getWorld().getTileEntity(e.getPos());
		if (tile != null && tile instanceof TileEntityChest) {
			TileEntityLockableLoot chest = (TileEntityLockableLoot) tile;
			if (chest.getLootTable() != null && chest.getLootTable().equals(LootTableList.CHESTS_DESERT_PYRAMID)
					&& e.getEntityPlayer().getRNG().nextFloat() < 0.02f) {
				BlockPos pos = findPos(e.getPos().getAllInBox(e.getPos().add(-1, 0, -1), e.getPos().add(1, 1, 1)),
						e.getWorld());
				if (pos != null && SkeletonsConfig.skeletons.corrupted.spawnFromLootChests) {
					EntityCorruptedSkeleton entity = new EntityCorruptedSkeleton(e.getWorld());
					entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BONE));
					entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					entity.setAttackTarget(e.getEntityPlayer());
					if (entity.getCanSpawnHere()) {
						e.getWorld().spawnEntity(entity);
					}
				}
			}
		}
	}
	
	private static BlockPos findPos(Iterable<BlockPos> blocks, World world){
		for(BlockPos pos : blocks){
			if(world.isAirBlock(pos) && world.isAirBlock(pos.add(0, 1, 0))){
				return pos;
			}
		}
		return null;
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
				if (!e.getEntityLiving().isPotionActive(ModPotions.potionVomit)) {
					ModPotions.potionVomit.isReady = false;
					ModPotions.potionVomit.mul = 0.0f;
				}
				if (!e.getEntityLiving().isPotionActive(ModPotions.potionFreeze)) {
					ModPotions.potionFreeze.isReady = false;
					ModPotions.potionFreeze.mul = 0.0f;
				}
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
	public static void onJoin(EntityJoinWorldEvent e){
		if (e.getEntity() instanceof EntityZombie) {
			EntityZombie zombie = (EntityZombie) e.getEntity();
			EntityAIMoveToNearestDoctor task = new EntityAIMoveToNearestDoctor(zombie, 1.2D, 16D);
			if (!zombie.tasks.taskEntries.contains(task)) {
				zombie.tasks.addTask(1, task);
			}
		}
	}

	@SubscribeEvent
	public static void onImpact(ProjectileImpactEvent.Throwable e) {
		if (e.getThrowable() instanceof EntityPotion) {
			EntityPotion potion = (EntityPotion) e.getThrowable();
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
	
	@SubscribeEvent
	public static void onHarvestBlock(BlockEvent.HarvestDropsEvent e){
		if(e.getState().getBlock().isWood(e.getWorld(), e.getPos())){
			if(e.getHarvester() != null && e.getHarvester() instanceof EntityPlayerMP){
				if(e.getWorld().rand.nextInt(MitesConfig.mites.wood.spawnChance) == 0){ 
					EntityWoodMite mite = new EntityWoodMite(e.getWorld());
					mite.setPosition(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ());
					mite.getLookHelper().setLookPositionWithEntity(e.getHarvester(), 30, 30);
					mite.setAttackTarget(e.getHarvester());
					e.getWorld().spawnEntity(mite);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MOD_ID)) {
			ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
		}
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void spawnMiniSpider(LivingSpawnEvent.CheckSpawn e) {
		if (!e.isSpawner() && e.getResult() == Result.ALLOW) {
			if (e.getEntityLiving() instanceof EntityAnimatedSpider || e.getEntityLiving() instanceof EntitySpider) {
				if (e.getEntityLiving().getRNG().nextInt(SpidersConfig.spiders.mini.spawnChance) == 1) {
					EntityMiniSpider mini = new EntityMiniSpider(e.getWorld());
					mini.setLocationAndAngles(e.getX() + e.getEntityLiving().getRNG().nextGaussian(), e.getY(),
							e.getZ() + e.getEntityLiving().getRNG().nextGaussian(), 0, 0);
					e.getWorld().spawnEntity(mini);
				}
			}
		}
		//Not really needed
//		if (!e.isSpawner() && e.getResult() == Result.ALLOW) {
//			if (e.getEntityLiving() instanceof EntityAnimatedZombie || e.getEntityLiving() instanceof EntityZombie) {
//				if (e.getEntityLiving().getRNG().nextInt(100) == 1) {
//					EntityDoctorZombie doctor = new EntityDoctorZombie(e.getWorld());
//					doctor.setLocationAndAngles(e.getX() + e.getEntityLiving().getRNG().nextGaussian(), e.getY(),
//							e.getZ() + e.getEntityLiving().getRNG().nextGaussian(), 0, 0);
//					e.getWorld().spawnEntity(doctor);
//				}
//			}
//		}
	}

}
