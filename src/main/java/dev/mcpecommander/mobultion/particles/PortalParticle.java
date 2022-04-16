package dev.mcpecommander.mobultion.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.mcpecommander.mobultion.setup.ClientSetup;
import dev.mcpecommander.mobultion.utils.MathCalculations;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Locale;

/* McpeCommander created on 09/07/2021 inside the package - dev.mcpecommander.mobultion.particles */
public class PortalParticle extends TextureSheetParticle {

    SpriteSet sprite;
    Vec3 finalPos;
    float oSize;

    protected PortalParticle(ClientLevel world, double posX, double posY, double posZ,
                           double speedX, double speedY, double speedZ,
                             PortalParticle.PortalParticleData data, SpriteSet sprite) {
        super(world, posX, posY, posZ, speedX, speedY, speedZ);
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.xd = speedX;
        this.yd = speedY;
        this.zd = speedZ;
        this.finalPos = new Vec3(data.getFinalX(), data.getFinalY(), data.getFinalZ());
        this.quadSize = 0.3f * (this.random.nextFloat() * 2f);
        this.oSize = quadSize;
        this.rCol = data.getRed();
        this.gCol = data.getGreen();
        this.bCol = data.getBlue();
        this.alpha = data.getAlpha();
        this.lifetime = this.random.nextInt(30) + 20;
        this.sprite = sprite;
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        //System.out.println(this.quadSize + " " + this.checkReached());
        this.pickSprite(sprite);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.quadSize = (float) (oSize * MathCalculations.map(age, 0f, lifetime, 1f, 0.3f));
            if(checkReached()) return;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 1.05F;
            this.yd *= 1.05F;
            this.zd *= 1.05F;

        }
    }

    private boolean checkReached(){
        return (this.x < finalPos.x + 0.1f && this.x > finalPos.x - 0.1f) &&
                (this.y < finalPos.y + 0.1f && this.y > finalPos.y - 0.1f) &&
                (this.z < finalPos.z + 0.1f && this.z > finalPos.z - 0.1f);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return alpha != 1.0f ? ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT : ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<PortalParticle.PortalParticleData> {

        private final SpriteSet sprite;

        public Factory(SpriteSet sprite){
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(PortalParticle.@NotNull PortalParticleData data, @NotNull ClientLevel world,
                                       double posX, double posY, double posZ,
                                       double speedX, double speedY, double speedZ) {
            PortalParticle particle = new PortalParticle(world, posX, posY, posZ, speedX, speedY, speedZ, data, sprite);
            particle.pickSprite(sprite);
            return particle;
        }
    }

    public static class PortalParticleData implements ParticleOptions {

        public static final ParticleOptions.Deserializer<PortalParticle.PortalParticleData> DESERIALIZER = new Deserializer<>() {
            @Override
            public PortalParticle.@NotNull PortalParticleData fromCommand(@NotNull ParticleType<PortalParticle.PortalParticleData> particleType, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float red = (float) reader.readDouble();
                reader.expect(' ');
                float green = (float) reader.readDouble();
                reader.expect(' ');
                float blue = (float) reader.readDouble();
                reader.expect(' ');
                float alpha = (float) reader.readDouble();
                reader.expect(' ');
                float finalX = (float) reader.readDouble();
                reader.expect(' ');
                float finalY = (float) reader.readDouble();
                reader.expect(' ');
                float finalZ = (float) reader.readDouble();
                return new PortalParticle.PortalParticleData(red, green, blue, alpha, finalX, finalY, finalZ);
            }

            @Override
            public PortalParticle.@NotNull PortalParticleData fromNetwork(@NotNull ParticleType<PortalParticle.PortalParticleData> particleType, FriendlyByteBuf buffer) {
                return new PortalParticle.PortalParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                        buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            }
        };

        public static final Codec<PortalParticle.PortalParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("red").forGetter(data -> data.red),
                Codec.FLOAT.fieldOf("green").forGetter(data -> data.green),
                Codec.FLOAT.fieldOf("blue").forGetter(data -> data.blue),
                Codec.FLOAT.fieldOf("alpha").forGetter(data -> data.alpha),
                Codec.FLOAT.fieldOf("finalX").forGetter(data -> data.finalX),
                Codec.FLOAT.fieldOf("finalY").forGetter(data -> data.finalY),
                Codec.FLOAT.fieldOf("finalZ").forGetter(data -> data.finalZ)
        ).apply(instance, PortalParticle.PortalParticleData::new));

        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;
        private final float finalX;
        private final float finalY;
        private final float finalZ;

        public PortalParticleData(float red, float green, float blue, float alpha, float finalX, float finalY, float finalZ) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = Mth.clamp(alpha, 0.01f, 4.0f);
            this.finalX = finalX;
            this.finalY = finalY;
            this.finalZ = finalZ;
        }

        @OnlyIn(Dist.CLIENT)
        public float getRed() {
            return this.red;
        }

        @OnlyIn(Dist.CLIENT)
        public float getGreen() {
            return this.green;
        }

        @OnlyIn(Dist.CLIENT)
        public float getBlue() {
            return this.blue;
        }

        @OnlyIn(Dist.CLIENT)
        public float getAlpha() {
            return this.alpha;
        }

        @OnlyIn(Dist.CLIENT)
        public float getFinalX() {
            return this.finalX;
        }

        @OnlyIn(Dist.CLIENT)
        public float getFinalY() {
            return this.finalY;
        }

        @OnlyIn(Dist.CLIENT)
        public float getFinalZ() {
            return this.finalZ;
        }

        @Override
        public @NotNull ParticleType<?> getType() {
            return ClientSetup.PORTAL_PARTICLE_TYPE.get();
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.red);
            buffer.writeFloat(this.green);
            buffer.writeFloat(this.blue);
            buffer.writeFloat(this.alpha);
            buffer.writeFloat(this.finalX);
            buffer.writeFloat(this.finalY);
            buffer.writeFloat(this.finalZ);
        }

        @Override
        public @NotNull String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.red, this.green, this.blue, this.alpha, this.finalX, this.finalY, this.finalZ);
        }
    }
}
