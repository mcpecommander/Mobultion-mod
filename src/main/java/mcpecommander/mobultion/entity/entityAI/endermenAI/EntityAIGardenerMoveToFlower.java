package mcpecommander.mobultion.entity.entityAI.endermenAI;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityGardenerEnderman;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class EntityAIGardenerMoveToFlower extends EntityAIMoveToBlock {

	private EntityGardenerEnderman gardener;
	private int penalty = 0;
	private byte task;
	private boolean flag = false;

	public EntityAIGardenerMoveToFlower(EntityGardenerEnderman gardener, double speedIn) {
		super(gardener, speedIn,
				(int) gardener.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue());
		this.gardener = gardener;
	}

	@Override
	public boolean shouldExecute() {
		if (penalty-- < 0) {
			if (!this.gardener.world.isAreaLoaded(this.gardener.getPosition(), 16)) {
				this.penalty = 100;
				return false;
			}
			this.task = 0;
			return super.shouldExecute();
		}
		return false;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.penalty = 0;
		this.task = 0;
		this.runDelay = 50;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.flag = false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		this.gardener.getLookHelper().setLookPosition(this.destinationBlock.getX(), this.destinationBlock.getY(),
				this.destinationBlock.getZ(), 10, 10);
		if (this.getIsAboveDestination()) {
			this.gardener.getNavigator().clearPath();
			if (this.task == 1 && !this.gardener.getAnimationHandler().isAnimationActive(Reference.MOD_ID,
					"gardener_water", this.gardener)) {
				if (this.flag) {
					ItemDye.applyBonemeal(new ItemStack(Items.DYE, 1, 15), this.gardener.world, this.destinationBlock);
					this.flag = false;
					this.resetTask();
					return;
				}
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands",
						this.gardener);
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this.gardener);
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this.gardener);
				this.gardener.getAnimationHandler().networkStartAnimation(Reference.MOD_ID, "gardener_water", 0,
						this.gardener, false);
				this.flag = true;
			}

			if (this.task == 2 && !this.gardener.getAnimationHandler().isAnimationActive(Reference.MOD_ID,
					"gardener_garden", this.gardener)) {
				if (this.flag) {
					this.gardener.world.setBlockState(destinationBlock, Blocks.GRASS.getDefaultState(), 2);
					this.flag = false;
					this.resetTask();
					return;
				}
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands",
						this.gardener);
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this.gardener);
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this.gardener);
				this.gardener.getAnimationHandler().networkStartAnimation(Reference.MOD_ID, "gardener_garden", 0,
						this.gardener, false);
				this.flag = true;
			}

			if (this.task == 3 && !this.gardener.getAnimationHandler().isAnimationActive(Reference.MOD_ID,
					"gardener_water", this.gardener)) {
				if (this.flag) {
					Block block = this.gardener.world.getBlockState(this.destinationBlock.up()).getBlock();
					if (block instanceof IPlantable) {
						ItemDye.applyBonemeal(new ItemStack(Items.DYE, 1, 15), this.gardener.world,
								this.destinationBlock.up());
					}
					this.flag = false;
					this.resetTask();
					return;
				}
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands",
						this.gardener);
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this.gardener);
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this.gardener);
				this.gardener.getAnimationHandler().networkStartAnimation(Reference.MOD_ID, "gardener_water", 0,
						this.gardener, false);
				this.flag = true;
			}

			if (this.task == 4 && !this.gardener.getAnimationHandler().isAnimationActive(Reference.MOD_ID,
					"gardener_water", this.gardener)) {
				if (this.flag) {
					this.gardener.world.setBlockState(destinationBlock,
							this.gardener.world.getBlockState(destinationBlock).withProperty(BlockFarmland.MOISTURE, 7),
							3);
					this.flag = false;
					this.resetTask();
					return;
				}
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands",
						this.gardener);
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this.gardener);
				this.gardener.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this.gardener);
				this.gardener.getAnimationHandler().networkStartAnimation(Reference.MOD_ID, "gardener_water", 0,
						this.gardener, false);
				this.flag = true;
			}

		}

	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.task == 0 ? false : super.shouldContinueExecuting();
	}

	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		if (block == Blocks.GRASS && !state.getValue(BlockGrass.SNOWY).booleanValue() && !hasCrops(worldIn, pos)
				&& canStand(worldIn, pos)) {
			this.task = 1;
			return true;
		}
		if (block == Blocks.DIRT && !state.getValue(BlockDirt.SNOWY).booleanValue()
				&& state.getValue(BlockDirt.VARIANT) == DirtType.DIRT && !hasCrops(worldIn, pos)
				&& canStand(worldIn, pos)) {
			this.task = 2;
			return true;
		}
		if (block == Blocks.FARMLAND && state.getValue(BlockFarmland.MOISTURE).intValue() > 0) {
			if (hasCrops(worldIn, pos)) {
				this.task = 3;
			} else {
				this.task = 4;
			}
			return true;
		}

		return false;
	}

	@Override
	protected boolean getIsAboveDestination() {
		return this.gardener.getDistanceSqToCenter(this.destinationBlock.up()) < 2.0D;
	}

	private boolean canStand(World world, BlockPos pos) {
		if (world.getBlockState(pos).isSideSolid(world, pos, EnumFacing.UP) && world.isAirBlock(pos.up())
				&& world.isAirBlock(pos.up(2))) {
			return true;
		}
		return false;
	}

	private boolean hasCrops(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos.up()).getBlock();
		return block instanceof IPlantable
				&& block.canSustainPlant(worldIn.getBlockState(pos), worldIn, pos, EnumFacing.UP, (IPlantable) block);
	}

}
