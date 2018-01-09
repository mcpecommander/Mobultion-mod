package mcpecommander.mobultion.entity.entityAI.spidersAI.tricksterAI;

import mcpecommander.mobultion.entity.entities.spiders.EntityTricksterSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.math.BlockPos;

public class EntityAITricksterSpiderTeleportAttack extends EntityAIAttackMelee{
	int teleportDelay;

	public EntityAITricksterSpiderTeleportAttack(EntityTricksterSpider entity) {
		super(entity, 1.0D, true);
		this.teleportDelay = 0;
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.attacker.getAttackTarget() != null && !this.attacker.getAttackTarget().isDead){
			if(((EntityTricksterSpider) this.attacker).isTeleporting()){
				return super.shouldExecute();
			}
		}
		return false;
	}
	
	protected double getAttackReachSqr(EntityLivingBase attackTarget)
    {
        return (double)(4.0F + attackTarget.width);
    }
	
    protected void teleportToEntity(Entity target)
    {
    	double targetPosX = target.posX;
    	double targetPosY = target.posY;
    	double targetPosZ = target.posZ;
    	double posX = this.attacker.posX;
    	double posZ = this.attacker.posZ;
    	double finalPosX;
    	double finalPosZ;
    	if(targetPosX - posX > 0){
    		finalPosX = targetPosX + 2;
    	}else{
    		finalPosX = targetPosX - 2;
    	}
    	if(targetPosZ - posZ > 0){
    		finalPosZ = targetPosZ + 2;
    	}else{
    		finalPosZ = targetPosZ - 2;
    	}
    	if(this.attacker.world.isAirBlock(new BlockPos(finalPosX, targetPosY, finalPosZ))){
    		this.attacker.setPositionAndUpdate(finalPosX, targetPosY, finalPosZ);
    	}       
    }
    
    @Override
    public void startExecuting() {
    	super.startExecuting();
    	if(teleportDelay == 0){
    		teleportDelay = 100;
    	}
    }
    
    @Override
    public void updateTask() {
    	super.updateTask();
    	this.teleportDelay = Math.max(teleportDelay - 1, 0);
    	if(this.attacker.getAttackTarget() != null || !this.attacker.getAttackTarget().isDead){
    		if(this.teleportDelay == 1){
    			this.teleportToEntity(this.attacker.getAttackTarget());
    			((EntityTricksterSpider) this.attacker).setMode((byte) 2);
    			this.teleportDelay = 0;
    		}
    	}
    }

}
