package mcpecommander.luggagemod.entity.entityAI.spidersAI.miniAI;

import mcpecommander.luggagemod.entity.entities.spiders.EntityMiniSpider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityAIMiniSpiderAttack extends EntityAIAttackMelee{
	
	public EntityAIMiniSpiderAttack(EntityMiniSpider spider)
    {
        super(spider, 1.0D, true);
    }

    protected double getAttackReachSqr(EntityLivingBase attackTarget)
    {
        return (double)(4.0F + attackTarget.width);
    }
}
