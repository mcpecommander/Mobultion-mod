package mcpecommander.mobultion.entity.entityAI.spidersAI.angelAI;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mcpecommander.mobultion.entity.entities.spiders.EntityAnimatedSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIAngelSpiderTarget <T extends EntityLivingBase> extends EntityAITarget
{
    private final int targetChance;
    protected final EntityAINearestAttackableTarget.Sorter sorter;
    protected EntityLivingBase targetEntity;

    public EntityAIAngelSpiderTarget(EntityCreature creature)
    {
        super(creature, true, false);
        this.targetChance = 10;
        this.sorter = new EntityAINearestAttackableTarget.Sorter(creature);
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0 )
        {
            return false;
        }
        else if (this.taskOwner.getAttackTarget() != null)
        {
        	return false;
        }
        else
        {
            List<EntityLivingBase> list = this.taskOwner.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getTargetableArea(this.getTargetDistance()), null);

            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                Collections.sort(list, this.sorter);
                for(int i = 0; i < list.size(); i++){
                	if((list.get(i) instanceof EntityAnimatedSpider || list.get(i).getClass() == EntitySpider.class || list.get(i).getClass() == EntityCaveSpider.class) && list.get(i) != this.taskOwner){
                		if(list.get(i).getHealth() < list.get(i).getMaxHealth()){
                			this.targetEntity = list.get(i);
                			return true;
                		}
                	}
                }
                return false;
            }
        } 
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance)
    {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    /*
     * Sort the entity list according to their health.
     */
    public static class Sorter implements Comparator<Entity>
        {
            private final Entity entity;

            public Sorter(Entity entityIn)
            {
                this.entity = entityIn;
            }

            @Override
			public int compare(Entity entity1, Entity entity2)
            {
                double h0 = ((EntityLivingBase) entity1).getMaxHealth() - ((EntityLivingBase) entity1).getHealth();
                double h1 = ((EntityLivingBase) entity2).getMaxHealth() - ((EntityLivingBase) entity2).getHealth();

                if (h1 < h0)
                {
                    return -1;
                }
                else
                {
                    return h1 > h0 ? 1 : 0;
                }
            }
        }
}