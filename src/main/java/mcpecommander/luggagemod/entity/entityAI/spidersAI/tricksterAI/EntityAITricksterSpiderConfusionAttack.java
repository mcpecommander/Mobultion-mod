package mcpecommander.luggagemod.entity.entityAI.spidersAI.tricksterAI;

import mcpecommander.luggagemod.entity.entities.spiders.EntityTricksterSpider;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAITricksterSpiderConfusionAttack extends EntityAIBase
{
    World world;
    protected EntityTricksterSpider attacker;
    protected int attackTick;
    double speedTowardsTarget;
    boolean longMemory;
    Path entityPathEntity;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    protected final int attackInterval = 20;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;

    public EntityAITricksterSpiderConfusionAttack(EntityTricksterSpider creature)
    {
        this.attacker = creature;
        this.world = creature.world;
        this.speedTowardsTarget = 1.0D;
        this.longMemory = true;
        this.setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else
        {
            if (canPenalize && this.attacker.isConfusing())
            {
                if (--this.delayCounter <= 0)
                {
                    this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
                    this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                    return this.entityPathEntity != null;
                }
                else
                {
                    return true;
                }
            }
            this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);

            if (this.entityPathEntity != null && this.attacker.isConfusing())
            {
                return true;
            }
            else
            {
                return this.getAttackReachSqr(entitylivingbase) >= this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ) && this.attacker.isConfusing();
            }
        }
    }

    public boolean shouldContinueExecuting()
    {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else if (!this.longMemory)
        {
            return !this.attacker.getNavigator().noPath();
        }
        else if (!this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase)))
        {
            return false;
        }
        else
        {
            return !((EntityPlayer)entitylivingbase).isSpectator() || !((EntityPlayer)entitylivingbase).isCreative();
        }
    }

    public void startExecuting()
    {
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.delayCounter = 0;
    }

    public void resetTask()
    {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer)entitylivingbase).isSpectator() || ((EntityPlayer)entitylivingbase).isCreative()))
        {
            this.attacker.setAttackTarget((EntityLivingBase)null);
        }

        this.attacker.getNavigator().clearPathEntity();
    }

    public void updateTask()
    {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
        double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        --this.delayCounter;

        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F))
        {
            this.targetX = entitylivingbase.posX;
            this.targetY = entitylivingbase.getEntityBoundingBox().minY;
            this.targetZ = entitylivingbase.posZ;
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);

            if (this.canPenalize)
            {
                this.delayCounter += failedPathFindingPenalty;
                if (this.attacker.getNavigator().getPath() != null)
                {
                    net.minecraft.pathfinding.PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                    if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                        failedPathFindingPenalty = 0;
                    else
                        failedPathFindingPenalty += 10;
                }
                else
                {
                    failedPathFindingPenalty += 10;
                }
            }

            if (d0 > 1024.0D)
            {
                this.delayCounter += 10;
            }
            else if (d0 > 256.0D)
            {
                this.delayCounter += 5;
            }

            if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget))
            {
                this.delayCounter += 15;
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.checkAndPerformAttack(entitylivingbase, d0);
    }

    protected void checkAndPerformAttack(EntityLivingBase entity, double distance)
    {
        double d0 = this.getAttackReachSqr(entity);

        if (distance <= d0 && this.attackTick <= 0)
        {
            this.attackTick = 30;
            this.confuseAttack();
            this.attacker.setMode((byte) 0);
        }
    }
    
    public void confuseAttack(){
    	this.confuseAttackEffect();
    	this.attacker.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
    	int yaw = -this.attacker.getRNG().nextInt(360);
    	EntityPlayerMP player = (EntityPlayerMP) this.attacker.getAttackTarget();
    	player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, yaw, player.rotationPitch);
    	this.attacker.attackEntityAsMob(this.attacker.getAttackTarget());
    	
    }
    
	public void confuseAttackEffect() {
		if (this.attacker.isServerWorld()) {
			if (this.attacker.getAttackTarget() != null && !this.attacker.getAttackTarget().isDead) {				
				double velocityX ;
				double velocityZ ;

				if((this.attacker.getAttackTarget().rotationYaw < 0 && this.attacker.getAttackTarget().rotationYaw > -90) ||(this.attacker.getAttackTarget().rotationYaw > -360 && this.attacker.getAttackTarget().rotationYaw < -270)){
					velocityZ = .5;
				}else{
					velocityZ = -.5;
				}
				if(this.attacker.getAttackTarget().rotationYaw < 0 && this.attacker.getAttackTarget().rotationYaw > -180){
					velocityX = .5;
				}else{
					velocityX = -.5;
				}
				
				 this.attacker.spawnEffect(this.world, this.attacker.getAttackTarget().posX + velocityX, this.attacker.getAttackTarget().posY + this.attacker.getAttackTarget().getEyeHeight(), this.attacker.getAttackTarget().posZ + velocityZ, 0, 0, 0, 4f, 10 );
			}
		}

	}

    protected double getAttackReachSqr(EntityLivingBase attackTarget)
    {
        return (double)(4.0F + attackTarget.width);
    }
}