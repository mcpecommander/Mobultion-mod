package mcpecommander.mobultion.entity.entities.endermen;

import com.google.common.base.Optional;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAtEnderman;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.endermenAI.EntityAIEndermanGlassShot;
import mcpecommander.mobultion.entity.entityAI.endermenAI.EntityAIFindPlayer;
import mcpecommander.mobultion.entity.entityAI.endermenAI.EntityAIGardenerMoveToFlower;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityGlassEnderman extends EntityAnimatedEnderman {
	
	private boolean flag = false;
	private static final DataParameter<Boolean> SHOTING = EntityDataManager.<Boolean>createKey(EntityGlassEnderman.class, DataSerializers.BOOLEAN);

	static {
		EntityGlassEnderman.animHandler.addAnim(Reference.MOD_ID, "glass_rotate", "glass_enderman", true);
		EntityGlassEnderman.animHandler.addAnim(Reference.MOD_ID, "glass_shot", "glass_enderman", false);
		EntityGlassEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "glass_enderman", true);
		EntityGlassEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "glass_enderman", true);
		EntityGlassEnderman.animHandler.addAnim(Reference.MOD_ID, "lookatenderman",
				new AnimationLookAtEnderman("Head", "Jaw"));
		EntityGlassEnderman.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
		EntityGlassEnderman.animHandler.addAnim(Reference.MOD_ID, "shatter", "glass_enderman", false);
	}

	public EntityGlassEnderman(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 2.9F);
		this.stepHeight = 1.0F;
	}

	@Override
	public void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIEndermanGlassShot(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D, 100));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIFindPlayer(this));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));

	}
	
	@Override
	protected void onDeathUpdate() {
		++this.deathTime;
		this.extinguish();
        if (this.deathTime == 30)
        {
            if (!this.world.isRemote )
            {
            	if((this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
	                int i = this.getExperiencePoints(this.attackingPlayer);
	                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
	                while (i > 0)
	                {
	                    int j = EntityXPOrb.getXPSplit(i);
	                    i -= j;
	                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
	                }
            	}
            	for (int i = 0; i < 360; i += 20) {
        			double x = Math.sin(i);
        			double z = Math.cos(i);
        			EntityGlassShot shot = new EntityGlassShot(world, this, x , this.getRNG().nextGaussian() / 10d, z, false);
        			shot.posX += x*1.2d;
        			shot.posZ += z*1.2d;
        			shot.posY += this.getRNG().nextDouble() - 0.5d;
        			this.world.spawnEntity(shot);
        			
            	}
            }

            this.setDead();
            
        }
		
		if (this.isWorldRemote()
				&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "shatter", this) && !flag) {
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "glass_rotate", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "glass_shot", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookatenderman", this);
			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "shatter", 0, this);
			flag = true;
		}
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(SHOTING, Boolean.valueOf(false));
    }
	
	public void setShoting(boolean shoting) {
		this.dataManager.set(SHOTING, shoting);
		this.dataManager.setDirty(SHOTING);
	}
	
	public boolean getShoting() {
		return this.dataManager.get(SHOTING).booleanValue();
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.isWorldRemote()) {
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
		}
		if (isWorldRemote() && this.deathTime < 1) {
			
			if(this.getShoting() ) {
				if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "glass_shot", this)) {
					if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this))
						this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
					if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "glass_rotate", this))
						this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "glass_rotate", this);
	
					this.getAnimationHandler().startAnimation(Reference.MOD_ID, "glass_shot", 0, this);
				}
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "glass_rotate", this)
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "glass_shot", this)) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "glass_rotate", this);
			}

			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& !this.getMoving()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& this.getMoving() && this.deathTime < 1 && !this.isRiding()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this)
					&& this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "glass_shot", this)) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk_hands", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookatenderman", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookatenderman", this);
			}
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "riding", this) && this.isRiding()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "riding", this);
			}
		}
	}

	@Override
	public boolean shouldAttackPlayer(EntityPlayer player) {
		Vec3d vec3d = player.getLook(1.0F).normalize();
		Vec3d vec3d1 = new Vec3d(this.posX - player.posX,
				this.getEntityBoundingBox().minY + this.getEyeHeight() - (player.posY + player.getEyeHeight()),
				this.posZ - player.posZ);
		double d0 = vec3d1.lengthVector();
		vec3d1 = vec3d1.normalize();
		double d1 = vec3d.dotProduct(vec3d1);
		return d1 > 1.0D - 0.05D / d0 ? player.canEntityBeSeen(this) : false;
	}

}
