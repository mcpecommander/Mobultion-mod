package mcpecommander.mobultion.entity.entityAI.endermenAI;

import java.util.Random;

import mcpecommander.mobultion.entity.entities.endermen.EntityAnimatedEnderman;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAITakeBlock extends EntityAIBase {
	private final EntityAnimatedEnderman enderman;

	public EntityAITakeBlock(EntityAnimatedEnderman enderman) {
		this.enderman = enderman;
	}

	@Override
	public boolean shouldExecute() {
		if (this.enderman.getHeldBlockState() != null) {
			return false;
		} else if (!this.enderman.world.getGameRules().getBoolean("mobGriefing")) {
			return false;
		} else {
			return this.enderman.getRNG().nextInt(20) == 0;
		}
	}

	@Override
	public void updateTask() {
		Random random = this.enderman.getRNG();
		World world = this.enderman.world;
		int i = MathHelper.floor(this.enderman.posX - 2.0D + random.nextDouble() * 4.0D);
		int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 3.0D);
		int k = MathHelper.floor(this.enderman.posZ - 2.0D + random.nextDouble() * 4.0D);
		BlockPos blockpos = new BlockPos(i, j, k);
		IBlockState iblockstate = world.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		RayTraceResult raytraceresult = world.rayTraceBlocks(
				new Vec3d(MathHelper.floor(this.enderman.posX) + 0.5F, j + 0.5F,
						MathHelper.floor(this.enderman.posZ) + 0.5F),
				new Vec3d(i + 0.5F, j + 0.5F, k + 0.5F), false,
				true, false);
		boolean flag = raytraceresult != null && raytraceresult.getBlockPos().equals(blockpos);

		if (flag && !block.hasTileEntity(iblockstate) && iblockstate.isFullBlock() && iblockstate.isNormalCube()
				&& iblockstate.isFullCube()) {
			this.enderman.setHeldBlockState(iblockstate);
			world.setBlockToAir(blockpos);
		}
	}
}
