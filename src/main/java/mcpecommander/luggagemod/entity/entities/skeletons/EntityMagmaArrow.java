package mcpecommander.luggagemod.entity.entities.skeletons;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMagmaArrow extends EntityArrow{
    private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityTippedArrow.class, DataSerializers.VARINT);

    public EntityMagmaArrow(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
        this.setFire(100);
    }
    
    public EntityMagmaArrow(World world){
    	super(world);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(COLOR, Integer.valueOf(16711680));
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.world.isRemote)
        {
            if (this.inGround)
            {
            	this.setFire(10);
                if (this.timeInGround % 5 == 0)
                {
                    this.spawnPotionParticles(1);
                }
            }
            else
            {
                this.spawnPotionParticles(2);
            }
        }
        else if (this.inGround && this.timeInGround != 0 && this.timeInGround >= 600)
        {
            this.world.setEntityState(this, (byte)0);
            this.dataManager.set(COLOR, Integer.valueOf(-1));
            this.extinguish();
        }
        if(this.isInWater() && this.world.getBlockState(this.getPosition()).getMaterial() == Material.WATER){
        	this.world.setBlockState(this.getPosition(), Blocks.COBBLESTONE.getDefaultState());
			this.setDead();
        }
    }

    private void spawnPotionParticles(int particleCount)
    {
        int i = this.getColor();

        if (i != -1 && particleCount > 0)
        {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i >> 0 & 255) / 255.0D;

            for (int j = 0; j < particleCount; ++j)
            {
                this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, d0, d1, d2);
            }
        }
    }
    
    @Override
	protected void onHit(RayTraceResult raytraceResultIn) {
		super.onHit(raytraceResultIn);
		if(raytraceResultIn.getBlockPos() != null){
			BlockPos blockpos = raytraceResultIn.getBlockPos();
			IBlockState blockState = this.world.getBlockState(blockpos);
			if(blockState.getBlock().isFlammable(this.world, raytraceResultIn.getBlockPos(), EnumFacing.getFacingFromVector((float)raytraceResultIn.hitVec.x, (float)raytraceResultIn.hitVec.y, (float)raytraceResultIn.hitVec.z))){
				for(BlockPos neighborblock : blockpos.getAllInBox(blockpos.add(1, 1, 1), blockpos.add(-1, 0, -1))){
					IBlockState iblockstate = world.getBlockState(neighborblock);
					if(iblockstate.getBlock().isAir(iblockstate, this.world, neighborblock)){
						this.world.setBlockState(neighborblock, Blocks.FIRE.getDefaultState(), 11);
						return;
					}
				}
			}else if(blockState.getBlock() instanceof BlockSand){
				this.world.setBlockState(blockpos, Blocks.GLASS.getDefaultState());
			}else if(blockState.getMaterial() == Material.WATER ){
				this.world.setBlockState(blockpos, Blocks.COBBLESTONE.getDefaultState());
				this.setDead();
			}
		}
	}



    public int getColor()
    {
        return ((Integer)this.dataManager.get(COLOR)).intValue();
    }

    protected ItemStack getArrowStack()
    {
    	return new ItemStack(Items.ARROW);
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 0)
        {
            int i = this.getColor();

            if (i != -1)
            {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i >> 0 & 255) / 255.0D;

                for (int j = 0; j < 20; ++j)
                {
                    this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, d0, d1, d2);
                }
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

}
