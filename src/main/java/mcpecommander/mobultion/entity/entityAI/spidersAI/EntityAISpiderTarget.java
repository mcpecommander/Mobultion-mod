package mcpecommander.mobultion.entity.entityAI.spidersAI;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;

public class EntityAISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>{
	
    public EntityAISpiderTarget(EntityMob spider, Class<T> classTarget)
    {
        super(spider, classTarget, true);
    }

    public boolean shouldExecute()
    {
        double D = this.taskOwner.getBrightness();
        return D >= 0.5F ? false : super.shouldExecute();
    }
}