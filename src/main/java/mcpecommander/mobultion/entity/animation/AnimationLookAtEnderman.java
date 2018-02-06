package mcpecommander.mobultion.entity.animation;

import javax.vecmath.Quat4f;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.util.MathHelper;
import com.leviathanstudio.craftstudio.common.animation.CustomChannel;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import net.minecraft.entity.EntityLiving;

public class AnimationLookAtEnderman extends CustomChannel{

	private String headPart, jawPart;
	
	public AnimationLookAtEnderman(String headPart, String jawPart) {
		super("lookat");
        this.headPart = headPart;
        this.jawPart = jawPart;
	}

	@Override
	public void update(CSModelRenderer part, IAnimated animated) {
		if (animated instanceof EntityLiving){
            if (part.boxName.equals(this.headPart) || part.boxName.equals(this.jawPart)) {
                EntityLiving entityL = (EntityLiving) animated;
                float diff = entityL.getRotationYawHead() - entityL.renderYawOffset;
                Quat4f quat = MathHelper.quatFromEuler(entityL.rotationPitch, diff, 0.0F);
                Quat4f quat2 = new Quat4f(part.getDefaultRotationAsQuaternion());
                quat.mul(quat2);
                part.getRotationMatrix().set(quat);
                part.getRotationMatrix().transpose();
            }
            
        }
		
	}

}
