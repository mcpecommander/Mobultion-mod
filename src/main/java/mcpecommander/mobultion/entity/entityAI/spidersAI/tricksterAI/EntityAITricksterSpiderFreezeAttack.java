package mcpecommander.mobultion.entity.entityAI.spidersAI.tricksterAI;

import mcpecommander.mobultion.entity.entities.spiders.EntityTricksterSpider;
import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

public class EntityAITricksterSpiderFreezeAttack extends EntityAIBase{
    private final EntityTricksterSpider trickster;
    private int attackTime;

    public EntityAITricksterSpiderFreezeAttack(EntityTricksterSpider trickster)
    {
        this.trickster = trickster;
        this.setMutexBits(3);
    }

    @Override
	public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.trickster.getAttackTarget();
        if(entitylivingbase != null && entitylivingbase.isEntityAlive()){
        	return false;
        }else{
        	return this.trickster.isFreezing();
        }
        
    }

    @Override
	public void startExecuting()
    {
        this.attackTime = 80;
    }
    
    @Override
	public void resetTask()
    {
        super.resetTask();
    }


    @Override
	public void updateTask()
    {
        --this.attackTime;
        EntityLivingBase entitylivingbase = this.trickster.getAttackTarget();
        if(entitylivingbase != null){
	        double d0 = this.trickster.getDistanceSq(entitylivingbase);
	
	        if (d0 < this.getFollowDistance() * this.getFollowDistance())
	        {
	            --this.attackTime;
	            if (this.attackTime <= 0)
	            {
	                 this.trickster.world.playEvent((EntityPlayer)null, 1018, new BlockPos((int)this.trickster.posX, (int)this.trickster.posY, (int)this.trickster.posZ), 0);
	                 PotionEffect freeze = new PotionEffect(ModPotions.potionFreeze, 100, 0, false, false);
	                 this.trickster.getAttackTarget().addPotionEffect(freeze);
	                 this.trickster.setMode((byte) 0);
	                 this.attackTime = 100;
	            } 
	            
	            this.trickster.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
	        }
	        else
	        {
	            this.trickster.getNavigator().clearPath();
	            this.trickster.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
	        }
        }
        super.updateTask();
    }

    private double getFollowDistance()
    {
        IAttributeInstance iattributeinstance = this.trickster.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
}