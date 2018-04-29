package mcpecommander.mobultion.items;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemThunderWand extends Item{
	
	public ItemThunderWand() {
		this.setRegistryName(Reference.MobultionItems.THUNDERWAND.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.THUNDERWAND.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
		this.setMaxDamage(20);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		RayTraceResult result = this.rayTrace(worldIn, playerIn, true);
		if(result != null && !worldIn.isRemote){
			if(result.typeOfHit == RayTraceResult.Type.MISS){
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
			}else if (result.typeOfHit == RayTraceResult.Type.BLOCK){
				EntityLightningBolt bolt = new EntityLightningBolt(worldIn, result.hitVec.x, result.hitVec.y, result.hitVec.z, false);
				worldIn.addWeatherEffect(bolt);
				playerIn.getHeldItem(handIn).damageItem(1, playerIn);
				playerIn.getCooldownTracker().setCooldown(this, 100);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
			}else if (result.typeOfHit == RayTraceResult.Type.ENTITY){
				EntityLightningBolt bolt = new EntityLightningBolt(worldIn, result.entityHit.posX, result.entityHit.posY, result.entityHit.posZ, false);
				worldIn.addWeatherEffect(bolt);
				playerIn.getHeldItem(handIn).damageItem(1, playerIn);
				playerIn.getCooldownTracker().setCooldown(this, 100);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
			}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	protected RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids){
		Vec3d vec3d = playerIn.getPositionEyes(1.0f);
        Vec3d vec3d1 = playerIn.getLook(1.0f);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * 20, vec3d1.y * 20, vec3d1.z * 20);
        List<Entity> list = worldIn.getEntitiesInAABBexcluding(playerIn, playerIn.getEntityBoundingBox().expand(vec3d1.x * 20, vec3d1.y * 20, vec3d1.z * 20).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
        {
            @Override
			public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
            }
        }));
        for (int j = 0; j < list.size(); ++j)
        {
            Entity target = list.get(j);
            AxisAlignedBB axisalignedbb = target.getEntityBoundingBox().grow(target.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

            if (axisalignedbb.contains(vec3d))
            {
                 return new RayTraceResult(target);

            }
            else if (raytraceresult != null)
            {
            	return new RayTraceResult(target);
                
            }
        }
        return worldIn.rayTraceBlocks(vec3d, vec3d2, false, false, true);
        
	}
	
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
