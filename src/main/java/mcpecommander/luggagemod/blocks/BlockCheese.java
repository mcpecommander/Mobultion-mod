package mcpecommander.luggagemod.blocks;

import java.util.Random;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.particle.ConfuseCloudParticle;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCheese extends Block {

	public BlockCheese() {
		super(Material.CAKE);
		setUnlocalizedName(Reference.LuggageModBlocks.CHEESE.getUnlocalizedName());
		setRegistryName(Reference.LuggageModBlocks.CHEESE.getRegistryName());
		
		this.setCreativeTab(LuggageMod.LUGGAGEMOD_TAB);
		
		
	}

	
	@Override
	  public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
	    return EnumBlockRenderType.MODEL;
	  }
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
	

	

	
}
