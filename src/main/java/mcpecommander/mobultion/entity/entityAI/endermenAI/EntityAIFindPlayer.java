package mcpecommander.mobultion.entity.entityAI.endermenAI;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import mcpecommander.mobultion.entity.entities.endermen.EntityAnimatedEnderman;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIFindPlayer  extends EntityAINearestAttackableTarget<EntityPlayer>
{
    private final EntityAnimatedEnderman enderman;
    private EntityPlayer player;
    private int aggroTime;
    private int teleportTime;

    public EntityAIFindPlayer(EntityAnimatedEnderman enderman)
    {
        super(enderman, EntityPlayer.class, false);
        this.enderman = enderman;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        double d0 = this.getTargetDistance();
        this.player = this.enderman.world.getNearestAttackablePlayer(this.enderman.posX, this.enderman.posY, this.enderman.posZ, d0, d0, (Function)null, new Predicate<EntityPlayer>()
        {
            public boolean apply(@Nullable EntityPlayer player)
            {
                return player != null && EntityAIFindPlayer.this.enderman.shouldAttackPlayer(player);
            }
        });
        return this.player != null;
    }

    @Override
    public void startExecuting()
    {
        this.aggroTime = 5;
        this.teleportTime = 0;
    }

    @Override
    public void resetTask()
    {
        this.player = null;
        super.resetTask();
    }
    
    @Override
    public boolean shouldContinueExecuting()
    {
        if (this.player != null)
        {
            if (!this.enderman.shouldAttackPlayer(this.player))
            {
                return false;
            }
            else
            {
                this.enderman.faceEntity(this.player, 10.0F, 10.0F);
                return true;
            }
        }
        else
        {
            return this.targetEntity != null && ((EntityPlayer)this.targetEntity).isEntityAlive() ? true : super.shouldContinueExecuting();
        }
    }

    @Override
    public void updateTask()
    {
        if (this.player != null)
        {
            if (--this.aggroTime <= 0)
            {
                this.targetEntity = this.player;
                this.player = null;
                super.startExecuting();
            }
        }
        else
        {
            if (this.targetEntity != null)
            {
                if (this.enderman.shouldAttackPlayer((EntityPlayer)this.targetEntity))
                {
                    if (((EntityPlayer)this.targetEntity).getDistanceSq(this.enderman) < 16.0D)
                    {
                        this.enderman.teleportRandomly();
                    }

                    this.teleportTime = 0;
                }
                else if (((EntityPlayer)this.targetEntity).getDistanceSq(this.enderman) > 256.0D && this.teleportTime++ >= 30 && this.enderman.teleportToEntity(this.targetEntity))
                {
                    this.teleportTime = 0;
                }
            }

            super.updateTask();
        }
    }
}