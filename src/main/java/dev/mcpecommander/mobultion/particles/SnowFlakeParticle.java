package dev.mcpecommander.mobultion.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.mcpecommander.mobultion.setup.ClientSetup;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Locale;

/* McpeCommander created on 10/07/2021 inside the package - dev.mcpecommander.mobultion.particles */
public class SnowFlakeParticle extends SpriteTexturedParticle {

    protected SnowFlakeParticle(ClientWorld world, double posX, double posY, double posZ,
                             double speedX, double speedY, double speedZ,
                             SnowFlakeParticle.SnowFlakeParticleData data) {
        super(world, posX, posY, posZ, speedX, speedY, speedZ);
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.xd = speedX;
        this.yd = speedY;
        this.zd = speedZ;
        this.quadSize = 0.1f * (this.random.nextFloat() * 2f);
        this.rCol = data.getRed();
        this.gCol = data.getGreen();
        this.bCol = data.getBlue();
        this.alpha = data.getAlpha();
        this.lifetime = this.random.nextInt(30) + 20;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.95F;
            this.yd *= 0.95F;
            this.zd *= 0.95F;

        }
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements IParticleFactory<SnowFlakeParticle.SnowFlakeParticleData> {

        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite){
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(SnowFlakeParticle.SnowFlakeParticleData data, ClientWorld world,
                                       double posX, double posY, double posZ,
                                       double speedX, double speedY, double speedZ) {
            SnowFlakeParticle particle = new SnowFlakeParticle(world, posX, posY, posZ, speedX, speedY, speedZ, data);
            particle.pickSprite(sprite);
            return particle;
        }
    }

    public static class SnowFlakeParticleData implements IParticleData {

        public static final IParticleData.IDeserializer<SnowFlakeParticle.SnowFlakeParticleData> DESERIALIZER = new IDeserializer<SnowFlakeParticle.SnowFlakeParticleData>() {
            @Override
            public SnowFlakeParticle.SnowFlakeParticleData fromCommand(ParticleType<SnowFlakeParticle.SnowFlakeParticleData> particleType, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float red = (float) reader.readDouble();
                reader.expect(' ');
                float green = (float) reader.readDouble();
                reader.expect(' ');
                float blue = (float) reader.readDouble();
                reader.expect(' ');
                float alpha = (float) reader.readDouble();
                return new SnowFlakeParticle.SnowFlakeParticleData(red, green, blue, alpha);
            }

            @Override
            public SnowFlakeParticle.SnowFlakeParticleData fromNetwork(ParticleType<SnowFlakeParticle.SnowFlakeParticleData> particleType, PacketBuffer buffer) {
                return new SnowFlakeParticle.SnowFlakeParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                        buffer.readFloat());
            }
        };

        public static final Codec<SnowFlakeParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("red").forGetter(data -> data.red),
                Codec.FLOAT.fieldOf("green").forGetter(data -> data.green),
                Codec.FLOAT.fieldOf("blue").forGetter(data -> data.blue),
                Codec.FLOAT.fieldOf("alpha").forGetter(data -> data.alpha)
        ).apply(instance, SnowFlakeParticle.SnowFlakeParticleData::new));

        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;

        public SnowFlakeParticleData(float red, float green, float blue, float alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = MathHelper.clamp(alpha, 0.01f, 4.0f);

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


        @Override
        public ParticleType<?> getType() {
            return ClientSetup.SNOW_FLAKE_PARTICLE_TYPE.get();
        }

        @Override
        public void writeToNetwork(PacketBuffer buffer) {
            buffer.writeFloat(this.red);
            buffer.writeFloat(this.green);
            buffer.writeFloat(this.blue);
            buffer.writeFloat(this.alpha);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.red, this.green, this.blue, this.alpha);
        }
    }
}