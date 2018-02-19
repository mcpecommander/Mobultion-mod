package mcpecommander.mobultion.entity.entities.skeletons;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.mobConfigs.SkeletonsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCorruptedSkeleton extends EntityAnimatedSkeleton {
	private boolean flag = false;

	static {
		EntityCorruptedSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_death", "forest_skeleton", false);
		EntityCorruptedSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "forest_skeleton", true);
		EntityCorruptedSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "forest_skeleton", true);
		EntityCorruptedSkeleton.animHandler.addAnim(Reference.MOD_ID, "knight_slash", "forest_skeleton", true);
		EntityCorruptedSkeleton.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
		EntityCorruptedSkeleton.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}

	public EntityCorruptedSkeleton(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.99F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		//this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.2d, false){
			@Override
			public void updateTask() {
				super.updateTask();
				if(this.attacker.getDistanceSq(this.attacker.getAttackTarget()) < 3d){
					((EntityCorruptedSkeleton) this.attacker).setSwingingArms(true);
				}else{
					((EntityCorruptedSkeleton) this.attacker).setSwingingArms(false);
				}
			}
			
			@Override
			public void resetTask() {
				super.resetTask();
				((EntityCorruptedSkeleton) this.attacker).setSwingingArms(false);
			}
		});
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
	}

	@Override
	public double getYOffset() {
		return -0.43D;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15f);
	}

	@Override
	public float getEyeHeight() {
		return 1.74F;
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return Reference.LootTables.ENTITYCORRUPTEDSKELETON;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.corruptedBone));
		this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0f);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
		if (this.isWorldRemote()
				&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_death", this) && !flag) {
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "knight_slash", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this);
			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_death", 0, this);
			flag = true;
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.world.isDaytime() && !this.world.isRemote && !this.isChild()) {
			float f = this.getBrightness();

			if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world
					.canSeeSky(new BlockPos(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ))) {
				boolean flag = true;
				ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

				if (!itemstack.isEmpty()) {
					if (itemstack.isItemStackDamageable()) {
						itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

						if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
							this.renderBrokenItemStack(itemstack);
							this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
						}
					}

					flag = false;
				}

				if (flag) {
					this.setFire(8);
				}
			}
		}
		if (!this.world.isRemote) {
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
		}
		if (this.isWorldRemote()) {
			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& !this.getMoving()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				if (!this.isSwingingArms()) {
					this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				}
			}
			if (this.isSwingingArms()
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "knight_slash", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "knight_slash", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& this.getMoving() && this.deathTime < 1 && !this.isRiding()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this)
					&& !this.isSwingingArms()
					&& this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "knight_slash", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk_hands", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this) && this.deathTime < 1) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", this);
			}
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "riding", this) && this.isRiding()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "riding", this);
			}
		}

	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = super.attackEntityAsMob(entityIn);

		if (flag) {
			float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();

			if (this.rand.nextFloat() < f * 0.6F) {
				PotionEffect potioneffect = new PotionEffect(MobEffects.MINING_FATIGUE,
						SkeletonsConfig.skeletons.corrupted.miningFatigue, 0);
				((EntityLivingBase) entityIn).addPotionEffect(potioneffect);
				PotionEffect potioneffect2 = new PotionEffect(MobEffects.SLOWNESS,
						SkeletonsConfig.skeletons.corrupted.slowness, 0);
				((EntityLivingBase) entityIn).addPotionEffect(potioneffect2);
			}
		}

		return flag;
	}

	@Override
	protected EntityArrow getArrow(float distanceFactor) {
		return null;
	}

}
