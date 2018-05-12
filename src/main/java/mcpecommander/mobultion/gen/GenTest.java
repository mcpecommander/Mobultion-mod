package mcpecommander.mobultion.gen;

import java.util.Map.Entry;
import java.util.Random;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.spiders.EntityHypnoSpider;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;

public class GenTest implements IWorldGenerator{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if(!world.isRemote && random.nextInt(1000) == 0) {
			final BlockPos basePos = new BlockPos(chunkX * 16 + random.nextInt(16), world.getTopSolidOrLiquidBlock(new BlockPos(chunkX * 16, 2, chunkZ * 16)).getY(), chunkZ * 16 + random.nextInt(16));			
	        final PlacementSettings settings = new PlacementSettings().setRotation(Rotation.NONE).setIntegrity(0.9f);
	        final Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), new ResourceLocation(Reference.MOD_ID, "test"));
	        template.addBlocksToWorld(world, basePos, settings);
	        for(Entry<BlockPos, String> entry : template.getDataBlocks(basePos, settings).entrySet()) {
	        	if("hypno".equals(entry.getValue())) {
	        		EntityHypnoSpider spider = new EntityHypnoSpider(world, true);
	        		spider.setPosition(entry.getKey().getX() + 0.5, entry.getKey().getY() + 1, entry.getKey().getZ() + 0.5);
	        		world.setBlockState(entry.getKey(), Blocks.AIR.getDefaultState());
	        		world.spawnEntity(spider);
	        	}
	        }
	        
		}
		
	}
	
	public static void genStructure() {
		
	}
	
//	public static boolean checkPos(BlockPos pos, World world) {
//		world.get
//		
//	}

}
