package mcpecommander.mobultion.entity.entities.spiders;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.entityAI.spidersAI.angelAI.EntityAIAngelSpiderHeal;
import mcpecommander.mobultion.entity.entityAI.spidersAI.angelAI.EntityAIAngelSpiderTarget;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityAngelSpider extends EntityAnimatedSpider{

	static{
		EntityAngelSpider.animHandler.addAnim(Reference.MOD_ID, "spider_move", "angel_spider", false);
		EntityAngelSpider.animHandler.addAnim(Reference.MOD_ID, "wings_flap", "angel_spider", false);
		EntityAngelSpider.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
	}

	public EntityAngelSpider(World worldIn) {
		super(worldIn);
		this.setSize(1.4f, 1f);
	}
	
	protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityPlayer.class, 3.0F, 1.0D, 1.2D));
        this.tasks.addTask(4, new EntityAIAngelSpiderHeal(this));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIAngelSpiderTarget(this));
    }
    
    public double getMountedYOffset()
    {
        return (double)(this.height * 0.6F);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    }

    public float getEyeHeight()
    {
        return 0.65F;
    }
    
    @Override
    public void onLivingUpdate() {
    	super.onLivingUpdate();
    	if(!this.isWorldRemote()){
    		this.getEntityData().setBoolean("HI", true);
    		NBTTagCompound nbt = new NBTTagCompound();
    		nbt.setBoolean("HI", true);
    		this.writeToNBTAtomically(nbt);
    		//this.world.sendPacketToServer(packetIn);
    	}
    	if(this.isWorldRemote()){
    		if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this)){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", 0, this);
    		}

    		if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "spider_move", this) && this.isMoving()){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "spider_move", 0, this);
    		}
    		if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "wings_flap", this)){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "wings_flap", 0, this);
    		}
    	}
    }

}
