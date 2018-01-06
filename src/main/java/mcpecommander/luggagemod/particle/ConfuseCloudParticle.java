package mcpecommander.luggagemod.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ConfuseCloudParticle extends Particle{
	private final ResourceLocation confuse = new ResourceLocation("mlm:entity/confuse_particle");

	public ConfuseCloudParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float scale) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		
		particleGravity = Blocks.PURPLE_GLAZED_TERRACOTTA.blockParticleGravity;
		this.particleAlpha = 0.99f;
		
		motionX = xSpeedIn;
	    motionY = ySpeedIn;
	    motionZ = zSpeedIn;
	    this.setRBGColorF(226f/255f, 16f/255f, 181f/255f);
	    this.particleScale = scale;
	    
	    TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(confuse.toString());
	    setParticleTexture(sprite);
	}
	
	@Override
	public int getFXLayer() {
		return 1;
	}
	
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		move(motionX, motionY, motionZ);

		if (onGround) {
			this.setExpired();
		}

		if (prevPosY == posY && motionY > 0) {
			this.setExpired();
		}

		if (this.particleMaxAge-- <= 0) {
			this.setExpired();
		}
	}
	
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	  {
	    double minU = this.particleTexture.getMinU();
	    double maxU = this.particleTexture.getMaxU();
	    double minV = this.particleTexture.getMinV();
	    double maxV = this.particleTexture.getMaxV();

	    double scale = 0.1F * this.particleScale;
	    final double scaleLR = scale;
	    final double scaleUD = scale;
	    double x = this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX;
	    double y = this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY;
	    double z = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ;
	    int combinedBrightness = this.getBrightnessForRender(partialTicks);
	    int skyLightTimes16 = combinedBrightness >> 16 & 65535;
	    int blockLightTimes16 = combinedBrightness & 65535;

	    buffer.pos(x - rotationX * scaleLR - rotationXY * scaleUD,
	            y - rotationZ * scaleUD,
	            z - rotationYZ * scaleLR - rotationXZ * scaleUD)
	                 .tex(maxU, maxV)
	                 .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
	                 .lightmap(skyLightTimes16, blockLightTimes16)
	                 .endVertex();
	    buffer.pos(x - rotationX * scaleLR + rotationXY * scaleUD,
	            y + rotationZ * scaleUD,
	            z - rotationYZ * scaleLR + rotationXZ * scaleUD)
	            .tex(maxU, minV)
	            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
	            .lightmap(skyLightTimes16, blockLightTimes16)
	            .endVertex();
	    buffer.pos(x + rotationX * scaleLR + rotationXY * scaleUD,
	            y + rotationZ * scaleUD,
	            z + rotationYZ * scaleLR + rotationXZ * scaleUD)
	            .tex(minU, minV)
	            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
	            .lightmap(skyLightTimes16, blockLightTimes16)
	            .endVertex();
	    buffer.pos(x + rotationX * scaleLR - rotationXY * scaleUD,
	            y - rotationZ * scaleUD,
	            z + rotationYZ * scaleLR - rotationXZ * scaleUD)
	            .tex(minU, maxV)
	            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
	            .lightmap(skyLightTimes16, blockLightTimes16)
	            .endVertex();

	  }
}
