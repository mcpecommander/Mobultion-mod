package mcpecommander.mobultion.entity.animation;

import javax.vecmath.Quat4f;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.util.MathHelper;
import com.leviathanstudio.craftstudio.common.animation.CustomChannel;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AnimationLookAtWither extends CustomChannel{

	private String headPart;
	private String headPart1;
	private String headPart2;
	
	public AnimationLookAtWither(String headPart, String headPart1, String headPart2) {
		super("wither_lookat");
        this.headPart = headPart;
        this.headPart1 = headPart1;
        this.headPart2 = headPart2;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void update(CSModelRenderer parts, IAnimated animated) {
        if (animated instanceof EntityLiving){
            if (parts.boxName.equals(this.headPart) || parts.boxName.equals(this.headPart1) || parts.boxName.equals(this.headPart2)) {
                EntityLiving entityL = (EntityLiving) animated;
                float diff = entityL.getRotationYawHead() - entityL.renderYawOffset;
                Quat4f quat = MathHelper.quatFromEuler(entityL.rotationPitch, diff, 0.0F);
                Quat4f quat2 = new Quat4f(parts.getDefaultRotationAsQuaternion());
                quat.mul(quat2);
                parts.getRotationMatrix().set(quat);
                parts.getRotationMatrix().transpose();
            }
        }
    }

}
