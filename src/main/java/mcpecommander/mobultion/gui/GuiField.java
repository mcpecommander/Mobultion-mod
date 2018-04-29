package mcpecommander.mobultion.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiField extends GuiTextField{
	
	private String tooltips;
	private Type type;

	public GuiField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height, String tooltips, Type type) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
		this.tooltips = tooltips;
		this.type = type;
		this.setMaxStringLength(10);
	}
	
	public String getTooltips() {
		return tooltips;
	}

	public void setTooltips(String tooltips) {
		this.tooltips = tooltips;
	}
	
	@Override
	public boolean textboxKeyTyped(char typedChar, int keyCode) {
		String validChars = "0123456789";
		if(validChars.contains(String.valueOf(typedChar)) 
                || keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_DELETE
                || keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT || keyCode == Keyboard.KEY_HOME || keyCode == Keyboard.KEY_END ) {
			return super.textboxKeyTyped(typedChar, keyCode);
		}
		return false;
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		boolean flag = this.isFocused();
		boolean flag1 = super.mouseClicked(mouseX, mouseY, mouseButton);
		if(flag && !this.isFocused()) {
			if(!this.getText().trim().isEmpty()) {
				try
                {
                    long value = Long.parseLong(this.getText().trim());
                    switch(type) {
                    case MIN:
                    	if (value <  16 && value > 0) {
                        	return flag1;
                        }else {
                        	this.setText("");
                        	break;
                        }
                    case MAX:
                    	if (value <  16 && value > 0) {
                        	return flag1;
                        }else {
                        	this.setText("");
                        	break;
                        }
                    case WEIGHT:
                    	if (value <  500 && value > 0) {
                        	return flag1;
                        }else {
                        	this.setText("");
                        	break;
                        }
					default:
						break;
                    
                    }
                }
                catch (Throwable e)
                {}
			}
		}
		return flag1;
	}
	
	public static int checkValue(GuiField gui, Type type) {
		if(!gui.getText().trim().isEmpty()) {
			try
            {
                long value = Long.parseLong(gui.getText().trim());
                switch(type) {
                case MIN:
                	if (value <  16 && value > 0) {
                    	return (int) value;
                    }else {
                    	gui.setText("");
                    	break;
                    }
                case MAX:
                	if (value <  16 && value > 0) {
                    	return (int) value;
                    }else {
                    	gui.setText("");
                    	break;
                    }
                case WEIGHT:
                	if (value <  500 && value > 0) {
                    	return (int) value;
                    }else {
                    	gui.setText("");
                    	break;
                    }
				default:
					break;
                
                }
            }
            catch (Throwable e)
            {}
		}
		return -1;
	}
	
	public enum Type{
		WEIGHT,
		MIN,
		MAX
	}

}
