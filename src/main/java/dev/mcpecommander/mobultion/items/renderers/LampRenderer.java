package dev.mcpecommander.mobultion.items.renderers;

import dev.mcpecommander.mobultion.items.LampItem;
import dev.mcpecommander.mobultion.items.models.LampModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.renderers */
public class LampRenderer extends GeoItemRenderer<LampItem>
{

    public LampRenderer()
    {
        super(new LampModel());
    }

}
