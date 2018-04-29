package mcpecommander.mobultion.entity.animation;

import javax.vecmath.Quat4f;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.common.animation.CustomChannel;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.entity.entities.endermen.EntityWanderingEnderman;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.MathHelper;

public class AnimationCape extends CustomChannel{

	private String cape;
	
	public AnimationCape(String cape) {
		super("cape");
		this.cape = cape;
	}

	@Override
	public void update(CSModelRenderer parts, IAnimated animated) {
		if (animated instanceof EntityLiving){
            if (parts.boxName.equals(this.cape)) {
                EntityWanderingEnderman entityL = (EntityWanderingEnderman) animated;
                double d0 = entityL.prevChasingPosX + (entityL.chasingPosX - entityL.prevChasingPosX) * (double)1 - (entityL.prevPosX + (entityL.posX - entityL.prevPosX) * (double)1);
                double d1 = entityL.prevChasingPosY + (entityL.chasingPosY - entityL.prevChasingPosY) * (double)1 - (entityL.prevPosY + (entityL.posY - entityL.prevPosY) * (double)1);
                double d2 = entityL.prevChasingPosZ + (entityL.chasingPosZ - entityL.prevChasingPosZ) * (double)1 - (entityL.prevPosZ + (entityL.posZ - entityL.prevPosZ) * (double)1);
                float f = entityL.prevRenderYawOffset + (entityL.renderYawOffset - entityL.prevRenderYawOffset) * 1;
                double d3 = (double)MathHelper.sin(f * 0.017453292F);
                double d4 = (double)(-MathHelper.cos(f * 0.017453292F));
                float f1 = (float)d1 * 10.0F;
                f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
                float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
                float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;

                if (f2 < 0.0F)
                {
                    f2 = 0.0F;
                }

                //float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
                f1 = f1 + MathHelper.sin((entityL.prevDistanceWalkedModified + (entityL.distanceWalkedModified - entityL.prevDistanceWalkedModified) * 1) * 6.0F) * 32.0F * 1;

                if (entityL.isSneaking())
                {
                    f1 += 25.0F;
                }
                float x = 6.0F + f2 / 2.0F + f1;
                float y = -f3;
                float z = f3 / 2.0F;
                Quat4f quat = com.leviathanstudio.craftstudio.client.util.MathHelper.quatFromEuler(x, y, z);
                Quat4f quat2 = new Quat4f(parts.getDefaultRotationAsQuaternion());
                quat.mul(quat2);
                parts.getRotationMatrix().set(quat);
                parts.getRotationMatrix().transpose();
            }
        }
		
	}

}
