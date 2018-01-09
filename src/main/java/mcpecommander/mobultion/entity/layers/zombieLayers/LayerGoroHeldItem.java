package mcpecommander.mobultion.entity.layers.zombieLayers;

import java.nio.FloatBuffer;

import javax.vecmath.Quat4f;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.util.MathHelper;

import mcpecommander.mobultion.entity.entities.zombies.EntityDoctorZombie;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerGoroHeldItem implements LayerRenderer<EntityLivingBase>
{
    protected final RenderLivingBase<?> livingEntityRenderer;

    public LayerGoroHeldItem(RenderLivingBase<?> livingEntityRendererIn)
    {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
        ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
        ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();
        if (!itemstack.isEmpty() || !itemstack1.isEmpty())
        {
            GlStateManager.pushMatrix();

            if (this.livingEntityRenderer.getMainModel().isChild)
            {
                float f = 0.5F;
                GlStateManager.translate(0.0F, 0.75F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.popMatrix();
        }
    }

	private void renderHeldItem(EntityLivingBase entity, ItemStack heldItemStack,
			ItemCameraTransforms.TransformType cameraTransform, EnumHandSide handSide) {
		boolean flag = handSide == EnumHandSide.LEFT;
		if (!heldItemStack.isEmpty()) {
			GlStateManager.pushMatrix();
			if (flag) {
				this.translateToLeftHand();
			} else {
				this.translateToHand();
			}
			if(flag && !(entity instanceof EntityDoctorZombie)){
				GlStateManager.rotate(-180f, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0, -0.45f, -0.2);
			}else{
				GlStateManager.rotate(-90f, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			}

			GlStateManager.translate(0, 0.1, -0.165F);

			Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, heldItemStack, cameraTransform, false);
			GlStateManager.popMatrix();
			
			//Second arm
			GlStateManager.pushMatrix();
			if (flag) {
				this.translateToLeftHand1();
			} else {
				this.translateToHand1();
			}
			if(flag && !(entity instanceof EntityDoctorZombie)){
				GlStateManager.rotate(-180f, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0, -0.45f, -0.2);
			}else{
				GlStateManager.rotate(70f, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0, 0, 0.15f);
			}

			GlStateManager.translate(0, 0.1, -0.165F);

			Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, heldItemStack, cameraTransform, false);
			GlStateManager.popMatrix();
		}
	}

    protected void translateToHand()
	{
		CSModelRenderer RightArm = null;
		CSModelRenderer RightArm1 = null;
		if (((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
				.getModelRendererFromName("RightArm") != null) {
			RightArm = ((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
					.getModelRendererFromName("RightArm");
		}
		if (((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
				.getModelRendererFromName("RightArm1") != null) {
			RightArm1 = ((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
					.getModelRendererFromName("RightArm1");
		}
		if (RightArm != null) {
			Quat4f q = new Quat4f();
			RightArm.getRotationMatrix().get(q);
			float z = (float) Math.atan2(2 * q.y * q.w - 2 * q.x * q.z, 1 - 2 * q.y * q.y - 2 * q.z * q.z);
			float x = (float) Math.atan2(2 * q.x * q.w - 2 * q.y * q.z, 1 - 2 * q.x * q.x - 2 * q.z * q.z);
			float y = (float) Math.asin(2 * q.x * q.y + 2 * q.z * q.w);
			Quat4f q1 = new Quat4f();
			RightArm1.getRotationMatrix().get(q1);
			float z1 = (float) Math.atan2(2 * q1.y * q1.w - 2 * q1.x * q1.z, 1 - 2 * q1.y * q1.y - 2 * q1.z * q1.z);
			float x1 = (float) Math.atan2(2 * q1.x * q1.w - 2 * q1.y * q1.z, 1 - 2 * q1.x * q1.x - 2 * q1.z * q1.z);
			float y1 = (float) Math.asin(2 * q1.x * q1.y + 2 * q1.z * q1.w);
			if (!RightArm.isHidden) {
				if (RightArm.showModel) {
					boolean compiled = false;
					try {
						java.lang.reflect.Field field = RightArm.getClass().getDeclaredField("compiled");
						field.setAccessible(true);
						compiled = (Boolean) field.get(RightArm);
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException e) {
						e.printStackTrace();
					}
					if (!compiled) {
						RightArm.compileDisplayList(0.0625F);
					}
					GlStateManager.translate(RightArm.rotationPointX * 0.0625f, RightArm.rotationPointY * 0.0625f,
							RightArm.rotationPointZ * 0.0625f);

					FloatBuffer buf = MathHelper.makeFloatBuffer(RightArm.getRotationMatrix());
					GlStateManager.multMatrix(buf);
					GlStateManager.translate(RightArm.offsetX * 0.0625f, RightArm.offsetY * 0.0625f,
							RightArm.offsetZ * 0.0625f);

					GlStateManager.translate(RightArm1.rotationPointX * 0.0625f, RightArm1.rotationPointY * 0.0625f,
							RightArm1.rotationPointZ * 0.0625f);
					FloatBuffer buf2 = MathHelper.makeFloatBuffer(RightArm1.getRotationMatrix());
					GlStateManager.multMatrix(buf2);
					GlStateManager.translate(RightArm1.offsetX * 0.0625f, RightArm1.offsetY * 0.0625f,
							RightArm1.offsetZ * 0.0625f);
				}
			}
		}

	}
    
    protected void translateToHand1()
	{
		CSModelRenderer RightArm = null;
		CSModelRenderer RightArm1 = null;
		if (((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
				.getModelRendererFromName("RightArmA") != null) {
			RightArm = ((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
					.getModelRendererFromName("RightArmA");
		}
		if (((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
				.getModelRendererFromName("RightArmA1") != null) {
			RightArm1 = ((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
					.getModelRendererFromName("RightArmA1");
		}
		if (RightArm != null) {
			Quat4f q = new Quat4f();
			RightArm.getRotationMatrix().get(q);
			float z = (float) Math.atan2(2 * q.y * q.w - 2 * q.x * q.z, 1 - 2 * q.y * q.y - 2 * q.z * q.z);
			float x = (float) Math.atan2(2 * q.x * q.w - 2 * q.y * q.z, 1 - 2 * q.x * q.x - 2 * q.z * q.z);
			float y = (float) Math.asin(2 * q.x * q.y + 2 * q.z * q.w);
			Quat4f q1 = new Quat4f();
			RightArm1.getRotationMatrix().get(q1);
			float z1 = (float) Math.atan2(2 * q1.y * q1.w - 2 * q1.x * q1.z, 1 - 2 * q1.y * q1.y - 2 * q1.z * q1.z);
			float x1 = (float) Math.atan2(2 * q1.x * q1.w - 2 * q1.y * q1.z, 1 - 2 * q1.x * q1.x - 2 * q1.z * q1.z);
			float y1 = (float) Math.asin(2 * q1.x * q1.y + 2 * q1.z * q1.w);
			if (!RightArm.isHidden) {
				if (RightArm.showModel) {
					boolean compiled = false;
					try {
						java.lang.reflect.Field field = RightArm.getClass().getDeclaredField("compiled");
						field.setAccessible(true);
						compiled = (Boolean) field.get(RightArm);
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException e) {
						e.printStackTrace();
					}
					if (!compiled) {
						RightArm.compileDisplayList(0.0625F);
					}
					GlStateManager.translate(RightArm.rotationPointX * 0.0625f, RightArm.rotationPointY * 0.0625f,
							RightArm.rotationPointZ * 0.0625f);

					FloatBuffer buf = MathHelper.makeFloatBuffer(RightArm.getRotationMatrix());
					GlStateManager.multMatrix(buf);
					GlStateManager.translate(RightArm.offsetX * 0.0625f, RightArm.offsetY * 0.0625f,
							RightArm.offsetZ * 0.0625f);

					GlStateManager.translate(RightArm1.rotationPointX * 0.0625f, RightArm1.rotationPointY * 0.0625f,
							RightArm1.rotationPointZ * 0.0625f);
					FloatBuffer buf2 = MathHelper.makeFloatBuffer(RightArm1.getRotationMatrix());
					GlStateManager.multMatrix(buf2);
					GlStateManager.translate(RightArm1.offsetX * 0.0625f, RightArm1.offsetY * 0.0625f,
							RightArm1.offsetZ * 0.0625f);
				}
			}
		}

	}
    
    protected void translateToLeftHand()
    {
    	CSModelRenderer LeftArm = null;
		CSModelRenderer LeftArm1 = null;
		if (((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
				.getModelRendererFromName("LeftArm") != null) {
			LeftArm = ((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
					.getModelRendererFromName("LeftArm");
		}
		if (((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
				.getModelRendererFromName("LeftArm1") != null) {
			LeftArm1 = ((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
					.getModelRendererFromName("LeftArm1");
		}
		if (LeftArm != null) {
			Quat4f q = new Quat4f();
			LeftArm.getRotationMatrix().get(q);
			float z = (float) Math.atan2(2 * q.y * q.w - 2 * q.x * q.z, 1 - 2 * q.y * q.y - 2 * q.z * q.z);
			float x = (float) Math.atan2(2 * q.x * q.w - 2 * q.y * q.z, 1 - 2 * q.x * q.x - 2 * q.z * q.z);
			float y = (float) Math.asin(2 * q.x * q.y + 2 * q.z * q.w);
			Quat4f q1 = new Quat4f();
			LeftArm1.getRotationMatrix().get(q1);
			float z1 = (float) Math.atan2(2 * q1.y * q1.w - 2 * q1.x * q1.z, 1 - 2 * q1.y * q1.y - 2 * q1.z * q1.z);
			float x1 = (float) Math.atan2(2 * q1.x * q1.w - 2 * q1.y * q1.z, 1 - 2 * q1.x * q1.x - 2 * q1.z * q1.z);
			float y1 = (float) Math.asin(2 * q1.x * q1.y + 2 * q1.z * q1.w);
			if (!LeftArm.isHidden) {
				if (LeftArm.showModel) {
					boolean compiled = false;
					try {
						java.lang.reflect.Field field = LeftArm.getClass().getDeclaredField("compiled");
						field.setAccessible(true);
						compiled = (Boolean) field.get(LeftArm);
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException e) {
						e.printStackTrace();
					}
					if (!compiled) {
						LeftArm.compileDisplayList(0.0625F);
					}
					GlStateManager.translate(LeftArm.rotationPointX * 0.0625f, LeftArm.rotationPointY * 0.0625f,
							LeftArm.rotationPointZ * 0.0625f);

					FloatBuffer buf = MathHelper.makeFloatBuffer(LeftArm.getRotationMatrix());
					GlStateManager.multMatrix(buf);
					GlStateManager.translate(LeftArm.offsetX * 0.0625f, LeftArm.offsetY * 0.0625f,
							LeftArm.offsetZ * 0.0625f);

					GlStateManager.translate(LeftArm1.rotationPointX * 0.0625f, LeftArm1.rotationPointY * 0.0625f,
							LeftArm1.rotationPointZ * 0.0625f);
					FloatBuffer buf2 = MathHelper.makeFloatBuffer(LeftArm1.getRotationMatrix());
					GlStateManager.multMatrix(buf2);
					GlStateManager.translate(LeftArm1.offsetX * 0.0625f, LeftArm1.offsetY * 0.0625f,
							LeftArm1.offsetZ * 0.0625f);
				}
			}
		}
    } 
    
    protected void translateToLeftHand1()
    {
    	CSModelRenderer LeftArm = null;
		CSModelRenderer LeftArm1 = null;
		if (((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
				.getModelRendererFromName("LeftArmA") != null) {
			LeftArm = ((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
					.getModelRendererFromName("LeftArmA");
		}
		if (((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
				.getModelRendererFromName("LeftArmA1") != null) {
			LeftArm1 = ((ModelCraftStudioSon) this.livingEntityRenderer.getMainModel())
					.getModelRendererFromName("LeftArmA1");
		}
		if (LeftArm != null) {
			Quat4f q = new Quat4f();
			LeftArm.getRotationMatrix().get(q);
			float z = (float) Math.atan2(2 * q.y * q.w - 2 * q.x * q.z, 1 - 2 * q.y * q.y - 2 * q.z * q.z);
			float x = (float) Math.atan2(2 * q.x * q.w - 2 * q.y * q.z, 1 - 2 * q.x * q.x - 2 * q.z * q.z);
			float y = (float) Math.asin(2 * q.x * q.y + 2 * q.z * q.w);
			Quat4f q1 = new Quat4f();
			LeftArm1.getRotationMatrix().get(q1);
			float z1 = (float) Math.atan2(2 * q1.y * q1.w - 2 * q1.x * q1.z, 1 - 2 * q1.y * q1.y - 2 * q1.z * q1.z);
			float x1 = (float) Math.atan2(2 * q1.x * q1.w - 2 * q1.y * q1.z, 1 - 2 * q1.x * q1.x - 2 * q1.z * q1.z);
			float y1 = (float) Math.asin(2 * q1.x * q1.y + 2 * q1.z * q1.w);
			if (!LeftArm.isHidden) {
				if (LeftArm.showModel) {
					boolean compiled = false;
					try {
						java.lang.reflect.Field field = LeftArm.getClass().getDeclaredField("compiled");
						field.setAccessible(true);
						compiled = (Boolean) field.get(LeftArm);
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException e) {
						e.printStackTrace();
					}
					if (!compiled) {
						LeftArm.compileDisplayList(0.0625F);
					}
					GlStateManager.translate(LeftArm.rotationPointX * 0.0625f, LeftArm.rotationPointY * 0.0625f,
							LeftArm.rotationPointZ * 0.0625f);

					FloatBuffer buf = MathHelper.makeFloatBuffer(LeftArm.getRotationMatrix());
					GlStateManager.multMatrix(buf);
					GlStateManager.translate(LeftArm.offsetX * 0.0625f, LeftArm.offsetY * 0.0625f,
							LeftArm.offsetZ * 0.0625f);

					GlStateManager.translate(LeftArm1.rotationPointX * 0.0625f, LeftArm1.rotationPointY * 0.0625f,
							LeftArm1.rotationPointZ * 0.0625f);
					FloatBuffer buf2 = MathHelper.makeFloatBuffer(LeftArm1.getRotationMatrix());
					GlStateManager.multMatrix(buf2);
					GlStateManager.translate(LeftArm1.offsetX * 0.0625f, LeftArm1.offsetY * 0.0625f,
							LeftArm1.offsetZ * 0.0625f);
				}
			}
		}
    } 

    public boolean shouldCombineTextures()
    {
        return false;
    }
}