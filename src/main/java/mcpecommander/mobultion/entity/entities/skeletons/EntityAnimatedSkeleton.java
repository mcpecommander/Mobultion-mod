package mcpecommander.mobultion.entity.entities.skeletons;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;
import com.leviathanstudio.craftstudio.common.animation.simpleImpl.AnimatedEntity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityAnimatedSkeleton extends EntityMob implements IRangedAttackMob, IGetEquip, IAnimated
{
	private static final DataParameter<Boolean> MOVING = EntityDataManager.<Boolean>createKey(EntityAnimatedSkeleton.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.<Boolean>createKey(EntityAnimatedSkeleton.class, DataSerializers.BOOLEAN);
    protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityAnimatedSkeleton.class);
    
    public EntityAnimatedSkeleton(World worldIn)
    {
        super(worldIn);
    }

    protected void initEntityAI(){
    	super.initEntityAI();
    }

    protected void applyEntityAttributes(){
    	super.applyEntityAttributes();
    }
    	

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
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
    
    

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SKELETON_STEP , 0.15F, 1.0F);
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    public void updateRidden()
    {
        super.updateRidden();

        if (this.getRidingEntity() instanceof EntityCreature)
        {
            EntityCreature entitycreature = (EntityCreature)this.getRidingEntity();
            this.renderYawOffset = entitycreature.renderYawOffset;
        }
    }
    
    @Override
    protected void onDeathUpdate()
    {
        ++this.deathTime;

        if (this.deathTime == 30)
        {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
            {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }

            this.setDead();

//            for (int k = 0; k < 20; ++k)
//            {
//                double d2 = this.rand.nextGaussian() * 0.02D;
//                double d0 = this.rand.nextGaussian() * 0.02D;
//                double d1 = this.rand.nextGaussian() * 0.02D;
//                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1);
//            }
        }
    }

    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        EntityArrow entityarrow = this.getArrow(distanceFactor);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityarrow);
    }

    /**
     * Distance factor is a float that is between .1f and 1.0f that increases the damage slightly.
     */
    protected EntityArrow getArrow(float distanceFactor)
    {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
        entitytippedarrow.setEnchantmentEffectsFromEntity(this, distanceFactor);
        return entitytippedarrow;
    }

    public double getYOffset()
    {
        return -0.6D;
    }

    @SideOnly(Side.CLIENT)
    public boolean isSwingingArms()
    {
        return ((Boolean)this.dataManager.get(SWINGING_ARMS)).booleanValue();
    }

    public void setSwingingArms(boolean swingingArms)
    {
        this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
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
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.getAnimationHandler().animationsUpdate(this);
    }
    
    @Override
    public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
        return EntityAnimatedSkeleton.animHandler;
    }
}