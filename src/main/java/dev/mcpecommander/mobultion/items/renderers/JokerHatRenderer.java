package dev.mcpecommander.mobultion.items.renderers;

import dev.mcpecommander.mobultion.items.JokerHatItem;
import dev.mcpecommander.mobultion.items.models.JokerHatModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.renderers */
public class JokerHatRenderer extends GeoItemRenderer<JokerHatItem>
{

    public JokerHatRenderer()
    {
        super(new JokerHatModel());
    }

}
