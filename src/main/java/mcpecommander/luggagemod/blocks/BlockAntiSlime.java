  package mcpecommander.luggagemod.blocks;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAntiSlime extends Block{

	public BlockAntiSlime() {
		super(Material.CLAY, MapColor.GREEN_STAINED_HARDENED_CLAY);
		this.setCreativeTab(LuggageMod.LUGGAGEMOD_TAB);
		setUnlocalizedName(Reference.LuggageModBlocks.ANTISLIME.getUnlocalizedName());
		setRegistryName(Reference.LuggageModBlocks.ANTISLIME.getRegistryName());
	}
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
	
	@Override
	  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
	    return EnumBlockRenderType.MODEL;
	  }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if (entityIn.isSneaking())
        {
        	entityIn.fall(fallDistance, 0.0F);
        }
        else
        {
        	super.onFallenUpon(worldIn, pos, entityIn, fallDistance);            
        }
    }
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {           
            entityIn.motionX *= 0.8d;
            entityIn.motionZ *= 0.8d;        
    }
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
