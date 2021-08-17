package dev.mcpecommander.mobultion.entities.endermen.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.endermen.entities.MobultionEndermanEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 25/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.layers */
public class EndermanEyesLayer<T extends MobultionEndermanEntity> extends GeoLayerRenderer<T> {

    /**
     * The resource location for a texture that matches the model texture in size but highlights the parts that will
     * light.
     */
    private final ResourceLocation ENDERMAN_EYES;
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private final ResourceLocation ENDERMAN_MODEL;

    public EndermanEyesLayer(IGeoRenderer<T> entityRendererIn, String endermanName) {
        super(entityRendererIn);
        ENDERMAN_MODEL = new ResourceLocation(MODID, "geo/endermen/"+ endermanName +".json");
        String eyes = endermanName.equals("wanderingenderman") ? "wanderingenderman" : "enderman";
        ENDERMAN_EYES = new ResourceLocation(MODID, "textures/entity/endermen/" + eyes + "eyes.png");
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        stack.pushPose();
        //Copied from GeoLayerRenderer.
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.eyes(ENDERMAN_EYES));
        //The main group of my model is "all"
        //This section is from renderRecursively but I am doing the first step here to stop anything other than the
        //head from rendering which guarantees that the render type is not changed when rendering items.
        GeoBone all = getEntityModel().getModel(ENDERMAN_MODEL).topLevelBones.get(0);
        RenderUtils.translate(all, stack);
        RenderUtils.moveToPivot(all, stack);
        RenderUtils.rotate(all, stack);
        RenderUtils.scale(all, stack);
        RenderUtils.moveBackFromPivot(all, stack);
        for (GeoBone childBone : all.childBones) {
            //Makes sure that only the head main group is being rendered since that the only part that lights in the
            //eye layer.
            if(!childBone.getName().equals("Head")) continue;
            this.getRenderer().renderRecursively(childBone, stack, ivertexbuilder, packedLightIn,
                    LivingRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1f, 1f, 1f, 1f);
        }
        stack.popPose();
    }
}