package mcpecommander.mobultion.entity.entities.skeletons;

import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.init.ModPotions;
import mcpecommander.mobultion.mobConfigs.SkeletonsConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHeartArrow extends EntityArrow {

	public EntityHeartArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityHeartArrow(World world) {
		super(world);
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	protected void onHit(RayTraceResult raytraceResultIn) {
		this.setDamage(SkeletonsConfig.skeletons.joker.arrowDamage);
		super.onHit(raytraceResultIn);
		if (raytraceResultIn.entityHit != null && !raytraceResultIn.entityHit.isDead) {
			if (raytraceResultIn.entityHit instanceof EntityLivingBase) {
				this.applyEnchantments((EntityLivingBase) this.shootingEntity, raytraceResultIn.entityHit);
				PotionEffect potioneffect = new PotionEffect(ModPotions.potionJokerness,
						SkeletonsConfig.skeletons.joker.jokerness, 0, false, false);
				((EntityLivingBase) raytraceResultIn.entityHit).addPotionEffect(potioneffect);
			}
		}
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ModItems.heartArrow);
	}

}
