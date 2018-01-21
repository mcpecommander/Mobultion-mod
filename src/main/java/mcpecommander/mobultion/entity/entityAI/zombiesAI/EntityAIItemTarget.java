package mcpecommander.mobultion.entity.entityAI.zombiesAI;

import mcpecommander.mobultion.entity.entities.zombies.EntityRavenousZombie;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemFood;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIItemTarget extends EntityAIBase{

	private EntityRavenousZombie entity;
	
	public EntityAIItemTarget(EntityRavenousZombie entity) {
		this.entity = entity;
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.entity != null && !this.entity.isDead){
			if(this.entity.getItemTarget() == null && this.entity.getAttackTarget() == null){
				for(EntityItem item : this.entity.world.getEntitiesWithinAABB(EntityItem.class, this.getTargetableArea(10D))){
					if(item.getItem().getItem() instanceof ItemFood && !item.isDead && item.onGround && this.entity.canEntityBeSeen(item)){
						this.entity.setItem(item);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	protected AxisAlignedBB getTargetableArea(double targetDistance)
    {
        return this.entity.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

}
