package mcpecommander.mobultion.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.items.ItemSpawnChanger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class GuiMobBiomes extends GuiScreen {

	private GuiSlider slider;// = new GuiSlider(500, 0, 200, 300, 25, "some", "body", 0, 20, 0, false,
								// false);
	private List<GuiBiomeButton> buttons = new ArrayList<>();
	private int oldValue, currentValue;
	public List<String> biomesList = new ArrayList<>();
	public String weightS, minS, maxS;
	private GuiField weight, min, max;
	int delay = 0, delay1 = 0, delay2 = 0;
	public EntityLiving entity;

	@Override
	public void initGui() {
		super.initGui();
		int x = 5;
		int y = 10;
		int id = 0;
		int page = 0;
		boolean flag = false;
		for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
			GuiBiomeButton button = new GuiBiomeButton(id++, x, y, biome, page);
			if (flag)
				button.visible = false;
			if (biomesList.contains(biome.getRegistryName().toString()) || (biomesList.size() == 1 && biomesList.get(0).equals("all")))
				button.enabled = false;
			buttons.add(button);
			this.addButton(button);
			x += width / 4;
			if (id % 4 == 0) {
				y += height / 11;
				x = 5;
			}
			if (id % 32 == 0) {
				x = 5;
				y = 10;
				flag = true;
				page++;
			}
		}
		slider = new GuiSlider(id++, (width / 2) - (buttons.size() / 32 * 10), 200, buttons.size() / 32 * 20, 20, "",
				"", 0, buttons.size() / 32, this.getCurrentValue(), false, false);
		this.addButton(slider);
		weight = new GuiField(id++, this.fontRenderer, 10, 190, 100, 20, "Spawn Rates", GuiField.Type.WEIGHT);
		min = new GuiField(id++, this.fontRenderer, 10, 190 + weight.height + 5, 45, 20, "Minimum Spawn Count",
				GuiField.Type.MIN);
		max = new GuiField(id++, this.fontRenderer, 10 + min.width + 10, min.y, 45, 20, "Maximum Spawn Count",
				GuiField.Type.MAX);
		min.setText(minS);
		max.setText(maxS);
		weight.setText(weightS);
		weight.setEnableBackgroundDrawing(true);
		weight.setVisible(true);
		weight.setEnabled(true);
		min.setEnableBackgroundDrawing(true);
		min.setVisible(true);
		min.setEnabled(true);
		max.setEnableBackgroundDrawing(true);
		max.setVisible(true);
		max.setEnabled(true);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawWorldBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
		weight.drawTextBox();
		min.drawTextBox();
		max.drawTextBox();

		if (hovering(min, mouseX, mouseY)) {
			delay++;
		} else {
			delay = 0;
		}
		if (delay > 30) {
			this.drawHoveringText(min.getTooltips(), mouseX, mouseY);
		}

		if (hovering(max, mouseX, mouseY)) {
			delay1++;
		} else {
			delay1 = 0;
		}
		if (delay1 > 30) {
			this.drawHoveringText(max.getTooltips(), mouseX, mouseY);
		}

		if (hovering(weight, mouseX, mouseY)) {
			delay2++;
		} else {
			delay2 = 0;
		}
		if (delay2 > 30) {
			this.drawHoveringText(weight.getTooltips(), mouseX, mouseY);
		}

		for (GuiBiomeButton button : buttons) {
			if (button.isMouseOver()) {
				button.delay++;
				if (button.delay > 30) {
					this.drawHoveringText(button.biome.getRegistryName().getResourceDomain(),
							button.x + button.width / 2, button.y + button.height / 2);
				}
			} else {
				button.delay = 0;
			}
		}
	}

	private boolean hovering(GuiField obj, int mouseX, int mouseY) {
		return mouseX >= obj.x && mouseY >= obj.y && mouseX < obj.x + obj.width && mouseY < obj.y + obj.height;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		weight.mouseClicked(mouseX, mouseY, mouseButton);
		min.mouseClicked(mouseX, mouseY, mouseButton);
		max.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		weight.textboxKeyTyped(typedChar, keyCode);
		min.textboxKeyTyped(typedChar, keyCode);
		max.textboxKeyTyped(typedChar, keyCode);

	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (slider != null) {
			if (slider.getValueInt() != oldValue) {
				int newValue = slider.getValueInt();
				for (GuiButton button : buttons) {
					button.visible = false;
				}
				for (int i = newValue * 32; i < (newValue + 1) * 32 && i < buttons.size(); i++) {
					buttons.get(i).visible = true;
				}

			}
			oldValue = slider.getValueInt();
		}

	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		biomesList.clear();
		for (GuiBiomeButton button : buttons) {
			if (!button.enabled) {
				biomesList.add(button.biome.getRegistryName().toString());
			}
		}
		String[] temp = {};
		ItemSpawnChanger.setEntityBiomesByName(EntityList.getKey(entity).getResourcePath(), biomesList.toArray(temp));
		if (GuiField.checkValue(min, GuiField.Type.MIN) != -1) {
			ItemSpawnChanger.setEntityMinByName(EntityList.getKey(entity).getResourcePath(),
					GuiField.checkValue(min, GuiField.Type.MIN));
		}
		if (GuiField.checkValue(max, GuiField.Type.MAX) != -1) {
			ItemSpawnChanger.setEntityMaxByName(EntityList.getKey(entity).getResourcePath(),
					GuiField.checkValue(max, GuiField.Type.MAX));
		}
		if (GuiField.checkValue(weight, GuiField.Type.WEIGHT) != -1) {
			ItemSpawnChanger.setEntityWeightByName(EntityList.getKey(entity).getResourcePath(),
					GuiField.checkValue(weight, GuiField.Type.WEIGHT));
		}
		for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
			List<SpawnListEntry> list = biome.getSpawnableList(EnumCreatureType.MONSTER);
			for (SpawnListEntry entry : list) {
				if (entry.entityClass == entity.getClass()) {
					list.remove(entry);
					break;
				}
			}
			if (!biomesList.isEmpty()) {
				if (biomesList.contains(biome.getRegistryName().toString())) {
					biome.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(entity.getClass(),
							GuiField.checkValue(weight, GuiField.Type.WEIGHT),
							GuiField.checkValue(min, GuiField.Type.MIN), GuiField.checkValue(max, GuiField.Type.MAX)));
				}
			}
		}
		ConfigManager.sync(Reference.MOD_ID, Type.INSTANCE);
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public GuiMobBiomes setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
		return this;
	}

}
