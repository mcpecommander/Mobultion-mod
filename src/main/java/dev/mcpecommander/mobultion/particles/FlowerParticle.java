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
public class FlowerParticle extends SpriteTexturedParticle {

    protected FlowerParticle(ClientWorld world, double posX, double posY, double posZ,
                             double speedX, double speedY, double speedZ,
                             FlowerParticle.FlowerParticleData data) {
        super(world, posX, posY, posZ, speedX, speedY, speedZ);
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.xd = speedX;
        this.yd = speedY;
        this.zd = speedZ;
        this.quadSize = 0.075f * (this.random.nextFloat() * 2f) * data.getSize();
        this.rCol = data.getRed();
        this.gCol = data.getGreen();
        this.bCol = data.getBlue();
        this.alpha = data.getAlpha();
        this.lifetime = this.random.nextInt(20) + 10;
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

    public static class Factory implements IParticleFactory<FlowerParticle.FlowerParticleData> {

        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite){
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(FlowerParticle.FlowerParticleData data, ClientWorld world,
                                       double posX, double posY, double posZ,
                                       double speedX, double speedY, double speedZ) {
            FlowerParticle particle = new FlowerParticle(world, posX, posY, posZ, speedX, speedY, speedZ, data);
            particle.pickSprite(sprite);
            return particle;
        }
    }

    public static class FlowerParticleData implements IParticleData {

        public static final IDeserializer<FlowerParticle.FlowerParticleData> DESERIALIZER = new IDeserializer<FlowerParticle.FlowerParticleData>() {
            @Override
            public FlowerParticle.FlowerParticleData fromCommand(ParticleType<FlowerParticle.FlowerParticleData> particleType, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float red = (float) reader.readDouble();
                reader.expect(' ');
                float green = (float) reader.readDouble();
                reader.expect(' ');
                float blue = (float) reader.readDouble();
                reader.expect(' ');
                float alpha = (float) reader.readDouble();
                reader.expect(' ');
                float size = (float) reader.readDouble();
                return new FlowerParticle.FlowerParticleData(red, green, blue, alpha, size);
            }

            @Override
            public FlowerParticle.FlowerParticleData fromNetwork(ParticleType<FlowerParticle.FlowerParticleData> particleType, PacketBuffer buffer) {
                return new FlowerParticle.FlowerParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                        buffer.readFloat(), buffer.readFloat());
            }
        };

        public static final Codec<FlowerParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("red").forGetter(data -> data.red),
                Codec.FLOAT.fieldOf("green").forGetter(data -> data.green),
                Codec.FLOAT.fieldOf("blue").forGetter(data -> data.blue),
                Codec.FLOAT.fieldOf("alpha").forGetter(data -> data.alpha),
                Codec.FLOAT.fieldOf("size").forGetter(data -> data.size)
        ).apply(instance, FlowerParticle.FlowerParticleData::new));

        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;
        private final float size;

        public FlowerParticleData(float red, float green, float blue, float alpha, float size) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = MathHelper.clamp(alpha, 0.01f, 4.0f);
            this.size = size;
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
        public float getSize() {
            return this.size;
        }

        @Override
        public ParticleType<?> getType() {
            return ClientSetup.FLOWER_PARTICLE_TYPE.get();
        }

        @Override
        public void writeToNetwork(PacketBuffer buffer) {
            buffer.writeFloat(this.red);
            buffer.writeFloat(this.green);
            buffer.writeFloat(this.blue);
            buffer.writeFloat(this.alpha);
            buffer.writeFloat(this.size);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.red, this.green, this.blue, this.alpha, this.size);
        }
    }
}