package mcpecommander.mobultion.entity.entities.zombies;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIDoctorHeal;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIDoctorTarget;
import mcpecommander.mobultion.init.ModItems;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityDoctorZombie extends EntityAnimatedZombie {

	private static final DataParameter<Byte> ACTIVE = EntityDataManager.<Byte>createKey(EntityDoctorZombie.class,
			DataSerializers.BYTE);

	static {
		EntityDoctorZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "zombie", true);
		EntityDoctorZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "zombie", true);
		EntityDoctorZombie.animHandler.addAnim(Reference.MOD_ID, "doctor_throw", "zombie", false);
		EntityDoctorZombie.animHandler.addAnim(Reference.MOD_ID, "doctor_heal", "zombie", false);
		EntityDoctorZombie.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
		EntityDoctorZombie.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}

	public EntityDoctorZombie(World worldIn) {
		super(worldIn);
		this.setSize(0.7F, 2F);
	}
	
	@Override
	public boolean getCanSpawnHere() {
		return this.world.getEntitiesWithinAABB(EntityDoctorZombie.class, this.getEntityBoundingBox().grow(10)).isEmpty() && super.getCanSpawnHere();
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIDoctorHeal(this, 1.0D));
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 3, 1D, 1.4D));
		this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.targetTasks.addTask(1,
				new EntityAIDoctorTarget(this, false, EntityZombie.class, EntityZombieVillager.class,
						EntityKnightZombie.class, EntityWorkerZombie.class, EntityGoroZombie.class));

	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ACTIVE, Byte.valueOf((byte) 0));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
	}

	public boolean isThrowing() {
		return this.dataManager.get(ACTIVE).byteValue() == 1;
	}

	public boolean isHealing() {
		return this.dataManager.get(ACTIVE).byteValue() == 2;
	}

	public void setActive(byte b) {
		if (this.dataManager.get(ACTIVE).byteValue() != b) {
			this.dataManager.set(ACTIVE, Byte.valueOf(b));
			this.dataManager.setDirty(ACTIVE);
		}
	}
	
	
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		super.onInitialSpawn(difficulty, livingdata);
		if (this.isChild()) {
			this.setChild(false);
		}
		if (this.isLeftHanded()) {
			this.setLeftHanded(false);
		}
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.health));
		ItemStack potion = new ItemStack(Items.SPLASH_POTION, 1, 0);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Potion", "minecraft:water");
		potion.setTagCompound(nbt);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, potion);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		return livingdata;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.isWorldRemote()) {
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
		}
		if (this.isWorldRemote()) {
			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& !this.getMoving()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				if (!this.isThrowing() && !this.isHealing()) {
					this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				}
			}
			if (this.isThrowing()
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "doctor_throw", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "doctor_throw", 0, this);
			}

			if (this.isHealing() && !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "doctor_heal", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "doctor_heal", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& this.getMoving() && this.deathTime < 1 && !this.isRiding()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this)
					&& !this.isThrowing() && !this.isHealing()
					&& this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "doctor_throw", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "doctor_heal", this);
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

}
