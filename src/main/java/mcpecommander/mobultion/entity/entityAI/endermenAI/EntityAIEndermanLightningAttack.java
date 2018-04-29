package mcpecommander.mobultion.entity.entityAI.endermenAI;

import mcpecommander.mobultion.entity.entities.endermen.EntityWanderingEnderman;
import mcpecommander.mobultion.init.ModItems;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIEndermanLightningAttack extends EntityAIBase {

	private EntityLiving entity;
	private int cooldown;
	private double speed;
	World world;
	private boolean finished = false;

	public EntityAIEndermanLightningAttack(EntityLiving entity, double speed) {
		this.entity = entity;
		this.world = entity.world;
		this.speed = speed;
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity == null || this.entity.isDead) {
			return false;
		} else if (this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead) {
			if (this.entity.getHealth() > 4f && !this.entity.isHandActive()) {
				return isWandInHand();
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity == null || this.entity.isDead) {
			return false;
		} else if (this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead) {
			return isWandInHand() && !finished;
		}
		return false;
	}

	private boolean isWandInHand() {
		return !this.entity.getHeldItemMainhand().isEmpty()
				&& this.entity.getHeldItemMainhand().getItem() == ModItems.thunderWand;
	}

	@Override
	public void startExecuting() {
		this.cooldown = 50;
		finished = false;
	}

	@Override
	public void resetTask() {
		this.entity.setAttackTarget(null);
		((EntityWanderingEnderman) this.entity).setCasting(false);
	}

	@Override
	public void updateTask() {
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
		if (entitylivingbase != null) {
			double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY,
					entitylivingbase.posZ);
			if (d0 <= 48D && d0 > 16D) {
				this.entity.getNavigator().clearPath();
			} else if(d0 < 16D){
				Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom((EntityCreature) this.entity, 14, 4, this.entity.getLook(1.0f));
				if(vec3d != null) this.entity.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, speed);
			}else{
				this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speed);
			}
			if (((EntityWanderingEnderman) this.entity).getCasting()) {
				if (d0 <= 48D) {
					EntityLightningBolt bolt = new EntityLightningBolt(world, entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, false);
					world.addWeatherEffect(bolt);
					this.cooldown = 300;
					finished = true;
					((EntityWanderingEnderman) this.entity).setCasting(false);
					return;
				}
			} else if (--this.cooldown <= 0) {
				((EntityWanderingEnderman) this.entity).setCasting(true);
			}
		}
	}

}
