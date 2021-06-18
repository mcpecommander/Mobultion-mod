package dev.mcpecommander.mobultion.entities.spiders.models;

import dev.mcpecommander.mobultion.entities.spiders.entities.HypnoSpiderEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
public class HypnoSpiderModel extends AnimatedGeoModel<HypnoSpiderEntity> {

    @Override
    public ResourceLocation getModelLocation(HypnoSpiderEntity object) {
        return new ResourceLocation(MODID, "geo/hypnospider.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HypnoSpiderEntity object) {
        return new ResourceLocation(MODID, "textures/entity/hypnospider.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HypnoSpiderEntity animatable) {
        return new ResourceLocation(MODID, "animations/hypnospider.animation.json");
    }

    @Override
    public void setLivingAnimations(HypnoSpiderEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        assert customPredicate != null;
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        if(entity.isDeadOrDying()) {
            head.setRotationX(0f);
            head.setRotationY(0f);
        }

    }
}
