package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.blocks.SpiderEggBlock;
import dev.mcpecommander.mobultion.blocks.tile.SpiderEggTile;
import dev.mcpecommander.mobultion.entities.spiders.SpidersConfig;
import dev.mcpecommander.mobultion.entities.spiders.entities.MotherSpiderEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/* McpeCommander created on 19/04/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entityGoals */
public class MotherSpiderEggLayingGoal extends Goal {

    MotherSpiderEntity mother;
    private long lastCanUseCheck;
    private static final int COOLDOWN = 350;
    private int pregnancyTime;
    private long layEggTime;

    public MotherSpiderEggLayingGoal(MotherSpiderEntity mother){
        this.mother = mother;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        return this.mother.level.getGameTime() - this.lastCanUseCheck > COOLDOWN &&
                this.mother.getMiniSpidersCount() < SpidersConfig.MOTHER_MAX_MINI_FOLLOWERS.get() - 4;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mother.getPregnancyStatus() > 0;
    }

    @Override
    public void start() {
        this.mother.setPregnant((byte)1);
        this.pregnancyTime = 0;
    }

    @Override
    public void stop() {
        this.mother.setPregnant((byte)0);
    }

    @Override
    public void tick() {
        byte pregnancyStatus = this.mother.getPregnancyStatus();
        BlockPos pos = new BlockPos(this.mother.getBlockX(), this.mother.getBlockY(), this.mother.getBlockZ());
        if(pregnancyStatus == 3 && this.mother.level.getGameTime() - this.layEggTime > 15
                && isPositionSuitableForEgg(this.mother.level, pos)){
            this.mother.setPregnant((byte)0);
            this.lastCanUseCheck = this.mother.level.getGameTime();
            this.mother.level.setBlock(this.mother.level.getBlockState(pos.below()).isAir() ? pos.below() : pos,
                    Registration.SPIDEREGG.get().defaultBlockState().setValue(SpiderEggBlock.EGGS,
                            this.mother.getRandom().nextInt(5) + 1), Block.UPDATE_ALL);
            if (this.mother.level.getBlockEntity(pos) instanceof SpiderEggTile spiderEgg){
                spiderEgg.setOwnerID(this.mother.getUUID());
            }
        }
        if(pregnancyStatus == 1){
            pregnancyTime ++;
            if(pregnancyTime > SpidersConfig.MOTHER_GESTATION.get() * 20){
                this.mother.setPregnant((byte)2);
            }
        }

        if(pregnancyStatus == (byte)2){
            this.mother.setPregnant((byte) 3);
            this.layEggTime = this.mother.level.getGameTime();
        }
    }

    private static boolean isPositionSuitableForEgg(Level level, BlockPos pos){
        return level.getBlockState(pos).canBeReplaced(new DirectionalPlaceContext(level, pos, Direction.DOWN, ItemStack.EMPTY, Direction.UP))
                && !level.getBlockState(pos.below()).isAir();
    }
}
