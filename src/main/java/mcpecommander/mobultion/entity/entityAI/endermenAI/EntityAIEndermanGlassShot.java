package mcpecommander.mobultion.entity.entityAI.endermenAI;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityGlassEnderman;
import mcpecommander.mobultion.entity.entities.endermen.EntityGlassShot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIEndermanGlassShot extends EntityAIBase {

	private EntityGlassEnderman entity;
	private int cooldown;
	private double speed;
	private boolean flag = false;
	World world;

	public EntityAIEndermanGlassShot(EntityGlassEnderman entity, double speed) {
		this.entity = entity;
		this.world = entity.world;
		this.speed = speed;
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity == null || this.entity.isDead) {
			return false;
		} else if (this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead) {
			return true;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.cooldown = 50;
		flag = false;
	}

	@Override
	public void resetTask() {
		this.entity.setAttackTarget(null);
	}

	@Override
	public void updateTask() {
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
		if (entitylivingbase != null) {
			this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30f, 30f);
			double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY,
					entitylivingbase.posZ);
			if (d0 <= 48D) {
				this.entity.getNavigator().clearPath();
			}else{
				this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speed);
			}
			if(cooldown < 20) {
				this.entity.setShoting(true);
			}
			if (--cooldown < 0) {
				if (d0 <= 100D ) {
					Vec3d vec = new Vec3d(this.entity.posX - entitylivingbase.posX, (this.entity.posY + this.entity.getEyeHeight()) - (entitylivingbase.posY), this.entity.posZ - entitylivingbase.posZ).normalize();
					EntityGlassShot glass = new EntityGlassShot(world, entity, -vec.x, -vec.y + 0.1, -vec.z);
					glass.posY += this.entity.getEyeHeight() - 1;
					world.spawnEntity(glass);
					cooldown = 30;
					this.entity.setShoting(false);
					
				}
			} 
		}
	}

}
