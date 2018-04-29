package mcpecommander.mobultion.entity.entityAI.spidersAI;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.monster.EntityMob;

public class EntityAISpiderAttack extends EntityAIAttackMelee{
	
    public EntityAISpiderAttack(EntityMob spider)
    {
        super(spider, 1.0D, true);
    }

    @Override
	public boolean shouldContinueExecuting()
    {
        float f = this.attacker.getBrightness();

        if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0)
        {
            this.attacker.setAttackTarget((EntityLivingBase)null);
            return false;
        }
        else
        {
            return super.shouldContinueExecuting();
        }
    }
    

    @Override
	protected double getAttackReachSqr(EntityLivingBase attackTarget)
    {
        return 4.0F + attackTarget.width;
    }
}