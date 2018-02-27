package mcpecommander.mobultion.entity.entities.spiders;

import javax.annotation.Nullable;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.entityAI.spidersAI.EntityAISpiderAttack;
import mcpecommander.mobultion.entity.entityAI.spidersAI.EntityAISpiderTarget;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMagmaSpider extends EntityAnimatedSpider{
	
	static{
		EntityMagmaSpider.animHandler.addAnim(Reference.MOD_ID, "spider_move", "magma_spider", false);
		EntityMagmaSpider.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
	}
	
	public EntityMagmaSpider(World worldIn) {
		super(worldIn);
		this.setSize(1.4f, 0.9f);
		this.isImmuneToFire = true;
		this.setPathPriority(PathNodeType.WATER, -1.0F);
	}

	@Override
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAISpiderAttack(this));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAISpiderTarget(this, EntityPlayer.class));
        this.targetTasks.addTask(3, new EntityAISpiderTarget(this, EntityIronGolem.class));
    }
    
    @Override
    public double getMountedYOffset()
    {
        return (double)(this.height * 0.6F);
    }
    
    @Override
    protected ResourceLocation getLootTable() {
    	return Reference.LootTables.ENTITYMAGMASPIDER;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(22.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }


    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

		if (this.world.rand.nextInt(SpidersConfig.spiders.magma.jockeyChance) == 0 && !this.world.isRemote) {
			EntitySkeleton entityskeleton = new EntitySkeleton(this.world);
			entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData) null);
			this.world.spawnEntity(entityskeleton);
			entityskeleton.startRiding(this);
		}

        return livingdata;
    }
    
    @Override
    public void onLivingUpdate() {
    	super.onLivingUpdate();
    	if(this.isWorldRemote()){
    		if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this)){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", 0, this);
    		}

    		if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "spider_move", this) && this.isMoving()){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "spider_move", 0, this);
    		}
    	}
    }

    @Override
    public float getEyeHeight()
    {
        return 0.65F;
    }
    
    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = super.attackEntityAsMob(entityIn);

        if (flag)
        {
            float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();

            if (this.rand.nextFloat() < f * 0.3F)
            {
                entityIn.setFire(SpidersConfig.spiders.magma.burningLength + (int)f);
            }
        }

        return flag;
    }
    
    @Override
    protected void updateAITasks(){
    	if (this.isWet() && SpidersConfig.spiders.magma.waterDamage)
        {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }
    	super.updateAITasks();
    }

}
