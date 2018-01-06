package mcpecommander.luggagemod.entity.entities.skeletons;

import mcpecommander.luggagemod.init.ModItems;
import mcpecommander.luggagemod.init.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHeartArrow extends EntityArrow{

    public EntityHeartArrow(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
    }
    
    public EntityHeartArrow(World world){
    	super(world);
    }
    @Override
    public void onEntityUpdate() {
    	super.onEntityUpdate();
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
    }
    
    @Override
	protected void onHit(RayTraceResult raytraceResultIn) {
    	this.setDamage(0.1D);
		super.onHit(raytraceResultIn);
		if(raytraceResultIn.entityHit != null && !raytraceResultIn.entityHit.isDead){
			if(raytraceResultIn.entityHit instanceof EntityLivingBase){
				this.applyEnchantments((EntityLivingBase) this.shootingEntity, raytraceResultIn.entityHit);
                PotionEffect potioneffect = new PotionEffect(ModPotions.potionJokerness, 200, 0, false, false);
                ((EntityLivingBase) raytraceResultIn.entityHit).addPotionEffect(potioneffect);
			}
		}
	}

    protected ItemStack getArrowStack()
    {
    	return new ItemStack(ModItems.heartArrow);
    }

}
