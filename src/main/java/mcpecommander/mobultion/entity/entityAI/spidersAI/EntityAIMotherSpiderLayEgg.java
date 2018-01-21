package mcpecommander.mobultion.entity.entityAI.spidersAI;

import mcpecommander.mobultion.entity.entities.spiders.EntityMotherSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntitySpiderEgg;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class EntityAIMotherSpiderLayEgg extends EntityAIBase {

	private int averageTime;
	private int dAverageTime;
	private int coolDown = 0;
	private EntityMotherSpider entity;
	World world;

	public EntityAIMotherSpiderLayEgg(EntityMotherSpider entity, int pregnancyTime) {
		this.entity = entity;
		this.world = entity.world;
		this.averageTime = pregnancyTime;
		this.dAverageTime = pregnancyTime;
		this.setMutexBits(4);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity != null && !this.entity.isDead) {
			if (this.entity.getAttackTarget() != null) {
				return this.coolDown-- <= 0;
			} else {
				return this.entity.getHealth() > 4f && this.entity.getRNG().nextInt(500) == 0 && this.coolDown-- <= 0
						&& this.world.isChunkGeneratedAt((int) (this.entity.posX) >> 4, (int) (this.entity.posZ) >> 4);
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity != null && !this.entity.isDead) {
			return this.entity.isPregnant();
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.entity.setPregnant(true);
	}

	@Override
	public void updateTask() {
		if (this.averageTime-- <= 0) {
			if (!this.world.isRemote) {
				EntitySpiderEgg egg = this.entity.getAttackingEntity() != null
						? new EntitySpiderEgg(this.world, this.entity.getAttackingEntity().getName())
						: new EntitySpiderEgg(this.world);
				egg.setLocationAndAngles(this.entity.posX, this.entity.posY, this.entity.posZ, this.entity.rotationYaw,
						0.0F);
				this.world.spawnEntity(egg);
				resetTask();
			}
		}
	}

	@Override
	public void resetTask() {
		this.entity.setPregnant(false);
		this.averageTime = this.dAverageTime;
		this.coolDown = 100;
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

}
