package mcpecommander.mobultion.entity.entityAI.spidersAI.miniAI;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIMiniSpiderTarget<T extends EntityLivingBase> extends EntityAITarget{
	
	protected final Class<T> targetClass;
    private final int targetChance;
    /** Instance of EntityAINearestAttackableTargetSorter. */
    protected final EntityAINearestAttackableTarget.Sorter sorter;
    protected final Predicate <? super T > targetEntitySelector;
    protected T targetEntity;

    public EntityAIMiniSpiderTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight)
    {
        this(creature, classTarget, checkSight, false);
    }

    public EntityAIMiniSpiderTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby)
    {
        this(creature, classTarget, 10, checkSight, onlyNearby, (Predicate)null);
    }

    public EntityAIMiniSpiderTarget(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate <? super T > targetSelector)
    {
        super(creature, checkSight, onlyNearby);
        this.targetClass = classTarget;
        this.targetChance = chance;
        this.sorter = new EntityAINearestAttackableTarget.Sorter(creature);
        this.setMutexBits(1);
        this.targetEntitySelector = new Predicate<T>()
        {
            @Override
			public boolean apply(@Nullable T entity)
            {
                if (entity == null)
                {
                    return false;
                }
                else if (targetSelector != null && !targetSelector.apply(entity))
                {
                    return false;
                }
                else
                {
                    return !EntitySelectors.NOT_SPECTATING.apply(entity) ? false : EntityAIMiniSpiderTarget.this.isSuitableTarget(entity, false);
                }
            }
        };
    }

    @Override
	public boolean shouldExecute()
    {
    	if(this.taskOwner.getHealth() > 2f && !this.taskOwner.isRiding()){
	    	if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
	        {
	            return false;
	        }
	        else if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class)
	        {
	            List<T> list = this.taskOwner.world.<T>getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
	
	            if (list.isEmpty())
	            {
	                return false;
	            }
	            else
	            {
	                Collections.sort(list, this.sorter);
	                int i = 0;
	                for(i = 0; i < list.size(); i++){
	                	if(!list.get(i).isBeingRidden()){
	                		this.targetEntity = list.get(i);
	                		return true;
	                	}
	                }
	            }
	        }    
    	} return false;
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
    
    @Override
    public boolean shouldContinueExecuting() {
    	if(this.targetEntity.isBeingRidden()){
    		return false;
    	}
    	return super.shouldContinueExecuting();
    }

    public static class Sorter implements Comparator<Entity>
        {
            private final Entity entity;

            public Sorter(Entity entityIn)
            {
                this.entity = entityIn;
            }

            @Override
			public int compare(Entity p_compare_1_, Entity p_compare_2_)
            {
                double d0 = this.entity.getDistanceSq(p_compare_1_);
                double d1 = this.entity.getDistanceSq(p_compare_2_);

                if (d0 < d1)
                {
                    return -1;
                }
                else
                {
                    return d0 > d1 ? 1 : 0;
                }
            }
        }
}
