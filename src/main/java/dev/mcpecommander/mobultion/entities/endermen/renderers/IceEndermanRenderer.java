package dev.mcpecommander.mobultion.entities.endermen.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.endermen.entities.IceEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanEyesLayer;
import dev.mcpecommander.mobultion.entities.endermen.models.IceEndermanModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 07/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class IceEndermanRenderer extends GeoEntityRenderer<IceEndermanEntity> {

    public IceEndermanRenderer(EntityRendererManager renderManager) {
        super(renderManager, new IceEndermanModel());
        this.shadowRadius = 0.5F;
        this.addLayer(new EndermanEyesLayer<>(this, "iceenderman"));
    }

    @Override
    public void render(IceEndermanEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if(entity.isDeadOrDying()){
            this.shadowRadius = 0f;
        }else{
            this.shadowRadius = 0.5F;
        }
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public RenderType getRenderType(IceEndermanEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    protected float getDeathMaxRotation(IceEndermanEntity entityLivingBaseIn) {
        return 0f;
    }
}
