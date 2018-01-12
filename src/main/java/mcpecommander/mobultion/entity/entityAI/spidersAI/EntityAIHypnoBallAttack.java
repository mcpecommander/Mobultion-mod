package mcpecommander.mobultion.entity.entityAI.spidersAI;

import mcpecommander.mobultion.entity.entities.spiders.EntityHypnoBall;
import mcpecommander.mobultion.entity.entities.spiders.EntityHypnoSpider;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class EntityAIHypnoBallAttack extends EntityAIBase{
    private final EntityHypnoSpider hypno;
    private int attackTime;

    public EntityAIHypnoBallAttack(EntityHypnoSpider hypno)
    {
        this.hypno = hypno;
        this.setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.hypno.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive();
    }

    public void startExecuting()
    {
        this.attackTime = 80;
    }
    
    public void resetTask()
    {
        super.resetTask();
    }


    public void updateTask()
    {
        --this.attackTime;
        EntityLivingBase entitylivingbase = this.hypno.getAttackTarget();
        double d0 = this.hypno.getDistanceSq(entitylivingbase);

        if (d0 < 9.0D)
        {
            if (this.attackTime <= 0)
            {
                this.attackTime = 80;
                this.hypno.attackEntityAsMob(entitylivingbase);
            }

            this.hypno.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
        }
        else if (d0 < this.getFollowDistance() * this.getFollowDistance())
        {
            double d1 = entitylivingbase.posX - this.hypno.posX;
            double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (this.hypno.posY + (double)(this.hypno.height / 2.0F));
            double d3 = entitylivingbase.posZ - this.hypno.posZ;
            //--this.attackTime;

            if (this.attackTime <= 0)
            {
                 this.hypno.world.playEvent((EntityPlayer)null, 1018, new BlockPos((int)this.hypno.posX, (int)this.hypno.posY, (int)this.hypno.posZ), 0);
                 EntityHypnoBall hypnoBall = new EntityHypnoBall(this.hypno.world, this.hypno, d1 , d2, d3 );
                 hypnoBall.posY = this.hypno.posY + (double)(this.hypno.height / 2.0F) + 0.5D;
                 this.hypno.world.spawnEntity(hypnoBall);
                 this.attackTime = SpidersConfig.spiders.hypno.fireDelay;
            } 
            
            this.hypno.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
        }
        else
        {
            this.hypno.getNavigator().clearPath();
            this.hypno.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
        }

        super.updateTask();
    }

    private double getFollowDistance()
    {
        IAttributeInstance iattributeinstance = this.hypno.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
}
