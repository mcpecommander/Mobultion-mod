package mcpecommander.mobultion.entity.layers.zombieLayers;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.zombies.EntityAnimatedZombie;
import mcpecommander.mobultion.entity.entities.zombies.EntityWorkerZombie;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class LayerZombieArmor<T extends EntityAnimatedZombie> implements LayerRenderer<T>{
	
	private static final ResourceLocation RES = new ResourceLocation(Reference.MOD_ID, "textures/entity/iron_armor_body.png");
	private static final ResourceLocation RES1 = new ResourceLocation(Reference.MOD_ID, "textures/entity/builder_helmet.png");
	
	private RenderLivingBase renderer;
	private ModelCraftStudioSon model;
	
	public LayerZombieArmor(RenderLivingBase renderer) {
		this.renderer = renderer;
		model = new ModelCraftStudioSon(Reference.MOD_ID, "zombie", 64, 64);
	}

	@Override
	public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		GlStateManager.pushMatrix();
		//GlStateManager.scale(1.2d, 1d, 1.2d);
		if(entity.isChild()){
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
		}
		GlStateManager.translate(0, -.02, 0);
		//System.out.println(model.boxList);
		for(ModelRenderer model : model.boxList){
			model.showModel = false;
			((CSModelRenderer) model).setDefaultStretch(1.2f, 1f, 1.2f);
		}
		if(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.IRON_CHESTPLATE){
			model.getModelRendererFromName("Bottom").showModel = true;
			model.getModelRendererFromName("Body").showModel = true;
			model.getModelRendererFromName("RightArm").showModel = true;
			model.getModelRendererFromName("LeftArm").showModel = true;
		}
		if(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == Items.IRON_HELMET){
			model.getModelRendererFromName("Head").showModel = true;
		}
		if(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == Items.IRON_LEGGINGS){
			model.getModelRendererFromName("RightLeg").showModel = true;
			model.getModelRendererFromName("RightLeg1").showModel = true;
			model.getModelRendererFromName("LeftLeg").showModel = true;
			model.getModelRendererFromName("LeftLeg1").showModel = true;
		}
		
		model.getModelRendererFromName("Head").setRotationMatrix(((ModelCraftStudioSon) this.renderer.getMainModel()).getModelRendererFromName("Head").getRotationMatrix());
		model.getModelRendererFromName("Body").setRotationMatrix(((ModelCraftStudioSon) this.renderer.getMainModel()).getModelRendererFromName("Body").getRotationMatrix());
		model.getModelRendererFromName("RightArm").setRotationMatrix(((ModelCraftStudioSon) this.renderer.getMainModel()).getModelRendererFromName("RightArm").getRotationMatrix());
		model.getModelRendererFromName("LeftArm").setRotationMatrix(((ModelCraftStudioSon) this.renderer.getMainModel()).getModelRendererFromName("LeftArm").getRotationMatrix());
		model.getModelRendererFromName("RightLeg").setRotationMatrix(((ModelCraftStudioSon) this.renderer.getMainModel()).getModelRendererFromName("RightLeg").getRotationMatrix());
		model.getModelRendererFromName("LeftLeg").setRotationMatrix(((ModelCraftStudioSon) this.renderer.getMainModel()).getModelRendererFromName("LeftLeg").getRotationMatrix());
		model.getModelRendererFromName("RightLeg1").setRotationMatrix(((ModelCraftStudioSon) this.renderer.getMainModel()).getModelRendererFromName("RightLeg1").getRotationMatrix());
		model.getModelRendererFromName("LeftLeg1").setRotationMatrix(((ModelCraftStudioSon) this.renderer.getMainModel()).getModelRendererFromName("LeftLeg1").getRotationMatrix());
		this.renderer.bindTexture(entity instanceof EntityWorkerZombie ? RES1 : RES);
		GlStateManager.color(1f, 1f, 1f, 1f);
		model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
