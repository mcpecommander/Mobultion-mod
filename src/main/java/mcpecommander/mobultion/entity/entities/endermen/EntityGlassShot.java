package mcpecommander.mobultion.entity.entities.endermen;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityGlassShot extends EntityFireball implements IAnimated {

	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityGlassShot.class);

	private int ticksInAir;
	private boolean kill = true;

	static {
		EntityGlassShot.animHandler.addAnim(Reference.MOD_ID, "shot_rotate", "glass_shot", true);
	}

	public EntityGlassShot(World worldIn) {
		super(worldIn);
		this.setSize(.5F, .5F);
	}

	public EntityGlassShot(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn);
		this.shootingEntity = shooter;
		this.setSize(.5F, .5F);
		this.setLocationAndAngles(shooter.posX, shooter.posY + 1, shooter.posZ, shooter.rotationYaw,
				shooter.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		double d0 = (double) MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d0 * 0.1D;
		this.accelerationY = accelY / d0 * 0.1D;
		this.accelerationZ = accelZ / d0 * 0.1D;
	}
	
	public EntityGlassShot(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ, boolean killOnDeathOfOwner) {
		this(worldIn, shooter, accelX, accelY, accelZ);
		this.kill = killOnDeathOfOwner;
	}

	//We do not call super.onUpdate() because we are changing the fireball method and Entity#onUpdate() does not have anything important to call.
	@Override
	public void onUpdate() {
		super.onEntityUpdate();
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		this.setPosition(this.posX, this.posY, this.posZ);
		if (this.world.isBlockLoaded(new BlockPos(this)) && !isWorldRemote()) {
			if (this.shootingEntity != null && (this.shootingEntity.isDead && kill)) {
				this.setDead();
			}
			++this.ticksInAir;

			RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 50,
					this.shootingEntity);

			if (raytraceresult != null
					&& !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
				this.onImpact(raytraceresult);
			}

			
			ProjectileHelper.rotateTowardsMovement(this, 0.2F);
			float f = this.getMotionFactor();
			if (this.isInWater()) {
				f = 0.8f;
			}

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= (double) f;
			this.motionY *= (double) f;
			this.motionZ *= (double) f;

			this.markVelocityChanged();
			if(!kill && ticksExisted > 25) {
				this.setDead();
			}else if(kill && ticksExisted > 100){
				this.setDead();
			}

		}

		this.animHandler.animationsUpdate(this);
		if (isWorldRemote()) {
			
			this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D,
					20);
			if (this.isInWater()) {
				for (int i = 0; i < 4; ++i) {
					float f1 = 0.25F;
					this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D,
							this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX,
							this.motionY, this.motionZ);
				}

			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "shot_rotate", this)) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "shot_rotate", 0, this);
			}
		}
	}

	@Override
	protected boolean isFireballFiery() {
		return false;
	}

	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityGlassShot.animHandler;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public double getX() {
		return this.posX;
	}

	@Override
	public double getY() {
		return this.posY;
	}

	@Override
	public double getZ() {
		return this.posZ;
	}

	@Override
	public boolean isWorldRemote() {
		return world.isRemote;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.3f, 1.1f);
		if (result.typeOfHit == Type.ENTITY) {
			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), 1.5f);

		}
		this.setDead();
	}

}
