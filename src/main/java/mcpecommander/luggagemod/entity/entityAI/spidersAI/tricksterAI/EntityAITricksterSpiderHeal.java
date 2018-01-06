package mcpecommander.luggagemod.entity.entityAI.spidersAI.tricksterAI;

import mcpecommander.luggagemod.entity.entities.spiders.EntityAnimatedSpider;
import mcpecommander.luggagemod.entity.entities.spiders.EntityTricksterSpider;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class EntityAITricksterSpiderHeal extends EntityAIBase{
	
	World world;
	private EntityAnimatedSpider attacker;
	private int healCoolDown;
	
	public EntityAITricksterSpiderHeal(EntityAnimatedSpider entity){
		this.attacker = entity;
		this.world = entity.world;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if(this.attacker == null || this.attacker.isDead){
			return false;
		}else{
			return this.attacker.getHealth() < this.attacker.getMaxHealth() && ((EntityTricksterSpider) this.attacker).isHealing();
		}
	}
	
	@Override
	public void startExecuting() {
		this.healCoolDown = 100;
		this.attacker.setAttackTarget(null);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(this.attacker == null || this.attacker.isDead){
			return false;
		}else{
			return this.healCoolDown != 0;
		}
	}
	
	@Override
	public void updateTask() {
		Math.max(this.healCoolDown - 1, 0);
		if(this.healCoolDown < 10){
			this.attacker.heal(4f);
			((EntityTricksterSpider) this.attacker).setMode((byte) 0);
		}
	}

}
