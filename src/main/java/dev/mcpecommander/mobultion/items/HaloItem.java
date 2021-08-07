package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.items.renderers.HaloRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/* McpeCommander created on 07/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HaloItem extends Item implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public HaloItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).stacksTo(1).setISTER(() -> HaloRenderer::new));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if(entity instanceof WolfEntity){
            WolfEntity wolf = (WolfEntity) entity;
            if(wolf.isTame() && wolf.getOwnerUUID() != null && wolf.getOwnerUUID().equals(player.getUUID()) &&
                wolf.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()){
                if(!player.level.isClientSide) {
                    wolf.setItemSlot(EquipmentSlotType.HEAD, stack.copy());
                }
                if(!player.abilities.instabuild) stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
