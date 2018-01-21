package mcpecommander.mobultion.entity.entityAI.zombiesAI;

import mcpecommander.mobultion.entity.entities.zombies.EntityRavenousZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemFood;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIItemEat extends EntityAIBase{
	
	private EntityLiving entity;
	private double speed;
	
	public EntityAIItemEat(EntityLiving entity, double speed) {
		this.entity = entity;
		this.speed = speed;
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity != null && !this.entity.isDead){
			if(this.entity.getAttackTarget() == null && ((EntityRavenousZombie) this.entity).getItemTarget() != null){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting(){
		if(shouldExecute()){
			if(this.entity.world.getEntityByID(((EntityRavenousZombie) this.entity).getItemTarget().getEntityId()) == null){
				((EntityRavenousZombie) this.entity).setItem(null);
				return false;
			}
			return true;
		}
			
		return false;
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		this.entity.getNavigator().clearPath();
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		EntityItem item = ((EntityRavenousZombie) this.entity).getItemTarget();
		this.entity.getLookHelper().setLookPositionWithEntity(item, 30, 30);
		if(this.entity.getDistanceSq(item) > 1d){
			if(this.entity.getNavigator().getPath() != this.entity.getNavigator().getPathToEntityLiving(item)){
				this.entity.getNavigator().tryMoveToEntityLiving(item, speed);
			}
		}else{
			this.entity.attackEntityAsMob(item);
		}
	}
	
	

}
