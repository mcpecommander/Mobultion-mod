package mcpecommander.luggagemod.fluid.fluidBlock;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.fluid.FluidMoltenCheese;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MoltenCheeseBlock extends BlockFluidClassic{
	public static Fluid MoltenCheese = new FluidMoltenCheese();

	public MoltenCheeseBlock() {
		super(new FluidMoltenCheese(), Material.LAVA);
		this.setUnlocalizedName(Reference.LuggageModBlocks.MOLTENCHEESE.getUnlocalizedName());
		this.setRegistryName(Reference.LuggageModBlocks.MOLTENCHEESE.getRegistryName());
		this.setCreativeTab(LuggageMod.LUGGAGEMOD_TAB);
		this.setFluidStack(new FluidStack(MoltenCheese, 1000));

	}
	
	@SideOnly(Side.CLIENT)
	public void initModel(){
		ModelBakery.registerItemVariants(new ItemBlock(this));
		final ModelResourceLocation loc = new ModelResourceLocation("mlm:fluid_block_molten_cheese", "molten_cheese");
		ModelLoader.setCustomMeshDefinition(new ItemBlock(this), new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return loc;
			}
		});
		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return loc;
			}
		});
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getBlockState().getBaseState().withProperty(LEVEL, meta);
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}	
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }


}
