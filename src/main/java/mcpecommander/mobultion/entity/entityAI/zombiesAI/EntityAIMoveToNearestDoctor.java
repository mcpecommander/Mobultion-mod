package mcpecommander.mobultion.entity.entityAI.zombiesAI;

import java.util.Comparator;
import java.util.List;

import mcpecommander.mobultion.entity.entities.zombies.EntityDoctorZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;

public class EntityAIMoveToNearestDoctor extends EntityAIBase{

	private EntityMob entity;
	private double speed;
	private EntityDoctorZombie target;
	private double distance;
    protected final EntityAIMoveToNearestDoctor.Sorter sorter;
	
	public EntityAIMoveToNearestDoctor(EntityMob entity, double speed, double distance) {
		this.entity = entity;
		this.speed = speed;
		this.distance = distance;
		this.sorter = new EntityAIMoveToNearestDoctor.Sorter(entity);
	}
	
	
	@Override
	public boolean shouldExecute() {
		if(this.entity != null && !this.entity.isDead){
			if(this.entity.getNavigator().noPath()){
				return this.entity.getHealth() < 6f || this.entity.isBurning();
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(this.entity != null && !this.entity.isDead){
			return this.entity.getHealth() < 6f || this.entity.isBurning();
		}
		return false;
	}
	
	@Override
	public void updateTask() {
		if(!this.entity.isDead){
			if(this.target == null){
				List<EntityDoctorZombie> list = this.entity.world.getEntitiesWithinAABB(EntityDoctorZombie.class, this.entity.getEntityBoundingBox().grow(this.distance, 4, this.distance));
				if(!list.isEmpty()){
					list.sort(sorter);
					this.target = list.get(0);
				}
			}else{
				if(!this.target.isDead){
					if(this.entity.getDistanceSq(target) > 16D){
						this.entity.getNavigator().tryMoveToEntityLiving(target, speed);
					}else{
						this.entity.getNavigator().clearPath();
					}
				}
			}
		}
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
