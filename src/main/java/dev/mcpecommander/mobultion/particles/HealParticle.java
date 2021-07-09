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

/* McpeCommander created on 09/07/2021 inside the package - dev.mcpecommander.mobultion.particles */
public class HealParticle extends SpriteTexturedParticle {

    IAnimatedSprite sprite;

    protected HealParticle(ClientWorld world, double posX, double posY, double posZ,
                           double speedX, double speedY, double speedZ, HealParticleData data, IAnimatedSprite sprite) {
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
        //this.alpha = data.getAlpha();
        this.lifetime = this.random.nextInt(30) + 20;
        this.sprite = sprite;
        this.setSpriteFromAge(sprite);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(sprite);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
        }
    }

    @Override
    public void setSpriteFromAge(IAnimatedSprite sprite) {
        this.setSprite(sprite.get(MathHelper.floor(this.age/2f), this.lifetime));
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements IParticleFactory<HealParticleData>{

        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite){
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(HealParticleData data, ClientWorld world,
                                       double posX, double posY, double posZ,
                                       double speedX, double speedY, double speedZ) {
            return new HealParticle(world, posX, posY, posZ, speedX, speedY, speedZ, data, sprite);
        }
    }

    public static class HealParticleData implements IParticleData{

        public static final IParticleData.IDeserializer<HealParticleData> DESERIALIZER = new IDeserializer<HealParticleData>() {
            @Override
            public HealParticleData fromCommand(ParticleType<HealParticleData> particleType, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float red = (float) reader.readDouble();
                reader.expect(' ');
                float green = (float) reader.readDouble();
                reader.expect(' ');
                float blue = (float) reader.readDouble();
                reader.expect(' ');
                float alpha = (float) reader.readDouble();
                return new HealParticleData(red, green, blue, alpha);
            }

            @Override
            public HealParticleData fromNetwork(ParticleType<HealParticleData> particleType, PacketBuffer buffer) {
                return new HealParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                        buffer.readFloat());
            }
        };

        public static final Codec<HealParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("r").forGetter(data -> data.red),
                Codec.FLOAT.fieldOf("g").forGetter(data -> data.green),
                Codec.FLOAT.fieldOf("b").forGetter(data -> data.blue),
                Codec.FLOAT.fieldOf("a").forGetter(data -> data.alpha)
        ).apply(instance, HealParticleData::new));

        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;

        public HealParticleData(float red, float green, float blue, float alpha) {
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
            return ClientSetup.HEAL_PARTICLE_TYPE;
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
