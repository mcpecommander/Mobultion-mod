package mcpecommander.mobultion.entity.entityAI.zombiesAI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.BlockCake;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAICakeTarget extends EntityAIBase{

	private EntityLiving entity;
	World world;
	private double speed;
	private int coolDown = 0;
	
	public EntityAICakeTarget(EntityLiving entity, double speed) {
		this.entity = entity;
		this.world = entity.world;
		this.speed = speed;
		//this.sorter = new EntityAICakeTarget.Sorter(this.target);
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.entity != null && !this.entity.isDead && this.coolDown-- <= 0){
			for(BlockPos pos : this.entity.getPosition().getAllInBox(entity.getPosition().add(-2, -1, -2), entity.getPosition().add(2, 1, 2))){
				if(this.world.getBlockState(pos).getBlock() instanceof BlockCake){
					return this.entity.getNavigator().tryMoveToXYZ(pos.getX()-1, pos.getY(), pos.getZ(), this.speed);
				}
			}
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		this.coolDown = 50;
		super.startExecuting();
	}

	
//	public static class Sorter implements Comparator<BlockPos>
//    {
//        private final BlockPos pos;
//
//        public Sorter(BlockPos pos)
//        {
//            this.pos = pos;
//        }
//
//        public int compare(BlockPos p_compare_1_, BlockPos p_compare_2_)
//        {
//            double d0 = this.pos.distanceSq(p_compare_1_.getX(), p_compare_1_.getY(), p_compare_1_.getZ());
//            double d1 = this.pos.distanceSq(p_compare_2_.getX(), p_compare_2_.getY(), p_compare_2_.getZ());
//
//            if (d0 < d1)
//            {
//                return -1;
//            }
//            else
//            {
//                return d0 > d1 ? 1 : 0;
//            }
//        }
//    }

}
