package mcpecommander.luggagemod.entity.entities.skeletons;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;

public interface IGetEquip {

	/**
	 * Return an item using Items.SOMETHING or ModItems.SOMETHING but never a new Item.
	 */
	@Nonnull
	Item getEquip();
}
