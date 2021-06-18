package dev.mcpecommander.mobultion.entities.spiders.models;

import dev.mcpecommander.mobultion.entities.spiders.entities.AngelSpiderEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
public class AngelSpiderModel extends AnimatedGeoModel<AngelSpiderEntity>{

	@Override
	public ResourceLocation getModelLocation(AngelSpiderEntity object) {
		return new ResourceLocation(MODID, "geo/angelspider.json");
	}

	@Override
	public ResourceLocation getTextureLocation(AngelSpiderEntity object) {
		return new ResourceLocation(MODID, "textures/entity/angelspider.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation(AngelSpiderEntity animatable) {
		return new ResourceLocation(MODID, "animations/angelspider.animation.json");
	}

	@Override
	public void setLivingAnimations(AngelSpiderEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("Head");

		assert customPredicate != null;
		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
		head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

	}

}