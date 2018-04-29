package mcpecommander.mobultion.entity.entityAI.zombiesAI;

import mcpecommander.mobultion.entity.entities.zombies.EntityDoctorZombie;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.mobConfigs.ZombiesConfig;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;

public class EntityAIDoctorHeal extends EntityAIBase{
	
	private EntityLiving entity;
	private int cooldown;
	private double speed;
	
	public EntityAIDoctorHeal(EntityLiving entity, double speed) {
		this.entity = entity;
		this.speed = speed;
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity == null || this.entity.isDead){
			return false;
		}else if(this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead){
			if(this.entity.getHealth() > 4f ){
				return isWaterInHand() && this.isHealthInHand();
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(this.entity == null || this.entity.isDead){
			return false;
		}
		else if(this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead){
			return this.entity.getAttackTarget().getHealth() < this.entity.getAttackTarget().getMaxHealth() && isWaterInHand() && this.isHealthInHand();
		}
		return false;
	}
	
	private boolean isWaterInHand(){
		 return !this.entity.getHeldItemMainhand().isEmpty() && this.entity.getHeldItemMainhand().getItem() == Items.SPLASH_POTION;
	}
	
	private boolean isHealthInHand(){
		 return !this.entity.getHeldItemOffhand().isEmpty() && this.entity.getHeldItemOffhand().getItem() == ModItems.health;
	}
	
	@Override
	public void startExecuting()
    {
        this.cooldown = 50;
    }
	
	@Override
	public void resetTask() {
		this.entity.setAttackTarget(null);
		((EntityDoctorZombie) this.entity).setActive((byte) 0);
	}
	
	@Override
	public void updateTask() {
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
		if (entitylivingbase != null)
        {
			boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);
			this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30F, 30F);
			double dis = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
			if(dis <= 16D){
				this.entity.getNavigator().clearPath();
			}else{
				this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speed);
			}
			
			if(this.cooldown <= 3 && entitylivingbase.isBurning() && flag){
				((EntityDoctorZombie) this.entity).setActive((byte) 1);
			}
			
			if(!entitylivingbase.isBurning() && dis <= 1D){
				((EntityDoctorZombie) this.entity).setActive((byte) 2);
			}
			
			if (--this.cooldown <= 0)
            {
            	if(entitylivingbase.isBurning() && ZombiesConfig.zombies.doctor.shouldExtinguish){
            		if(!this.entity.world.isRemote && dis <= (15D*15D) && flag){
						EntityPotion potion = new EntityPotion(this.entity.world, this.entity, this.entity.getHeldItemMainhand());
						double d0 = entitylivingbase.posX - this.entity.posX;
					    double d1 = entitylivingbase.getEntityBoundingBox().minY + entitylivingbase.height / 3.0F - potion.posY;
					    double d2 = entitylivingbase.posZ - this.entity.posZ;
					    double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
					    potion.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 0f);
						this.entity.world.spawnEntity(potion);
						this.resetTask();
            		}
				}else{
					this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speed);
					if(dis <= 1D){
						entitylivingbase.heal((float) ZombiesConfig.zombies.doctor.healAmount);
						this.resetTask();
					}
				}
            }
        }
	}

}
