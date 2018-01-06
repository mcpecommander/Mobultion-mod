package mcpecommander.luggagemod.entity.entities.spiders;

import mcpecommander.luggagemod.init.ModPotions;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
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
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }
    
    @Override
    protected boolean isFireballFiery() {
    	return false;
    }

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

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }
}
