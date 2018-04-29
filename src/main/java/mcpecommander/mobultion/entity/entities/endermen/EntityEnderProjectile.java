package mcpecommander.mobultion.entity.entities.endermen;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEnderProjectile extends EntityThrowable{

	private static final DataParameter<Byte> TYPE = EntityDataManager.<Byte>createKey(EntityGlassEnderman.class, DataSerializers.BYTE);
    private EntityLivingBase pearlThrower;
    private float attackDamage;
    private float chance;
    /**
     * 0: blaze, 1: flake, 2: glass
     */
    private float thornsDamage = 0.0f;
    private float breakChance = 0.0f;
    
	public EntityEnderProjectile(World worldIn) {
		super(worldIn);
	}
	
	public EntityEnderProjectile(World worldIn, EntityLivingBase throwerIn, float attackDamage, float chanceToSpawnMite)
    {
        super(worldIn, throwerIn);
        this.pearlThrower = throwerIn;
        this.attackDamage = attackDamage;
        this.chance = chanceToSpawnMite;
    }

    @SideOnly(Side.CLIENT)
    public EntityEnderProjectile(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
    
    @Override
    protected void entityInit() {
    	super.entityInit();
    	this.dataManager.register(TYPE, (byte)-1);
    }

	public float getThornsDamage() {
		return thornsDamage;
	}

	public void setThornsDamage(float thornsDamage) {
		this.thornsDamage = thornsDamage;
	}

	public byte getType() {
		return this.dataManager.get(TYPE).byteValue();
	}

	public void setType(byte type) {
		this.dataManager.set(TYPE, type);
		this.dataManager.setDirty(TYPE);
	}

	public void setBreakChance(float breakChance) {
		this.breakChance = breakChance;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if(!world.isRemote && this.rand.nextFloat() < this.breakChance) {
			this.setDead();
			return;
		}
		EntityLivingBase entitylivingbase = this.getThrower();

        if (result.entityHit != null)
        {
            if (result.entityHit == this.pearlThrower)
            {
                return;
            }

            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), this.getThornsDamage());
        }

        if (result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockpos = result.getBlockPos();
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityEndGateway)
            {
                TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;

                if (entitylivingbase != null)
                {
                    if (entitylivingbase instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.ENTER_BLOCK.trigger((EntityPlayerMP)entitylivingbase, this.world.getBlockState(blockpos));
                    }

                    tileentityendgateway.teleportEntity(entitylivingbase);
                    this.setDead();
                    return;
                }

                tileentityendgateway.teleportEntity(this);
                return;
            }
        }

        for (int i = 0; i < 32; ++i)
        {
            this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }

        if (!this.world.isRemote)
        {
            if (entitylivingbase instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;

                if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.world == this.world && !entityplayermp.isPlayerSleeping())
                {

                    if (this.rand.nextFloat() < this.chance && this.world.getGameRules().getBoolean("doMobSpawning"))
                    {
                        EntityEndermite entityendermite = new EntityEndermite(this.world);
                        entityendermite.setSpawnedByPlayer(true);
                        entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
                        this.world.spawnEntity(entityendermite);
                    }

                    if (entitylivingbase.isRiding())
                    {
                        entitylivingbase.dismountRidingEntity();
                    }

                    entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    entitylivingbase.fallDistance = 0.0F;
                    entitylivingbase.attackEntityFrom(DamageSource.FALL, this.attackDamage);

                }
            }
            else if (entitylivingbase != null)
            {
                entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                entitylivingbase.fallDistance = 0.0F;
            }

            this.setDead();
        }
		
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setByte("type", this.getType());
		compound.setFloat("thorns", this.getThornsDamage());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if(compound.hasKey("type", 1)) {
			this.setType(compound.getByte("type"));
		}
		if(compound.hasKey("thorns", 5)) {
			this.setThornsDamage(compound.getFloat("thorns"));
		}
	}
	
	@Override
	public void onUpdate()
    {
        EntityLivingBase entitylivingbase = this.getThrower();

        if (entitylivingbase != null && entitylivingbase instanceof EntityPlayer && !entitylivingbase.isEntityAlive())
        {
            this.setDead();
        }
        else
        {
            super.onUpdate();
        }
    }

	@Override
    @Nullable
    public Entity changeDimension(int dimensionIn)
    {
        if (this.thrower.dimension != dimensionIn)
        {
            this.thrower = null;
        }

        return super.changeDimension(dimensionIn);
    }

}
