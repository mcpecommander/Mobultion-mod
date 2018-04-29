package mcpecommander.mobultion.entity.entityAI.skeletonsAI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIForestSkeletonMoveToTree extends EntityAIBase{
	private EntityLiving entity;
	private int radius;
	World world;
	private BlockPos target;
	private double speed;
	private BlockPos lastPos;
	private int count;
	private List<BlockPos> tree = null;
	
	public EntityAIForestSkeletonMoveToTree(EntityLiving entity, int radius, double speed) {
		this.entity = entity;
		this.world = entity.world;
		this.radius = radius > 50 ? 50 : radius;
		this.speed = speed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity != null && !this.entity.isDead){
			return world.canBlockSeeSky(this.entity.getPosition()) && world.isDaytime() && this.entity.getAttackTarget() == null;
		}else return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(!world.canBlockSeeSky(this.entity.getPosition())){
			return false;
		}else return this.entity != null && !this.entity.isDead;
	}
	
	@Override
	public void updateTask() {
		if(this.entity != null && !this.entity.isDead){
			this.world.profiler.startSection("task update");
			if (this.entity.getNavigator().noPath()) {
				count++;
				if (this.lastPos != this.entity.getPosition() && count > 4) {
					 tree = EntityAIForestSkeletonMoveToTree.getAllInBox(entity.getPosition().add(-radius, -3, -radius),
							entity.getPosition().add(radius, 3, radius), world);
					 this.lastPos = this.entity.getPosition();
				}
				if (tree != null && this.count > 8) {
					tree.sort(new EntityAIForestSkeletonMoveToTree.Sorter(this.entity.getPosition()));
					if(tree.isEmpty()){
						return;
					}
					if (tree.get(0) != null) {
						this.getSpotPathPoint(tree.get(0));
						count = 0;
					}
				}
			}	
			this.world.profiler.endSection();
		}
	}
	
	private boolean getSpotPathPoint(BlockPos TreeBlock){
		if(TreeBlock != null){
			List<BlockPos> blocks = Lists.newArrayList(BlockPos.getAllInBox(TreeBlock.add(-1, -1, -1), TreeBlock.add(1, 1, 1)));
			//System.out.println(blocks);
			blocks = this.filter(blocks);
			//System.out.println(blocks);
			blocks.sort(new EntityAIForestSkeletonMoveToTree.Sorter(this.entity.getPosition()));
			//System.out.println(blocks);
			for(BlockPos block : blocks){
				boolean flag = this.entity.getNavigator().tryMoveToXYZ(block.getX(), block.getY(), block.getZ(), speed);
				if (!flag) {
					continue;
				} else{
					//System.out.println(block);
					return flag;
				}
			}
		}
		return false;
	}
	
	private static boolean checkForLeaves(BlockPos pos, int amount, World world){
		int i = 0;
		for(BlockPos block : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1))){
			IBlockState state = world.getBlockState(block);
			if(state.getBlock() instanceof BlockLeaves){
				//System.out.println(i);
				i++;
			}
		}
		return i >= amount;
	}
	
	private List<BlockPos> filter(List<BlockPos> blocks){
		List<BlockPos> list = new ArrayList<BlockPos>();
		for(BlockPos block : blocks){
			if(!world.canBlockSeeSky(block)){
				if((world.isAirBlock(block.add(0, 1, 0)) || world.getBlockState(block.add(0, 1, 0)).getBlock().getCollisionBoundingBox( world.getBlockState(block.add(0, 1, 0)), world, block.add(0, 1, 0)) == null)
						&& (world.isAirBlock(block) || world.getBlockState(block).getBlock().getCollisionBoundingBox( world.getBlockState(block), world, block) == null)){
					list.add(block);
				}
			}
		}
		return list;
	}
	
	public static List<BlockPos> getAllInBox(BlockPos from, BlockPos to, @Nonnull World world)
    {
        return getAllInBox(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()), world);
    }
	
	private static List<BlockPos> getAllInBox(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ, @Nonnull World world)
    {
		ArrayList<BlockPos> array = new ArrayList<>();
        for(int i = minX; i <= maxX; i++){
			for (int k = minZ; k <= maxZ; k++) {
				for (int j = minY; j <= maxY; j++) {
					BlockPos block = new BlockPos(i, j, k);
					if (world.getBlockState(block).getBlock().isWood(world, block)) {
						int count = 1;
						BlockPos neighbourBlock;
						do {
							neighbourBlock = new BlockPos(block.getX(), block.getY() + count, block.getZ());
							count++;
						} while ((world.getBlockState(neighbourBlock)
								.getBlock() == (world.getBlockState(block).getBlock())
								&& (world.getBlockState(neighbourBlock).getBlock().getDefaultState() == world
										.getBlockState(block).getBlock().getDefaultState()))
								&& count < 5);
						if (count >= 5) {
							if (checkForLeaves(neighbourBlock, 4, world)) {
								array.add(block);
							}
						}
					}
				}
			}
		}
		return array;
	}
	
	/**
	 * Not used, TODO: maybe someday.....
	 */
	public static void Spiral(int X, int Z) {
	    int x=0, z=0, dx = 0, dz = -1;
	    int t = Math.max(X,Z);
	    int maxI = t*t;

	    for (int i=0; i < maxI; i++){
	        if ((-X/2 <= x) && (x <= X/2) && (-Z/2 <= z) && (z <= Z/2)) {
	            System.out.println(x+","+z);
	            //DO STUFF
	        }

	        if( (x == z) || ((x < 0) && (x == -z)) || ((x > 0) && (x == 1-z))) {
	            t=dx; dx=-dz; dz=t;
	        }   
	        x+=dx; z+=dz;
	    }
	}
	
	public static class Sorter implements Comparator<BlockPos>
    {
        private final BlockPos pos;

        public Sorter(BlockPos pos)
        {
            this.pos = pos;
        }

        @Override
        public int compare(BlockPos pos1, BlockPos pos2)
        {
        	//System.out.println(this.pos);
            double h0 = this.pos.distanceSq(pos1.getX(), pos1.getY(), pos1.getZ());
            double h1 = this.pos.distanceSq(pos2.getX(), pos2.getY(), pos2.getZ());
            //System.out.println(h0);
            //System.out.println(h1);
            
            if (h1 > h0)
            {
                return -1;
            }
            else
            {
                return h1 < h0 ? 1 : 0;
            }
        }
    }
}
