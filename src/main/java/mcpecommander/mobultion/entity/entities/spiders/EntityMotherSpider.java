package mcpecommander.mobultion.entity.entities.spiders;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.entityAI.spidersAI.EntityAIMotherSpiderLayEgg;
import mcpecommander.mobultion.entity.entityAI.spidersAI.EntityAISpiderAttack;
import mcpecommander.mobultion.entity.entityAI.spidersAI.EntityAISpiderTarget;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMotherSpider extends EntityAnimatedSpider{
	private static final DataParameter<Boolean> PREGNANT = EntityDataManager.<Boolean>createKey(EntityMotherSpider.class, DataSerializers.BOOLEAN);
	static {
    	EntityMotherSpider.animHandler.addAnim(Reference.MOD_ID, "spider_move", "mother_spider", false);
    	EntityMotherSpider.animHandler.addAnim(Reference.MOD_ID, "mother_pregnant", "mother_spider", false);
    	EntityMotherSpider.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
	}
	
	public EntityMotherSpider(World worldIn) {
		super(worldIn);
		this.setSize(1.4f, 0.9f);
	}

	@Override
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAISpiderAttack(this));
        this.tasks.addTask(4, new EntityAIMotherSpiderLayEgg(this, SpidersConfig.spiders.mother.pregnancyTime));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAISpiderTarget(this, EntityPlayer.class));
    }

    @Override
    public double getMountedYOffset()
    {
        return (double)(this.height * 0.6F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
    } 

    @Override
    public float getEyeHeight()
    {
        return 0.65F;
    }
    
    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(PREGNANT, Boolean.valueOf(false));
    }
    
    public boolean isPregnant(){
    	return this.dataManager.get(PREGNANT).booleanValue();
    }
    
    public void setPregnant(boolean isPregnant){
    	this.dataManager.set(PREGNANT, Boolean.valueOf(isPregnant));
    	this.dataManager.setDirty(PREGNANT);
    }
    
    @Override
    public void onLivingUpdate() {
    	super.onLivingUpdate();
    	EntityAILeapAtTarget task = new EntityAILeapAtTarget(this, 0.4F);
    	if(this.isPregnant()){
    		this.tasks.removeTask(task);
    		this.motionX = motionX/1.3;
    		this.motionY = motionY/1.3;
    		this.motionZ = motionZ/1.3;
    	}else if (!this.tasks.taskEntries.contains(task)){
    		this.tasks.addTask(3, task);
    	}
    	
    	if(this.isWorldRemote()){
			if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this)){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", 0, this);
    		}

    		if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "spider_move", this) && this.isMoving()){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "spider_move", 0, this);
    		}
    		
    		if(this.isPregnant()){
    			if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "mother_pregnant", this)){
    				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "mother_pregnant", 0, this);
    			}
    		}else{
    			if(this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "mother_pregnant", this)){
    				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "mother_pregnant", this);
    			}
			}
		}
    }
    
    @Override
    public void onDeath(DamageSource cause) {
    	if(!this.isWorldRemote()){
    		if(cause.getTrueSource() instanceof EntityPlayerMP){
    			EntitySpiderEgg egg = new EntitySpiderEgg(this.world, cause.getTrueSource().getName());
    			egg.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
    			this.world.spawnEntity(egg);
    		}else if (cause.getTrueSource() instanceof EntityCreeper) {
    			EntitySpiderEgg egg = new EntitySpiderEgg(this.world, 4);
    			egg.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
    			this.world.spawnEntity(egg);
    		}
    	}
    	super.onDeath(cause);
    }
    
}


