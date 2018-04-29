package mcpecommander.mobultion.gui;

import mcpecommander.mobultion.MobultionMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == MobultionMod.GUI_CONFIG) {
			return new GuiMobBiomes();
		}
		return new GuiMobBiomes();
	}

}
