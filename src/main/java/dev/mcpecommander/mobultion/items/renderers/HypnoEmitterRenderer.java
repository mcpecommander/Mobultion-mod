package dev.mcpecommander.mobultion.items.renderers;

import dev.mcpecommander.mobultion.items.HypnoEmitterItem;
import dev.mcpecommander.mobultion.items.models.HypnoEmitterModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.renderers */
public class HypnoEmitterRenderer extends GeoItemRenderer<HypnoEmitterItem>
{

    public HypnoEmitterRenderer()
    {
        super(new HypnoEmitterModel());
    }

}
