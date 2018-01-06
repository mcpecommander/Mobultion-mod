package mcpecommander.luggagemod.entity.entityAI.skeletonsAI;

import mcpecommander.luggagemod.entity.entities.skeletons.EntitySkeletonRemains;
import mcpecommander.luggagemod.init.ModItems;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIShamanSkeletonHeal extends EntityAIBase{
	
	private EntityLiving entity;
	private int cooldown;
	private double speed;
	World world;
	private boolean finished = false;
	
	public EntityAIShamanSkeletonHeal(EntityLiving entity, double speed) {
		this.entity = entity;
		this.world = entity.world;
		this.speed = speed;
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity == null || this.entity.isDead){
			return false;
		}else if(this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead){
			if(this.entity.getHealth() > 4f && !this.entity.isHandActive()){
				return isWandInHand();
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(this.entity == null || this.entity.isDead){
			return false;
		}else if(this.entity.getAttackTarget() instanceof EntitySkeletonRemains){
			return isWandInHand() && !finished;
		}
		else if(this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead){
			return this.entity.getAttackTarget().getHealth() < this.entity.getAttackTarget().getMaxHealth() && isWandInHand() && !finished;
		}
		return false;
	}
	
	private boolean isWandInHand(){
		 return !this.entity.getHeldItemMainhand().isEmpty() && this.entity.getHeldItemMainhand().getItem() == ModItems.healingWand;
	}
	
	@Override
	public void startExecuting()
    {
        this.cooldown = 50;
        finished = false;
    }
	
	@Override
	public void resetTask() {
		((IRangedAttackMob)this.entity).setSwingingArms(false);
		this.entity.setAttackTarget(null);
		this.entity.resetActiveHand();
	}
	
	@Override
	public void updateTask() {
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
		if (entitylivingbase != null)
        {
			double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
			if(d0 <= 16D){
				this.entity.getNavigator().clearPathEntity();
			}else{
				this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speed);
			}
			if (this.entity.isHandActive())
            {
				((IRangedAttackMob)this.entity).setSwingingArms(true);
				this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30F, 30F);
				int i = this.entity.getItemInUseMaxCount();
				if (i >= 50) {
					if(entitylivingbase instanceof EntitySkeletonRemains){
						this.entity.resetActiveHand();
						entitylivingbase.ticksExisted += 500;
						this.cooldown = 300;
						finished = true;
						return;
					}else{
						this.entity.resetActiveHand();
						entitylivingbase.heal(8f);
						this.cooldown = 100;
						finished = true;
						return;
					}
				}
            }
            else if (--this.cooldown <= 0)
            {
                this.entity.setActiveHand(EnumHand.MAIN_HAND);
            }
        }
	}

}
