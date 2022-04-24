package dev.mcpecommander.mobultion.blocks.tile;

import dev.mcpecommander.mobultion.blocks.SpiderEggBlock;
import dev.mcpecommander.mobultion.entities.spiders.entities.MiniSpiderEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.UUID;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.blocks */
public class SpiderEggTile extends BlockEntity implements IAnimatable {

    private int maxTime, currentTime;
    private UUID ownerID;

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public SpiderEggTile(BlockPos pos, BlockState state) {
        super(Registration.SPIDEREGG_TILE.get(), pos, state);
    }

    @Override
    public void setLevel(@NotNull Level level) {
        super.setLevel(level);
        maxTime = 100 + level.random.nextInt(60);
    }

    public void tick() {
        currentTime++;
        if(currentTime > maxTime && this.level != null && !this.level.isClientSide){
            for(int i = 0; i < this.getBlockState().getValue(SpiderEggBlock.EGGS); i++){
                MiniSpiderEntity spider = new MiniSpiderEntity(Registration.MINISPIDER.get(), level);
                spider.setPos(Vec3.atCenterOf(this.worldPosition).add(level.random.nextGaussian() * 0.5 - 0.5, 0,
                        level.random.nextGaussian() * 0.5 - 0.5));
                level.addFreshEntity(spider);
                spider.setOwnerID(this.ownerID);
            }
            this.setRemoved();
            this.level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    /**
     * The predicate for the animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState shouldAnimate(AnimationEvent<E> event)
    {
        if(maxTime - currentTime < 30){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hatch", false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public void setOwnerID(UUID ID){
        this.ownerID = ID;
        this.setChanged();
    }

    public UUID getOwnerID(){
        return this.ownerID;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag NBTTag) {
        super.saveAdditional(NBTTag);
        if (this.ownerID != null) {
            NBTTag.putUUID("mobultion:ID", this.ownerID);
        }
    }

    @Override
    public void load(@NotNull CompoundTag NBTTag) {
        super.load(NBTTag);
        if(NBTTag.hasUUID("mobultion:ID")){
            this.ownerID = NBTTag.getUUID("mobultion:ID");
        }
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::shouldAnimate));

    }

    /**
     * Getter for the animation factory. Client side only but not null on the server.
     * @return AnimationFactory
     */
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


}
