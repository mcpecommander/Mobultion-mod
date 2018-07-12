package mcpecommander.mobultion.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Predicate;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.mites.EntityWoodMite;
import mcpecommander.mobultion.entity.entities.skeletons.EntityCorruptedSkeleton;
import mcpecommander.mobultion.entity.entities.spiders.EntityAnimatedSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntityMiniSpider;
import mcpecommander.mobultion.entity.entityAI.EntityAIFollowPlayerWithPigsheath;
import mcpecommander.mobultion.entity.entityAI.EntityAITargetHurtBoss;
import mcpecommander.mobultion.entity.entityAI.spidersAI.angelAI.EntityAIAngelSpiderHeal;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIMoveToNearestDoctor;
import mcpecommander.mobultion.init.ModEnchantments;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.init.ModPotions;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathBoots;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathHelmet;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathLeggings;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathTunic;
import mcpecommander.mobultion.mobConfigs.MitesConfig;
import mcpecommander.mobultion.mobConfigs.SkeletonsConfig;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import mcpecommander.mobultion.mobConfigs.ZombiesConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.LootTableLoadEvent;
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
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class CommonEvent {
	private static final UUID UNBLESSED_ID = UUID.fromString("020E6DFB-87AE-9653-9556-561210E291A0");
	private static final AttributeModifier UNBLESSED = (new AttributeModifier(UNBLESSED_ID, "un_blessed", -0.1D, 0))
			.setSaved(true);

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent e) {
		EntityLivingBase entity = e.getEntityLiving();
		if (entity instanceof EntitySpider || entity instanceof EntityAnimatedSpider) {
			IAttributeInstance instance = entity.getAttributeMap().getAttributeInstanceByName("generic.blessed");
			if (instance != null && instance.getAttributeValue() > 0.0d) {
				if (entity.getRNG().nextInt(SpidersConfig.spiders.angel.reviveChance) == 0) {
					entity.setHealth(entity.getMaxHealth());
					entity.playSound(SoundEvents.ITEM_TOTEM_USE, 1f, 1f);
					entity.removeActivePotionEffect(ModPotions.potionBlessed);
					entity.getAttributeMap().getAttributeInstanceByName("generic.blessed").removeModifier(EntityAIAngelSpiderHeal.BLESSED_ID);
					entity.getAttributeMap().getAttributeInstanceByName("generic.blessed").removeModifier(UNBLESSED_ID);
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

		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			if (!player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()
					&& EnchantmentHelper.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD)).keySet()
							.contains(ModEnchantments.blessed)) {
				if (!player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()
						&& EnchantmentHelper.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST))
								.keySet().contains(ModEnchantments.blessed)) {
					if (!player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()
							&& EnchantmentHelper.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS))
									.keySet().contains(ModEnchantments.blessed)) {
						if (!player.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty() && EnchantmentHelper
								.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.FEET)).keySet()
								.contains(ModEnchantments.blessed)) {
							if (player.getRNG().nextFloat() > 1.2f) {
								Reference.removeEnchant(Enchantment.getEnchantmentID(ModEnchantments.blessed),
										player.getItemStackFromSlot(EntityEquipmentSlot.FEET));
							}
							if (player.getRNG().nextFloat() > 1.2f) {
								Reference.removeEnchant(Enchantment.getEnchantmentID(ModEnchantments.blessed),
										player.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
							}
							if (player.getRNG().nextFloat() > 1.2f) {
								Reference.removeEnchant(Enchantment.getEnchantmentID(ModEnchantments.blessed),
										player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
							}
							if (player.getRNG().nextFloat() > 1.2f) {
								Reference.removeEnchant(Enchantment.getEnchantmentID(ModEnchantments.blessed),
										player.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
							}
							player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_TOTEM_USE,
									SoundCategory.PLAYERS, 1.8f, 1f);
							player.setHealth(20f);
							e.setCanceled(true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onConstruction(EntityConstructing e) {
		if (e.getEntity() instanceof EntitySpider || e.getEntity() instanceof EntityAnimatedSpider) {
			((EntityLivingBase) e.getEntity()).getAttributeMap()
					.registerAttribute((new RangedAttribute((IAttribute) null, "generic.blessed", 0.0D, -100D, 100D))
							.setShouldWatch(true));
		}
	}

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.RightClickBlock e) {
		// For testing, please stop deleting it. You will need it later on dumbass.
//		 if(!e.getEntityPlayer().getHeldItemMainhand().isEmpty() 
//		 ){
//		 System.out.println(e.getEntityPlayer().getHeldItemMainhand().getItem());
//		 System.out.println(e.getWorld().getBlockState(e.getPos()).getBlock());
//		 }
		TileEntity tile = e.getWorld().getTileEntity(e.getPos());
		if (tile != null && tile instanceof TileEntityChest) {
			TileEntityLockableLoot chest = (TileEntityLockableLoot) tile;
			if (chest.getLootTable() != null && chest.getLootTable().equals(LootTableList.CHESTS_DESERT_PYRAMID)
					&& e.getEntityPlayer().getRNG().nextFloat() < 0.02f) {
				e.getPos();
				BlockPos pos = findPos(BlockPos.getAllInBox(e.getPos().add(-1, 0, -1), e.getPos().add(1, 1, 1)),
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

	private static BlockPos findPos(Iterable<BlockPos> blocks, World world) {
		for (BlockPos pos : blocks) {
			if (world.isAirBlock(pos) && world.isAirBlock(pos.add(0, 1, 0))) {
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
	
	private static boolean setCheck(Set<EntityAITaskEntry> set, Class task) {
		for(EntityAITaskEntry entries : set) {
			if(entries.action.getClass() == task) {
				return false;
			}
		}
		return true;
	}

	@SubscribeEvent
	public static void onJoin(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityZombie && !e.getWorld().isRemote) {
			EntityZombie zombie = (EntityZombie) e.getEntity();
			EntityAIMoveToNearestDoctor task = new EntityAIMoveToNearestDoctor(zombie, 1.2D, 16D);
			if (setCheck(zombie.tasks.taskEntries, EntityAIMoveToNearestDoctor.class)) {
				zombie.tasks.addTask(1, task);
			}
			
		}
		if (e.getEntity() instanceof EntityOcelot && !e.getWorld().isRemote) {
			EntityOcelot ocelot = (EntityOcelot) e.getEntity();
			EntityAIFollowPlayerWithPigsheath task = new EntityAIFollowPlayerWithPigsheath(ocelot, 1.1D);
			if (setCheck(ocelot.tasks.taskEntries, EntityAIFollowPlayerWithPigsheath.class)) {
				ocelot.tasks.addTask(3, task);
			}
			for (EntityAITaskEntry entry : ocelot.tasks.taskEntries) {
				if (entry.action instanceof EntityAIAvoidEntity) {
					ocelot.tasks.taskEntries.remove(entry);
					EntityAIAvoidEntity<EntityPlayer> newTask = new EntityAIAvoidEntity<EntityPlayer>(ocelot,
							EntityPlayer.class, new Predicate<EntityPlayer>() {
								@Override
								public boolean apply(EntityPlayer player) {
									if ((player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()
											|| !(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
													.getItem() instanceof ItemPigsheathHelmet))
											&& (player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()
													|| !(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)
															.getItem() instanceof ItemPigsheathTunic))
											&& (player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()
													|| !(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS)
															.getItem() instanceof ItemPigsheathLeggings))
											&& (player.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty()
													|| !(player.getItemStackFromSlot(EntityEquipmentSlot.FEET)
															.getItem() instanceof ItemPigsheathBoots))) {
										return true;
									} else {
										return false;
									}
								};
							}, 16.0F, 0.8D, 1.33D) {
						@Override
						public boolean shouldExecute() {
							if (((EntityTameable) this.entity).isTamed()) {
								return false;
							}
							return super.shouldExecute();
						}
					};
					ocelot.tasks.addTask(4, newTask);
					break;
				}
			}
		}
		if (e.getEntity() instanceof EntityPigZombie && !e.getWorld().isRemote) {
			EntityPigZombie pigman = (EntityPigZombie) e.getEntity();
			for (EntityAITaskEntry entry : pigman.targetTasks.taskEntries) {
				if (entry.action instanceof EntityAIHurtByTarget) {
					pigman.targetTasks.taskEntries.remove(entry);
					EntityAIHurtByTarget task = new EntityAIHurtByTarget(pigman, true) {
						@Override
						protected void setEntityAttackTarget(EntityCreature creatureIn,
								EntityLivingBase entityLivingBaseIn) {
							if (creatureIn instanceof EntityPigZombie && entityLivingBaseIn instanceof EntityPlayer) {
								EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
								if ((player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()
										|| !(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
												.getItem() instanceof ItemPigsheathHelmet))
										&& (player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()
												|| !(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)
														.getItem() instanceof ItemPigsheathTunic))
										&& (player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()
												|| !(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS)
														.getItem() instanceof ItemPigsheathLeggings))
										&& (player.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty()
												|| !(player.getItemStackFromSlot(EntityEquipmentSlot.FEET)
														.getItem() instanceof ItemPigsheathBoots))) {
									Method method = ReflectionHelper.findMethod(pigman.getClass(), "becomeAngryAt",
											"func_70835_c", Entity.class);
									try {
										method.invoke(pigman, player);
										super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
									} catch (IllegalAccessException | IllegalArgumentException
											| InvocationTargetException e) {
										e.printStackTrace();
									}

								}
							}
						}
					};
					pigman.targetTasks.addTask(1, task);
					break;
				}
			}
			pigman.targetTasks.addTask(3, new EntityAITargetHurtBoss(pigman, new Predicate<EntityPlayer>() {

				@Override
				public boolean apply(EntityPlayer input) {
					if ((!input.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() && input
							.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemPigsheathHelmet)
							&& (!input.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()
									&& input.getItemStackFromSlot(EntityEquipmentSlot.CHEST)
											.getItem() instanceof ItemPigsheathTunic)
							&& (!input.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()
									&& input.getItemStackFromSlot(EntityEquipmentSlot.LEGS)
											.getItem() instanceof ItemPigsheathLeggings)
							&& (!input.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty()
									&& input.getItemStackFromSlot(EntityEquipmentSlot.FEET)
											.getItem() instanceof ItemPigsheathBoots)) {
						return true;
					}
					return false;
				}
			}));
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
	public static void onHarvestBlock(BlockEvent.HarvestDropsEvent e) {
		if (e.getState().getBlock().isWood(e.getWorld(), e.getPos())) {
			if (e.getHarvester() != null && e.getHarvester() instanceof EntityPlayerMP) {
				if (e.getWorld().rand.nextInt(MitesConfig.mites.wood.spawnChance) == 0) {
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
	public static void onLootTable(LootTableLoadEvent e) {
		if (e.getName().equals(new ResourceLocation("minecraft", "entities/zombie_pigman"))) {
			e.getTable().removePool("main");
			e.getTable().addPool(
					new LootPool(
							new LootEntry[] { new LootEntryItem(ModItems.pigmanFlesh, 1, 1,
									new LootFunction[] {
											new SetCount(new LootCondition[] {}, new RandomValueRange(-2, 2)),
											new LootingEnchantBonus(new LootCondition[] {}, new RandomValueRange(0, 1),
													0) },
									new LootCondition[] {}, "pigman_flesh") },
							new LootCondition[] {}, new RandomValueRange(1), new RandomValueRange(0), "pigman_flesh"));
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MOD_ID)) {
			ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
		}
	}


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
	}

}
