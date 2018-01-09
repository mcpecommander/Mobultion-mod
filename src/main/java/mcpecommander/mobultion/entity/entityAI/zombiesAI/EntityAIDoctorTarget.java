package mcpecommander.mobultion.entity.entityAI.zombiesAI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityAIDoctorTarget extends EntityAITarget{

	private Class[] targets;
	private EntityLivingBase mainTarget;
	World world;
    protected final EntityAIDoctorTarget.Sorter sorter;
	
	public EntityAIDoctorTarget(EntityCreature entity, boolean checkSight, Class... targets) {
		super(entity, checkSight);
		this.targets = targets;
		this.world = entity.world;
		this.sorter = new EntityAIDoctorTarget.Sorter(entity);
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.taskOwner != null && !this.taskOwner.isDead){
			List<Entity> mainList = new ArrayList<>();
			for(int i = 0; i < targets.length; i++){
				List<Entity> list = this.world.getEntitiesWithinAABB(targets[i], this.getTargetableArea(this.getTargetDistance()));
				mainList.addAll(list);
			}
			if(mainList.isEmpty()){
				return false;
			}else{
				Collections.sort(mainList, this.sorter);
				for(int i = 0; i< mainList.size(); i++){
					if(mainList.get(i) != null && !mainList.get(i).isDead){
						if(mainList.get(i).isBurning()){
							this.mainTarget = (EntityLivingBase) mainList.get(i);
						 	return true;
						}else if(((EntityLivingBase) mainList.get(i)).getHealth() < ((EntityLivingBase) mainList.get(i)).getMaxHealth()){
							this.mainTarget = (EntityLivingBase) mainList.get(i);
						 	return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.mainTarget);
        super.startExecuting();
    }
	
	protected AxisAlignedBB getTargetableArea(double targetDistance)
    {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }
	
	public static class Sorter implements Comparator<Entity>
    {
        private final Entity entity;

        public Sorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

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
