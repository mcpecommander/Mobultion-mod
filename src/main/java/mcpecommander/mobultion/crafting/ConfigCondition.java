package mcpecommander.mobultion.crafting;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import mcpecommander.mobultion.mobConfigs.ZombiesConfig;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class ConfigCondition implements IConditionFactory{

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return new BooleanSupplier() {
			
			@Override
			public boolean getAsBoolean() {
				return ZombiesConfig.zombies.magma.craftableSword;
			}
		};
	}

}
