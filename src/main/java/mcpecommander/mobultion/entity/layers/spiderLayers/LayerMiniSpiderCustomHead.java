package mcpecommander.mobultion.entity.layers.spiderLayers;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.mojang.authlib.GameProfile;

import mcpecommander.mobultion.entity.entities.spiders.EntityMiniSpider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;

public class LayerMiniSpiderCustomHead implements LayerRenderer<EntityMiniSpider>
{
    private final CSModelRenderer head;

    public LayerMiniSpiderCustomHead(CSModelRenderer head)
    {
        this.head = head;
    }

    @Override
    public void doRenderLayer(EntityMiniSpider entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
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
                GlStateManager.scale(0.55F, -0.55F, -0.55F);

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
    
    private void translateToHead(EntityMiniSpider entitylivingbaseIn){
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
                	//The Goddamn rotation order matters, Always rotate the yaw first then the pitch.
                    GlStateManager.translate(head.rotationPointX * 0.0625f, head.rotationPointY * 0.0625f, head.rotationPointZ * 0.0625f);
                    //System.out.println(x + " " + y + " " + z);
                    if (diff != 0.0F)
                    {
                    	//System.out.println("this is z " + z + ", " + Math.toDegrees(z));
                        GlStateManager.rotate(diff , 0.0F, 1.0F, 0.0F);
                    }
                    if (pitch != 0.0F)
                    {
                    	//System.out.println("this is y " + y + ", " + Math.toDegrees(y));
                        GlStateManager.rotate(pitch , 1.0F, 0.0F, 0.0F);
                    }

//                    if (x != 0.0F)
//                    {
//                    	System.out.println("this is x " + x + ", " + Math.toDegrees(x));
//                        GlStateManager.rotate(-x  * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
//                    }
                }
            }
        }
	}

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}