package dev.mcpecommander.mobultion.entities.zombies.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.zombies.entities.KnightZombieEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 27/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.layers */
public class IronArmourLayer extends GeoLayerRenderer<KnightZombieEntity> {

    private static final ResourceLocation IRON_ARMOUR = new ResourceLocation(MODID, "textures/entity/zombies/ironarmour.png");

    public IronArmourLayer(IGeoRenderer<KnightZombieEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    //It is way easier to just make the entity render the whole iron armour instead of making if statements for each part.
    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int packedLight,
                       KnightZombieEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        stack.pushPose();
        stack.scale(1.1f, 1.01f, 1.1f);
        renderCopyModel((GeoModelProvider<KnightZombieEntity>) this.getEntityModel(), IRON_ARMOUR, stack, buffer, packedLight, entity, partialTicks, 1f, 1f,1f);
        stack.popPose();
    }
}
