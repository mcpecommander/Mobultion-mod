package mcpecommander.mobultion.entity.model;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;

import net.minecraft.client.model.ModelRenderer;

public class ModelCraftStudioSon extends ModelCraftStudio{

	public ModelCraftStudioSon(String modid, String modelNameIn, int textureWidth, int textureHeight) {
		super(modid, modelNameIn, textureWidth, textureHeight);
	}
	
	@Override
	public CSModelRenderer getModelRendererFromName(String name) {
        CSModelRenderer result;
        for (CSModelRenderer parent : this.getParentBlocks()) {
            result = this.getModelRendererFromNameAndBlocks(name, parent);
            if (result != null)
                return result;
        }
        return null;
    }
	
	public static CSModelRenderer getModelRendererFromNameAndBlocks(String name, CSModelRenderer block) {
        CSModelRenderer childModel, result;

        if (block.boxName.equals(name))
            return block;
        if(block.childModels != null && !block.childModels.isEmpty()){
	        for (ModelRenderer child : block.childModels)
	            if (child instanceof CSModelRenderer) {
	                childModel = (CSModelRenderer) child;
	                result = getModelRendererFromNameAndBlocks(name, childModel);
	                if (result != null)
	                    return result;
	            }
        }

        return null;
    }
}
