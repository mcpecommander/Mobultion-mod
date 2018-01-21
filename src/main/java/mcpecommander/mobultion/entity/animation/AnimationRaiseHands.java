package mcpecommander.mobultion.entity.animation;

import javax.vecmath.Quat4f;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.util.MathHelper;
import com.leviathanstudio.craftstudio.common.animation.CustomChannel;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AnimationRaiseHands extends CustomChannel{

	public AnimationRaiseHands() {
		super("raise_hands");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void update(CSModelRenderer part, IAnimated animated) {
		if(animated instanceof EntityLiving){
			if(part.boxName.equals("LeftArm")){
				Quat4f quat = MathHelper.quatFromEuler(-90f, 0f, 0f);
				Quat4f quat1 = new Quat4f(part.getDefaultRotationAsQuaternion());
				quat.mul(quat1);
				part.getRotationMatrix().set(quat);
				part.getRotationMatrix().transpose();
			}
			if(part.boxName.equals("RightArm")){
				Quat4f quat = MathHelper.quatFromEuler(-90f, 0f, 0f);
				Quat4f quat1 = new Quat4f(part.getDefaultRotationAsQuaternion());
				quat.mul(quat1);
				part.getRotationMatrix().set(quat);
				part.getRotationMatrix().transpose();
			}
		}
		
	}

}
