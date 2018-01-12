package mcpecommander.mobultion.entity.entities.spiders;

import javax.annotation.Nullable;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAtWither;
import mcpecommander.mobultion.entity.entityAI.spidersAI.EntityAISpiderAttack;
import mcpecommander.mobultion.entity.entityAI.spidersAI.EntityAISpiderTarget;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityWitherSpider extends EntityAnimatedSpider {

	static {
		EntityWitherSpider.animHandler.addAnim(Reference.MOD_ID, "spider_move", "wither_spider", false);
		EntityWitherSpider.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAtWither("Head", "Head1", "Head2"));
	}

	public EntityWitherSpider(World worldIn) {
		super(worldIn);
		this.setSize(1.4f, 1.15f);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(4, new EntityAISpiderAttack(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAISpiderTarget(this, EntityPlayer.class));
		this.targetTasks.addTask(3, new EntityAISpiderTarget(this, EntityIronGolem.class));
	}

	@Override
	public double getMountedYOffset() {
		return (double) (this.height * 0.6F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3D);
	}

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);

		if (this.world.rand.nextInt(200) == 0 && !this.world.isRemote) {
			EntitySkeleton entityskeleton = new EntitySkeleton(this.world);
			entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData) null);
			this.world.spawnEntity(entityskeleton);
			entityskeleton.startRiding(this);
		}

		return livingdata;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = super.attackEntityAsMob(entityIn);

		if (flag) {
			float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();

			if (this.rand.nextFloat() < f * 0.3F) {
				PotionEffect potioneffect = new PotionEffect(MobEffects.WITHER, SpidersConfig.spiders.wither.witheringLength, 0);
				((EntityLivingBase) entityIn).addPotionEffect(potioneffect);
			}
		}

		return flag;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.getHealth() > 20f && this.getHealth() - amount < 20f && super.attackEntityFrom(source, amount)) {
			this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
			return true;
		}
		if (this.getHealth() > 10f && this.getHealth() - amount < 10f && super.attackEntityFrom(source, amount)) {
			this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
			return true;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public float getEyeHeight() {
		return 0.85F;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.isWorldRemote()) {
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this)) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "spider_move", this)
					&& this.isMoving()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "spider_move", 0, this);
			}
//			if(this.getHealth() < 20f && this.ticksExisted % 5 == 0){
//				double yaw  = ((this.rotationYaw + 60)  * Math.PI) / 180;
//				double z = Math.sin(yaw);
//				double x = Math.cos(yaw);
//				this.world.spawnParticle(EnumParticleTypes.DRIP_LAVA, this.posX + (x * 0.3D), this.posY + 0.5f , this.posZ + (z * 0.3D), 0, 0, 0);
//			}
		}
	}
}
