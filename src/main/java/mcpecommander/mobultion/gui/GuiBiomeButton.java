package mcpecommander.mobultion.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiBiomeButton extends GuiButtonExt{
	public Biome biome;
	private int page;
	public int delay;

	public GuiBiomeButton(int buttonId, int x, int y, Biome biome, int page) {
		super(buttonId, x, y, 100, 20, biome.getBiomeName());
		this.biome = biome;
		this.page = page;
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean flag = this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
		if(flag) {
			this.enabled = !this.enabled;
		}
		return flag;
	}

}
