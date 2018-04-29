package mcpecommander.mobultion.entity.entities.spiders;

import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHypnoBall extends EntityFireball{
	public EntityHypnoBall(World worldIn)
    {
        super(worldIn);
        this.setSize(0.3125F, 0.3125F);
        
    }

    public EntityHypnoBall(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn);
        this.setSize(0.3125F, 0.3125F);
        this.shootingEntity = shooter;
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight() - 0.1d, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1D;
        this.accelerationY = accelY / d0 * 0.1D;
        this.accelerationZ = accelZ / d0 * 0.1D;
    }
    
    @Override
    protected boolean isFireballFiery() {
    	return false;
    }

    @Override
	protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            if (result.entityHit != null && result.entityHit instanceof EntityLivingBase)
            {
                    boolean flag = result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, (EntityLivingBase) result.entityHit).setProjectile(), 4.0F);

                    if (flag)
                    {
                        this.applyEnchantments(this.shootingEntity, result.entityHit);
                        PotionEffect potioneffect = new PotionEffect(ModPotions.potionHypnotize, 100, 0, false, false);
                        ((EntityLivingBase) result.entityHit).addPotionEffect(potioneffect);
                    }
            }
            
            this.setDead();
        }
    }

    @Override
	public boolean canBeCollidedWith()
    {
        return false;
    }

    @Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }
}
