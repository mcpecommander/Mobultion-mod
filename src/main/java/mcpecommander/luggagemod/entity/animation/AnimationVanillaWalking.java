package mcpecommander.luggagemod.entity.animation;

import javax.vecmath.Quat4f;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.util.MathHelper;
import com.leviathanstudio.craftstudio.common.animation.CustomChannel;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.luggagemod.entity.entities.zombies.EntityAnimatedZombie;

public class AnimationVanillaWalking extends CustomChannel{

	public AnimationVanillaWalking() {
		super("walking");
		
	}

	@Override
	public void update(CSModelRenderer part, IAnimated animated) {
		if(animated instanceof EntityAnimatedZombie){
			if(part.boxName.equals("RightLeg") || part.boxName.equals("RightArm")){
				EntityAnimatedZombie entity = (EntityAnimatedZombie) animated;
				float f = (float) (Math.cos(entity.limbSwing * 0.6662F) * 1.4F * entity.limbSwingAmount);
				f = (float) Math.toDegrees(f);
				Quat4f q = MathHelper.quatFromEuler(f, 0, 0);
				part.getRotationMatrix().set(q);
				part.getRotationMatrix().transpose();
			}
			if(part.boxName.equals("LeftLeg") || part.boxName.equals("LeftArm")){
				EntityAnimatedZombie entity = (EntityAnimatedZombie) animated;
				float f = (float) (Math.cos(entity.limbSwing * 0.6662F + Math.PI) * 1.4F * entity.limbSwingAmount);
				f = (float) Math.toDegrees(f);
				Quat4f q = MathHelper.quatFromEuler(f, 0, 0);
				part.getRotationMatrix().set(q);
				part.getRotationMatrix().transpose();
			}
		}
		
		
	}
	
	/*
	 * this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;
        this.bipedRightLeg.rotateAngleZ = 0.0F;
        this.bipedLeftLeg.rotateAngleZ = 0.0F;
	 */

}
