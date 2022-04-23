package dev.mcpecommander.mobultion.blocks.renderer;

import dev.mcpecommander.mobultion.blocks.model.SpiderEggModel;
import dev.mcpecommander.mobultion.blocks.tile.SpiderEggTile;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.blocks.renderer */
public class SpiderEggRenderer extends GeoBlockRenderer<SpiderEggTile> {

    public SpiderEggRenderer(BlockEntityRendererProvider.Context rendererDispatcher) {
        super(rendererDispatcher, new SpiderEggModel());
    }

}
