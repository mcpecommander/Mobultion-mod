package dev.mcpecommander.mobultion.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.mcpecommander.mobultion.setup.ClientSetup;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Locale;

/* McpeCommander created on 10/07/2021 inside the package - dev.mcpecommander.mobultion.particles */
public class SnowFlakeParticle extends TextureSheetParticle {

    protected SnowFlakeParticle(ClientLevel world, double posX, double posY, double posZ,
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
        this.rCol = data.red();
        this.gCol = data.green();
        this.bCol = data.blue();
        this.alpha = data.alpha();
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
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleProvider<SnowFlakeParticle.SnowFlakeParticleData> {

        private final SpriteSet sprite;

        public Factory(SpriteSet sprite){
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(SnowFlakeParticle.@NotNull SnowFlakeParticleData data, @NotNull ClientLevel world,
                                       double posX, double posY, double posZ,
                                       double speedX, double speedY, double speedZ) {
            SnowFlakeParticle particle = new SnowFlakeParticle(world, posX, posY, posZ, speedX, speedY, speedZ, data);
            particle.pickSprite(sprite);
            return particle;
        }
    }

    public record SnowFlakeParticleData(float red, float green, float blue, float alpha) implements ParticleOptions {

            public static final ParticleOptions.Deserializer<SnowFlakeParticle.SnowFlakeParticleData> DESERIALIZER = new Deserializer<>() {
                @Override
                public SnowFlakeParticle.@NotNull SnowFlakeParticleData fromCommand(@NotNull ParticleType<SnowFlakeParticle.SnowFlakeParticleData> particleType, StringReader reader) throws CommandSyntaxException {
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
                public SnowFlakeParticle.@NotNull SnowFlakeParticleData fromNetwork(@NotNull ParticleType<SnowFlakeParticle.SnowFlakeParticleData> particleType, FriendlyByteBuf buffer) {
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

            public SnowFlakeParticleData(float red, float green, float blue, float alpha) {
                this.red = red;
                this.green = green;
                this.blue = blue;
                this.alpha = Mth.clamp(alpha, 0.01f, 4.0f);

            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public float red() {
                return this.red;
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public float green() {
                return this.green;
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public float blue() {
                return this.blue;
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public float alpha() {
                return this.alpha;
            }


            @Override
            public @NotNull ParticleType<?> getType() {
                return ClientSetup.SNOW_FLAKE_PARTICLE_TYPE.get();
            }

            @Override
            public void writeToNetwork(FriendlyByteBuf buffer) {
                buffer.writeFloat(this.red);
                buffer.writeFloat(this.green);
                buffer.writeFloat(this.blue);
                buffer.writeFloat(this.alpha);
            }

            @Override
            public @NotNull String writeToString() {
                return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                        this.red, this.green, this.blue, this.alpha);
            }
        }
}