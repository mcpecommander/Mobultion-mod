package mcpecommander.mobultion.entity.entities.skeletons;

import mcpecommander.mobultion.MobsConfig;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
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
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWitheringSkeleton extends EntityAnimatedSkeleton {

	private boolean flag = false;
	private final EntityAIAttackRangedBow<EntityWitheringSkeleton> aiArrowAttack = new EntityAIAttackRangedBow<EntityWitheringSkeleton>(
			this, 1.0D, 20, 15.0F);
	private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false) {

		public void resetTask() {
			super.resetTask();
			EntityWitheringSkeleton.this.setSwingingArms(false);
		}

		public void startExecuting() {
			super.startExecuting();
			EntityWitheringSkeleton.this.setSwingingArms(true);
		}
	};
	static {
		EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_death", "forest_skeleton", false);
		EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "forest_skeleton", true);
		EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "forest_skeleton", true);
		EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_holding_bow", "forest_skeleton", true);
		EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
		EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
		EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_spawn", "skeleton_death");
	}

	public EntityWitheringSkeleton(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.99F);
		this.setCombatTask();
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
	}

	public void setCombatTask() {
		if (this.world != null && !this.world.isRemote) {
			this.tasks.removeTask(this.aiAttackOnCollide);
			this.tasks.removeTask(this.aiArrowAttack);
			ItemStack itemstack = this.getHeldItemMainhand();

			if (itemstack.getItem() == Items.BOW) {
				int i = 20;

				if (this.world.getDifficulty() != EnumDifficulty.HARD) {
					i = 40;
				}

				this.aiArrowAttack.setAttackCooldown(i);
				this.tasks.addTask(4, this.aiArrowAttack);
			} else {
				this.tasks.addTask(4, this.aiAttackOnCollide);
			}
		}
	}

	@Override
	public double getYOffset() {
		return -0.43D;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	@Override
	public float getEyeHeight() {
		return 1.74F;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(this.getEquip()));
		this.setCombatTask();
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		EntityArrow entityarrow = this.getArrow(distanceFactor);
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entityarrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
		entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 14f);
		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entityarrow);
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
		if (this.isWorldRemote()
				&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_death", this) && !flag) {
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_holding_bow", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this);
			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_death", 0, this);
			flag = true;
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.world.isRemote) {
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
			if (this.isDead) {
				EntitySkeletonRemains grave = new EntitySkeletonRemains(this.world, this);
				grave.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				this.world.spawnEntity(grave);
			}
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
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_holding_bow", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_holding_bow", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& this.getMoving() && this.deathTime < 1 && !this.isRiding()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this)
					&& !this.isSwingingArms()
					&& this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_holding_bow", this);
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
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setCombatTask();
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		super.setItemStackToSlot(slotIn, stack);

		if (!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND) {
			this.setCombatTask();
		}
	}

	@Override
	public Item getEquip() {
		return Items.BOW;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	protected EntityArrow getArrow(float distanceFactor) {
		EntityTippedArrow arrow = new EntityTippedArrow(this.world, this);
		arrow.setEnchantmentEffectsFromEntity(this, distanceFactor);
		arrow.addEffect(new PotionEffect(MobEffects.WITHER, MobsConfig.skeletons.withering.wither, 0));
		return arrow;
	}

	@Override
	public boolean isWorldRemote() {
		return world.isRemote;
	}

}
