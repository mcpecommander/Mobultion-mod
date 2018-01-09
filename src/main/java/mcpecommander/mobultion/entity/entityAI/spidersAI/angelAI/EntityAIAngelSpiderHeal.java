package mcpecommander.mobultion.entity.entityAI.spidersAI.angelAI;

import mcpecommander.mobultion.MobsConfig;
import mcpecommander.mobultion.entity.entities.spiders.EntityAngelSpider;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAIAngelSpiderHeal extends EntityAIBase{
	
	World world;
	private EntityAngelSpider attacker;
	private int healCoolDown;
	
	public EntityAIAngelSpiderHeal(EntityAngelSpider entity){
		this.attacker = entity;
		this.world = entity.world;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if(this.attacker == null || this.attacker.isDead){
			return false;
		}else{
			return this.attacker.getAttackTarget()!= null && this.attacker.getHealth() > 4f && this.attacker.getAttackTarget().getHealth() < this.attacker.getAttackTarget().getMaxHealth();
		}
	}
	
	@Override
	public void startExecuting() {
		if(this.attacker != null && !this.attacker.isDead){
			if(this.attacker.getAttackTarget() != null && !this.attacker.getAttackTarget().isDead){
				this.attacker.getAttackTarget().getEntityData().setBoolean("is", true);
				this.healCoolDown = 100;
			}
		}
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
		this.healCoolDown = Math.max(this.healCoolDown - 1, 0);
		if(this.attacker.getAttackTarget() != null && !this.attacker.getAttackTarget().isDead){
			this.attacker.getLookHelper().setLookPositionWithEntity(this.attacker.getAttackTarget(), 30f, 30f);
			if(this.healCoolDown < 10){
				this.attacker.getAttackTarget().heal(MobsConfig.spiders.angel.healAmount);
				if(this.attacker.isServerWorld()){
					WorldServer temp = (WorldServer) this.attacker.getEntityWorld();
					temp.spawnParticle(EnumParticleTypes.HEART, true, this.attacker.getAttackTarget().posX, this.attacker.getAttackTarget().posY + this.attacker.getAttackTarget().getEyeHeight(), this.attacker.getAttackTarget().posZ, 2, 0.0f, 0.0f, 0.0f, 0.01f);
				}
				this.healCoolDown = 0;
			}
		}
	}
	

}
