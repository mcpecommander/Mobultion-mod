package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.items.renderers.HaloRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;
import software.bernie.example.client.renderer.item.JackInTheBoxRenderer;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import net.minecraft.world.item.Item.Properties;

import java.util.function.Consumer;

/* McpeCommander created on 07/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HaloItem extends Item implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public HaloItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).stacksTo(1));
    }

    @Override
    public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new HaloRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(entity instanceof Wolf){
            Wolf wolf = (Wolf) entity;
            if(wolf.isTame() && wolf.getOwnerUUID() != null && wolf.getOwnerUUID().equals(player.getUUID()) &&
                wolf.getItemBySlot(EquipmentSlot.HEAD).isEmpty()){
                if(!player.level.isClientSide) {
                    wolf.setItemSlot(EquipmentSlot.HEAD, stack.copy());
                }
                if(!player.getAbilities().instabuild) stack.shrink(1);
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
