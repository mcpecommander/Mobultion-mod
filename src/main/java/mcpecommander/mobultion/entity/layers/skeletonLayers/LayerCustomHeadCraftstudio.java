package mcpecommander.mobultion.entity.layers.skeletonLayers;

import java.nio.FloatBuffer;
import java.util.UUID;

import javax.vecmath.Quat4f;

import org.apache.commons.lang3.StringUtils;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.client.util.MathHelper;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;

public class LayerCustomHeadCraftstudio implements LayerRenderer<EntityLivingBase>
{
    private final CSModelRenderer head;

    public LayerCustomHeadCraftstudio(CSModelRenderer head)
    {
        this.head = head;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	//System.out.println(entitylivingbaseIn);
        ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

        if (!itemstack.isEmpty())
        {
            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            this.translateToHead(entitylivingbaseIn);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (item == Items.SKULL)
            {
                float f2 = 1.1875F;
                GlStateManager.scale(1.1875F, -1.1875F, -1.1875F);

                GameProfile gameprofile = null;

                if (itemstack.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = itemstack.getTagCompound();
                    if (nbttagcompound.hasKey("SkullOwner", 10))
                    {
                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", 8))
                    {
                        String s = nbttagcompound.getString("SkullOwner");

                        if (!StringUtils.isBlank(s))
                        {
                            gameprofile = TileEntitySkull.updateGameprofile(new GameProfile((UUID)null, s));
                            nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                        }
                    }
                }

                TileEntitySkullRenderer.instance.renderSkull(-0.5F, -0.25F, -0.5F, EnumFacing.UP, 180.0F, itemstack.getMetadata(), gameprofile, -1, limbSwing);
                
            }else if (!(item instanceof ItemArmor) || ((ItemArmor)item).getEquipmentSlot() != EntityEquipmentSlot.HEAD)
            {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.scale(0.625F, -0.625F, -0.625F);
                
                minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.HEAD);
            }

            GlStateManager.popMatrix();
        }
    }
    
    private void translateToHead(EntityLivingBase entitylivingbaseIn){
        float diff = entitylivingbaseIn.getRotationYawHead() - entitylivingbaseIn.renderYawOffset;
        float pitch = entitylivingbaseIn.rotationPitch;
		//System.out.println(entitylivingbaseIn.getRotationYawHead() + " " + entitylivingbaseIn.renderYawOffset);
    	if (!head.isHidden)
        {
            if (head.showModel)
            {
            	boolean compiled = false;
            	try {
            	    java.lang.reflect.Field field = head.getClass().getDeclaredField("compiled");
            	    field.setAccessible(true);
            	    compiled = (Boolean) field.get(head);
            	} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            	    e.printStackTrace();
            	}
                if(!compiled){
                	head.compileDisplayList(0.0625F);
                }
                if (diff == 0.0F && pitch == 0.0F )
                {
                    if (head.rotationPointX != 0.0F || head.rotationPointY != 0.0F || head.rotationPointZ != 0.0F)
                    {
                    	//System.out.println(RightArm.rotateAngleX + " " + RightArm.rotateAngleY + " " + RightArm.rotateAngleZ);
                        GlStateManager.translate(head.rotationPointX * 0.0625f, head.rotationPointY * 0.0625f, head.rotationPointZ * 0.0625f);
                    }
                }
                else
                {

                    GlStateManager.translate(head.rotationPointX * 0.0625f, head.rotationPointY * 0.0625f, head.rotationPointZ * 0.0625f);
                    //System.out.println(x + " " + y + " " + z);
                    FloatBuffer buf = MathHelper.makeFloatBuffer(head.getRotationMatrix());
                    GlStateManager.multMatrix(buf);
//                    if (diff != 0.0F)
//                    {
//                    	//System.out.println("this is z " + z + ", " + Math.toDegrees(z));
//                        GlStateManager.rotate(diff , 0.0F, 1.0F, 0.0F);
//                    }
//                    if (pitch != 0.0F)
//                    {
//                    	//System.out.println("this is y " + y + ", " + Math.toDegrees(y));
//                        GlStateManager.rotate(pitch , 1.0F, 0.0F, 0.0F);
//                    }

//                    if (x != 0.0F)
//                    {
//                    	System.out.println("this is x " + x + ", " + Math.toDegrees(x));
//                        GlStateManager.rotate(-x  * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
//                    }
                }
            }
        }
	}
    
    /**
     * For testing reasons, not used and does not do any calculations.
     */
    private static void toEulerAngle(Quat4f q)
    {
    	// roll (x-axis rotation)
    	double sinr = +2.0 * (q.w * q.x + q.y * q.z);
    	double cosr = +1.0 - 2.0 * (q.x * q.x + q.y * q.y);
    	System.out.println( Math.atan2(sinr, cosr));

    	// pitch (y-axis rotation)
    	double sinp = +2.0 * (q.w * q.y - q.z * q.x);
    	if (Math.abs(sinp) >= 1)
    		System.out.println(Math.copySign(Math.PI / 2, sinp)); // use 90 degrees if out of range
    	else
    		System.out.println(Math.asin(sinp));

    	// yaw (z-axis rotation)
    	double siny = +2.0 * (q.w * q.z + q.x * q.y);
    	double cosy = +1.0 - 2.0 * (q.y * q.y + q.z * q.z);  
    	System.out.println( Math.atan2(siny, cosy));
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}