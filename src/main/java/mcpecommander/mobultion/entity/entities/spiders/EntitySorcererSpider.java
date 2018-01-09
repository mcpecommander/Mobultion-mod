package mcpecommander.mobultion.entity.entities.spiders;

import mcpecommander.mobultion.MobsConfig;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.entityAI.spidersAI.sorcererAI.EntityAISorcererSpiderSpellAttack;
import mcpecommander.mobultion.entity.entityAI.spidersAI.sorcererAI.EntityAISorcererSpiderTarget;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntitySorcererSpider extends EntityAnimatedSpider{
	private static final DataParameter<Boolean> SPELLCASTING = EntityDataManager.<Boolean>createKey(EntitySorcererSpider.class, DataSerializers.BOOLEAN);
	static {
    	EntitySorcererSpider.animHandler.addAnim(Reference.MOD_ID, "spider_move", "sorcerer_spider", false);
    	EntitySorcererSpider.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
    	EntitySorcererSpider.animHandler.addAnim(Reference.MOD_ID, "sorcerer_cast", "sorcerer_spider", false);
	}
	
	public EntitySorcererSpider(World worldIn) {
		super(worldIn);
		this.setSize(1.4f, 0.9f);
	}

	@Override
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAISorcererSpiderSpellAttack(this, MobsConfig.spiders.sorcerer.castingTime));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAISorcererSpiderTarget(this, EntityPlayer.class, true));
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
    } 
    
	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(SPELLCASTING, Boolean.valueOf(false));
    }

	@Override
    public float getEyeHeight()
    {
        return 0.65F;
    }
    
    public boolean isSpellcasting(){
        return this.dataManager.get(SPELLCASTING).booleanValue();
    }
    
    public void setSpellcasting(boolean isSpellcasting){
    	this.dataManager.set(SPELLCASTING, Boolean.valueOf(isSpellcasting));
    	this.dataManager.setDirty(SPELLCASTING);
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
    		
    		if(this.isSpellcasting() && !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "sorcerer_cast", this)){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "sorcerer_cast", 0, this);
    		}
    		if(!this.isSpellcasting() && this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "sorcerer_cast", this)){
    			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "sorcerer_cast", this);
    		}
    	}    	
    }
}
