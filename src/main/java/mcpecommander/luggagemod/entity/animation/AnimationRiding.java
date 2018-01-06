package mcpecommander.luggagemod.entity.animation;

import javax.vecmath.Quat4f;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.util.MathHelper;
import com.leviathanstudio.craftstudio.common.animation.CustomChannel;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.luggagemod.entity.entities.skeletons.EntityAnimatedSkeleton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AnimationRiding extends CustomChannel{

	public AnimationRiding() {
		super("riding");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void update(CSModelRenderer part, IAnimated animated) {
		if(animated instanceof EntityAnimatedSkeleton){
			if(part.boxName.equals("LeftLeg")){
				Quat4f quat = MathHelper.quatFromEuler(-90, -20f, 0f);
				Quat4f quat2 = new Quat4f(part.getDefaultRotationAsQuaternion());
				quat.mul(quat2);
                part.getRotationMatrix().set(quat);
                part.getRotationMatrix().transpose();
			}
			if(part.boxName.equals("LeftLeg1")){
				Quat4f quat = MathHelper.quatFromEuler(80f, -180f, 180);
				Quat4f quat2 = new Quat4f(part.getDefaultRotationAsQuaternion());
				quat.mul(quat2);
                part.getRotationMatrix().set(quat);
                part.getRotationMatrix().transpose();
			}
			if(part.boxName.equals("RightLeg")){
				Quat4f quat = MathHelper.quatFromEuler(-90f, 20f, 0f);
				Quat4f quat2 = new Quat4f(part.getDefaultRotationAsQuaternion());
				quat.mul(quat2);
                part.getRotationMatrix().set(quat);
                part.getRotationMatrix().transpose();
			}
			if(part.boxName.equals("RightLeg1")){
				Quat4f quat = MathHelper.quatFromEuler(80f, 180f, 180f);
				Quat4f quat2 = new Quat4f(part.getDefaultRotationAsQuaternion());
				quat.mul(quat2);
                part.getRotationMatrix().set(quat);
                part.getRotationMatrix().transpose();
			}
		}
	}

}
