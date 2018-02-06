package mcpecommander.mobultion.entity.entityAI.endermenAI;

import java.util.Random;

import mcpecommander.mobultion.entity.entities.endermen.EntityAnimatedEnderman;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIPlaceBlock extends EntityAIBase
{
    private final EntityAnimatedEnderman enderman;

    public EntityAIPlaceBlock(EntityAnimatedEnderman enderman)
    {
        this.enderman = enderman;
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.enderman.getHeldBlockState() == null)
        {
            return false;
        }
        else if (!this.enderman.world.getGameRules().getBoolean("mobGriefing"))
        {
            return false;
        }
        else
        {
            return this.enderman.getRNG().nextInt(2000) == 0;
        }
    }

    @Override
    public void updateTask()
    {
        Random random = this.enderman.getRNG();
        World world = this.enderman.world;
        int i = MathHelper.floor(this.enderman.posX - 1.0D + random.nextDouble() * 2.0D);
        int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 2.0D);
        int k = MathHelper.floor(this.enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = world.getBlockState(blockpos);
        IBlockState iblockstate1 = world.getBlockState(blockpos.down());
        IBlockState iblockstate2 = this.enderman.getHeldBlockState();

        if (iblockstate2 != null && this.canPlaceBlock(world, blockpos, iblockstate2.getBlock(), iblockstate, iblockstate1))
        {
            world.setBlockState(blockpos, iblockstate2, 3);
            this.enderman.setHeldBlockState((IBlockState)null);
        }
    }

    private boolean canPlaceBlock(World world, BlockPos placePos, Block blockUnder, IBlockState blockState, IBlockState blockUnderState)
    {
        if (!blockUnder.canPlaceBlockAt(world, placePos))
        {
            return false;
        }
        else if (blockState.getMaterial() != Material.AIR)
        {
            return false;
        }
        else if (blockUnderState.getMaterial() == Material.AIR)
        {
            return false;
        }
        else
        {
            return blockUnderState.isFullCube();
        }
    }
}