package mcpecommander.mobultion.entity.entityAI.spidersAI.miniAI;

import mcpecommander.mobultion.entity.entities.spiders.EntityMiniSpider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityAIMiniSpiderAttack extends EntityAIAttackMelee{
	
	public EntityAIMiniSpiderAttack(EntityMiniSpider spider)
    {
        super(spider, 1.0D, true);
    }

    @Override
	protected double getAttackReachSqr(EntityLivingBase attackTarget)
    {
        return 4.0F + attackTarget.width;
    }
}
