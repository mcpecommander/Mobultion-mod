package mcpecommander.mobultion.entity.entityAI.skeletonsAI;

import mcpecommander.mobultion.entity.entities.skeletons.EntityVampireSkeleton;
import mcpecommander.mobultion.init.ModSounds;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIBatMorph extends EntityAIBase {

	private EntityVampireSkeleton vampire;
	private int cooldown;
	private int activeTime;

	public EntityAIBatMorph(EntityVampireSkeleton vampire) {
		this.vampire = vampire;
	}

	@Override
	public boolean shouldExecute() {
		if (this.vampire != null && !this.vampire.isDead && this.vampire.getHealth() < 5f) {
			if (cooldown-- <= 0) {
				if (this.vampire.getRNG().nextInt(20) == 1) {
					return true;
				} else {
					this.cooldown = 5;
				}
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.vampire.setMorphing((byte) 1);
		this.vampire.playSound(ModSounds.bat_morph, 1f,
				(this.vampire.getRNG().nextFloat() - this.vampire.getRNG().nextFloat()) * 0.2F + 1.0F);
		this.activeTime = 20;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return true;
	}

	@Override
	public void updateTask() {
		if (this.activeTime-- <= 0) {
			this.vampire.setMorphing((byte) 2);
		}
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

}
