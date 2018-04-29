package mcpecommander.mobultion.entity.entities.endermen;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAtEnderman;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.endermenAI.EntityAIFindPlayer;
import mcpecommander.mobultion.init.ModSounds;
import mcpecommander.mobultion.mobConfigs.EndermenConfig;
import mcpecommander.mobultion.particle.ColoredLavaParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMagmaEnderman extends EntityAnimatedEnderman {

	static {
		EntityMagmaEnderman.animHandler.addAnim(Reference.MOD_ID, "scream", "enderman", true);
		EntityMagmaEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "enderman", true);
		EntityMagmaEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "enderman", true);
		EntityMagmaEnderman.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAtEnderman("Head", "Jaw"));
		EntityMagmaEnderman.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}

	public EntityMagmaEnderman(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 2.9F);
		this.stepHeight = 1.0F;
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.isImmuneToFire = true;
	}

	@Override
	public void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIFindPlayer(this));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));

	}

	@Override
	protected void updateAITasks() {
		if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600) {
			float f = this.getBrightness();

			if (f > 0.5F && this.world.canSeeSky(new BlockPos(this))
					&& this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
				this.setAttackTarget((EntityLivingBase) null);
				this.teleportRandomly();
			}
		}

		if (this.isWet()) {
			this.attackEntityFrom(DamageSource.DROWN, 1.0F);
		}

		super.updateAITasks();
	}

	@Override
	public boolean teleportRandomly() {
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * EndermenConfig.endermen.magma.teleportDistance;
		double d1 = this.posY + (this.rand.nextInt((int) EndermenConfig.endermen.magma.teleportDistance)
				- (int) (EndermenConfig.endermen.magma.teleportDistance / 2));
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * EndermenConfig.endermen.magma.teleportDistance;
		return this.teleportTo(d0, d1, d2);
	}

	@Override
	public boolean shouldAttackPlayer(EntityPlayer player) {
		Vec3d vec3d = player.getLook(1.0F).normalize();
		Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY
				+ this.getEyeHeight() - (player.posY + player.getEyeHeight()),
				this.posZ - player.posZ);
		double d0 = vec3d1.lengthVector();
		vec3d1 = vec3d1.normalize();
		double d1 = vec3d.dotProduct(vec3d1);
		return d1 > 1.0D - 0.05D / d0 ? player.canEntityBeSeen(this) : false;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = super.attackEntityAsMob(entityIn);
		if (flag && EndermenConfig.endermen.magma.shouldIgnite) {
			entityIn.setFire(1 + this.getRNG()
					.nextInt(3 + ((int) this.world.getDifficultyForLocation(getPosition()).getAdditionalDifficulty())));
		}
		return flag;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else if (source instanceof EntityDamageSourceIndirect) {
			for (int i = 0; i < 64; ++i) {
				if (this.teleportRandomly()) {
					return true;
				}
			}
			if (source.getTrueSource() instanceof EntityLivingBase) {
				this.setAttackTarget((EntityLivingBase) source.getTrueSource());
			}
			return false;
		} else {
			boolean flag = super.attackEntityFrom(source, amount);

			if (source.isUnblockable() && this.rand.nextInt(10) != 0) {
				this.teleportRandomly();
			}

			if (flag && source.getTrueSource() instanceof EntityPlayerMP) {
				EntityPlayerMP entity = (EntityPlayerMP) source.getTrueSource();
				if (entity.getHeldItemMainhand().isEmpty() && this.getRNG().nextInt(10) == 1) {
					entity.setFire(this.getRNG().nextInt(7));
				}
			}

			return flag;
		}
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return Reference.LootTables.ENTITYMAGMAENDERMAN;
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EndermenConfig.endermen.magma.health);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.isWorldRemote()) {
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
		}
		if (isWorldRemote()) {
			//System.out.println(this.limbSwing + " " + this.limbSwingAmount + " " + Math.cos(this.limbSwing * 0.6662F));
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
			if (this.isScreaming() && !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "scream", this)
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
			performEffect();
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void performEffect(){
		double yaw = ((this.rotationYawHead + 90) * Math.PI) / 180;
		double z = Math.sin(yaw);
		double x = Math.cos(yaw);
		double d0 = (16712965 >> 16 & 255) / 255.0D;
		double d1 = (16712965 >> 8 & 255) / 255.0D;
		double d2 = (16712965 >> 0 & 255) / 255.0D;
		if (this.isScreaming() && ticksExisted % 5 == 0) {
			this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (x * 0.35D),
					this.posY + this.getEyeHeight(), this.posZ + (z * 0.35D), d0, d1, d2);
		}

		if (ticksExisted % 20 == 0 && this.getRNG().nextInt(10) == 0) {
			this.world.playSound(posX, posY, posZ, ModSounds.spit, SoundCategory.HOSTILE, getSoundVolume() * 2f,
					getSoundPitch(), false);
			for (int i = 0; i < 6; i++) {
				ColoredLavaParticle particle = new ColoredLavaParticle(this.world,
						this.posX + (this.getRNG().nextFloat() * 0.2f) + (x * 0.25D),
						this.posY + this.getEyeHeight(),
						this.posZ + (this.getRNG().nextFloat() * 0.2f) + (z * 0.25D), (x * 0.07D), -0.11D,
						(z * 0.07D));
				Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			}
		}
	}

}
