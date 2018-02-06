package mcpecommander.mobultion.entity.entities.endermen;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.entity.entities.zombies.EntityAnimatedZombie;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityAnimatedEnderman extends EntityMob implements IAnimated{
	
	private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier ATTACKING_SPEED_BOOST = (new AttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
    private static final DataParameter<Boolean> SCREAMING = EntityDataManager.<Boolean>createKey(EntityAnimatedEnderman.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MOVING = EntityDataManager.<Boolean>createKey(EntityAnimatedEnderman.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK = EntityDataManager.<Optional<IBlockState>>createKey(EntityAnimatedEnderman.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityAnimatedEnderman.class);
    private int lastCreepySound;
    protected int targetChangeTime;
    
	public EntityAnimatedEnderman(World worldIn) {
		super(worldIn);
	}
	
	@Override
	public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        super.setAttackTarget(entitylivingbaseIn);
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (entitylivingbaseIn == null)
        {
            this.targetChangeTime = 0;
            this.dataManager.set(SCREAMING, Boolean.valueOf(false));
            iattributeinstance.removeModifier(ATTACKING_SPEED_BOOST);
        }
        else
        {
            this.targetChangeTime = this.ticksExisted;
            this.dataManager.set(SCREAMING, Boolean.valueOf(true));

            if (!iattributeinstance.hasModifier(ATTACKING_SPEED_BOOST))
            {
                iattributeinstance.applyModifier(ATTACKING_SPEED_BOOST);
            }
        }
    }
	
	public void setScreaming(boolean screaming){
		this.dataManager.set(SCREAMING, Boolean.valueOf(screaming));
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(CARRIED_BLOCK, Optional.absent());
        this.dataManager.register(SCREAMING, Boolean.valueOf(false));
        this.dataManager.register(MOVING, Boolean.valueOf(false));
    }
	
	public boolean getMoving(){
    	return this.dataManager.get(MOVING).booleanValue();
    }

	public void setMoving(boolean isMoving){
		this.dataManager.set(MOVING, Boolean.valueOf(isMoving));
		this.dataManager.setDirty(MOVING);
	}
	
	public boolean isMoving(EntityLiving entity){
    	if(!entity.getMoveHelper().isUpdating()){
    		return Math.abs(entity.moveForward) > 0.01f || Math.abs(entity.moveStrafing) > 0.1f || Math.abs(entity.moveVertical) > 0.1f;
    	}return true;
    }
	
	public void playEndermanSound() {
		if (this.ticksExisted >= this.lastCreepySound + 400) {
			this.lastCreepySound = this.ticksExisted;

			if (!this.isSilent()) {
				this.world.playSound(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ,
						SoundEvents.ENTITY_ENDERMEN_STARE, this.getSoundCategory(), 2.5F, 1.0F, false);
			}
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        IBlockState iblockstate = this.getHeldBlockState();

        if (iblockstate != null)
        {
            compound.setShort("carried", (short)Block.getIdFromBlock(iblockstate.getBlock()));
            compound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
        }
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        IBlockState iblockstate;

        if (compound.hasKey("carried", 8))
        {
            iblockstate = Block.getBlockFromName(compound.getString("carried")).getStateFromMeta(compound.getShort("carriedData") & 65535);
        }
        else
        {
            iblockstate = Block.getBlockById(compound.getShort("carried")).getStateFromMeta(compound.getShort("carriedData") & 65535);
        }

        if (iblockstate == null || iblockstate.getBlock() == null || iblockstate.getMaterial() == Material.AIR)
        {
            iblockstate = null;
        }

        this.setHeldBlockState(iblockstate);
    }
	
	public void setHeldBlockState(@Nullable IBlockState state) {
		this.dataManager.set(CARRIED_BLOCK, Optional.fromNullable(state));
	}

	@Nullable
	public IBlockState getHeldBlockState() {
		return (IBlockState) ((Optional) this.dataManager.get(CARRIED_BLOCK)).orNull();
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (SCREAMING.equals(key) && this.isScreaming() && this.world.isRemote) {
			this.playEndermanSound();
		}

		super.notifyDataManagerChange(key);
	}
	
	public abstract boolean shouldAttackPlayer(EntityPlayer player);


	@Override
    public float getEyeHeight()
    {
        return 2.55F;
    }

	public boolean teleportRandomly()
    {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }
	
	public boolean teleportToEntity(Entity entity)
    {
        Vec3d vec3d = new Vec3d(this.posX - entity.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0F) - entity.posY + (double)entity.getEyeHeight(), this.posZ - entity.posZ);
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
        return this.teleportTo(d1, d2, d3);
    }
	
	public boolean teleportTo(double x, double y, double z)
    {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag)
        {
            this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }
	
	@Override
	protected SoundEvent getAmbientSound()
    {
        return this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }

	@Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_ENDERMEN_HURT;
    }

	@Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ENDERMEN_DEATH;
    }
	
	public boolean isScreaming()
    {
        return ((Boolean)this.dataManager.get(SCREAMING)).booleanValue();
    }
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.getAnimationHandler().animationsUpdate(this);
		if(!isWorldRemote() && this.isScreaming() && ticksExisted % 40 == 0){
			this.playSound(SoundEvents.ENTITY_ENDERMEN_STARE, 2.5F, 1.0F);
		}
	}

	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityAnimatedEnderman.animHandler;
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
		return this.world.isRemote;
	}

}
