package mcpecommander.mobultion.entity.entities.endermen;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAtEnderman;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.endermenAI.EntityAIEndermanAttackMelee;
import mcpecommander.mobultion.entity.entityAI.endermenAI.EntityAIFindPlayer;
import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityIceEnderman extends EntityAnimatedEnderman {

	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityIceEnderman.class);
	//A dataParameter that auto-syncs to the client (not backwards). This is used to define if this should play the biting animation.
	private static final DataParameter<Boolean> BITING = EntityDataManager.<Boolean>createKey(EntityIceEnderman.class,
			DataSerializers.BOOLEAN);

	static {
		EntityIceEnderman.animHandler.addAnim(Reference.MOD_ID, "scream", "enderman", true);
		EntityIceEnderman.animHandler.addAnim(Reference.MOD_ID, "enderman_bite", "enderman", false);
		EntityIceEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "enderman", true);
		EntityIceEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "enderman", true);
		EntityIceEnderman.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAtEnderman("Head", "Jaw"));
		EntityIceEnderman.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}

	public EntityIceEnderman(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 2.9F);
		this.stepHeight = 1.0F;
	}
	
	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityIceEnderman.animHandler;
	}
	
	//Stopped 
	@Override
	public boolean handleWaterMovement() {
		return false;
	}
	
	//Not tested enough. Do not use.
	@Override
	protected float getWaterSlowDown() {
		return -0.546f;
	}
	
	//Should stop water from pushing the entity. Combine with stopping the handleWanterMovement.
	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIEndermanAttackMelee(this, 1.0D, false));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D, 100));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIFindPlayer(this));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));

	}
	
	//Grouped all the loot tables for easier rewriting later on if needed. Also thanks vanilla
	@Override
	protected ResourceLocation getLootTable() {
		return Reference.LootTables.ENTITYICEENDERMAN;
	}

	//Every data parameter must be registered in this method if you do not want the game to crash.
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(BITING, Boolean.valueOf(false));
	}

	//A setter that sets the parameter and set it dirty (as in minecraft need to sync this to the client). SetDirty is not required but is used for faster sync rates.
	public void setBiting(boolean biting) {
		this.dataManager.set(BITING, Boolean.valueOf(biting));
		this.dataManager.setDirty(BITING);
	}

	public boolean getBiting() {
		return this.dataManager.get(BITING);
	}

	//A good method to run server sided shit. Mostly used for damaging(or healing in this case) the entity from water in my mod.
	@Override
	protected void updateAITasks() {
		if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600) {
			float f = this.getBrightness();

			if (f > 0.5F && this.world.canSeeSky(new BlockPos(this))
					&& this.rand.nextFloat() * 100.0F < (f - 0.4F) * 2.0F) {
				this.setAttackTarget((EntityLivingBase) null);
				this.teleportRandomly();
			}
		}

		if (this.isWet() && this.getRNG().nextInt(50) == 0) {
			this.heal(1f);
		}

		super.updateAITasks();
	}

	//Summon the lingering effect cloud with the freeze potion effect.
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.isWorldRemote()) {
			EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY,
					this.posZ);
			entityareaeffectcloud.setOwner(this);
			entityareaeffectcloud.setRadius(3.0F);
			entityareaeffectcloud.setRadiusOnUse(-0.5F);
			entityareaeffectcloud.setWaitTime(10);
			entityareaeffectcloud
					.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
			entityareaeffectcloud.addEffect(new PotionEffect(ModPotions.potionFreeze, 100, 0));
			// entityareaeffectcloud.setPotion(PotionType.getPotionTypeForName("mobultion:freeze_potion"));
			this.world.spawnEntity(entityareaeffectcloud);
		}
	}

	//Normal enderman behavior to arrows and other entities with an added punishment of freezing the player if attacked with an empty hand.
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.equals(DamageSource.ON_FIRE)) {
			return super.attackEntityFrom(source, amount * 2);
		}
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else if (source instanceof EntityDamageSourceIndirect) {
			if (source.getTrueSource() instanceof EntityLivingBase) {
				this.setAttackTarget((EntityLivingBase) source.getTrueSource());
			}
			return false;
		} else {
			boolean flag = super.attackEntityFrom(source, amount);

			if (source.isUnblockable() && this.rand.nextInt(30) <= 5) {
				this.teleportRandomly();
			}

			if (flag && source.getTrueSource() instanceof EntityPlayerMP) {
				EntityPlayerMP entity = (EntityPlayerMP) source.getTrueSource();
				if (entity.getHeldItemMainhand().isEmpty()) {
					PotionEffect effect = new PotionEffect(ModPotions.potionFreeze, 40, 0, false, false);
					entity.addPotionEffect(effect);
				}
			}

			return flag;
		}
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20d);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
	}

	//The magic copy-pasta from vanilla that determines whether the player is looking at the enderman. (modified to lower the chance of becoming angry)
	@Override
	public boolean shouldAttackPlayer(EntityPlayer player) {
		Vec3d vec3d = player.getLook(1.0F).normalize();
		Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY
				+ this.getEyeHeight() - (player.posY + player.getEyeHeight()),
				this.posZ - player.posZ);
		double d0 = vec3d1.lengthVector();
		vec3d1 = vec3d1.normalize();
		double d1 = vec3d.dotProduct(vec3d1);
		return d1 > 1.0D - 0.015D / d0 ? player.canEntityBeSeen(this) : false;
	}

	//Do not, I repeat do not try to understand what is happening here. It is related to the animation sequence and when to play them but it is highly unoptimized.
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.isWorldRemote()) {
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
		}

		if (isWorldRemote()) {
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "enderman_bite", this)
					&& this.getBiting()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "scream", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "enderman_bite", 0, this);
			}
			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "scream", this) && !this.isScreaming()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "scream", this);
			}

			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& !this.getMoving()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				if (!this.isScreaming()) {
					this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				}
			}
			if (this.isScreaming() && !this.getBiting()
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "scream", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "scream", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& this.getMoving() && this.deathTime < 1 && !this.isRiding()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this)
					&& !this.isScreaming()
					&& this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "scream", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk_hands", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this)
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "scream", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", this);
			}
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "riding", this) && this.isRiding()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "riding", this);
			}

			//Since the particle is vanilla this won't crash on class loading on dedicated servers. Otherwise must be in its own client sided method. 
			double yaw = ((this.rotationYawHead + 90) * Math.PI) / 180;
			double z = Math.sin(yaw);
			double x = Math.cos(yaw);
			double d0 = (9237234 >> 16 & 255) / 255.0D;
			double d1 = (9237234 >> 8 & 255) / 255.0D;
			double d2 = (9237234 >> 0 & 255) / 255.0D;
			if (this.isScreaming() && ticksExisted % 5 == 0) {
				this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (x * 0.35D),
						this.posY + this.getEyeHeight(), this.posZ + (z * 0.35D), d0, d1, d2);
			}
		}
	}


}
