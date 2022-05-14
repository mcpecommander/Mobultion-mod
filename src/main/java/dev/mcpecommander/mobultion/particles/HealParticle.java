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

import java.util.Locale;

/* McpeCommander created on 09/07/2021 inside the package - dev.mcpecommander.mobultion.particles */
public class HealParticle extends TextureSheetParticle {

    SpriteSet sprite;
    double finalX, finalY, finalZ;
    double originalDistance;

    protected HealParticle(ClientLevel world, double posX, double posY, double posZ,
                               double finalX, double finalY, double finalZ, HealParticleData data, SpriteSet sprite) {
        super(world, posX, posY, posZ, finalX, finalY, finalZ);
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        originalDistance = new Vec3(posX, posY, posZ).distanceTo(new Vec3(finalX, finalY, finalZ));
        this.finalX = finalX;
        this.finalY = finalY;
        this.finalZ = finalZ;
        Vec3 speed = new Vec3(finalX - posX, finalY - posY, finalZ - posZ).normalize();
        this.xd = speed.x / 2f;
        this.yd = speed.y / 2f;
        this.zd = speed.z / 2f;
        this.quadSize = 0.1f + 0.1f * (this.random.nextFloat() * 2f);
        this.rCol = data.red();
        this.gCol = data.green();
        this.bCol = data.blue();
        //this.alpha = data.getAlpha();
        this.lifetime = 50;
        this.sprite = sprite;
        this.setSpriteFromAge(sprite);
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(sprite);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime || checkReached()) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
        }
    }

    private boolean checkReached(){
        return (this.x < finalX + 0.2f && this.x > finalX - 0.2f) &&
                (this.y < finalY + 0.2f && this.y > finalY - 0.2f) &&
                (this.z < finalZ + 0.2f && this.z > finalZ - 0.2f);
    }

    @Override
    public void setSpriteFromAge(@NotNull SpriteSet sprite) {
        double currentDistance = Math.sqrt(new Vec3(x, y, z).distanceToSqr(finalX, finalY, finalZ));
        this.setSprite(this.sprite.get((int) MathCalculations.map(currentDistance, 0, originalDistance, 0, lifetime), lifetime));
        //this.setSprite(this.sprite.get(MathHelper.floor(this.age/2f), this.lifetime));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public record Factory(SpriteSet sprite) implements ParticleProvider<HealParticleData> {

            @Override
            public Particle createParticle(@NotNull HealParticleData data, @NotNull ClientLevel world,
                                           double posX, double posY, double posZ,
                                           double speedX, double speedY, double speedZ) {
                return new HealParticle(world, posX, posY, posZ, speedX, speedY, speedZ, data, sprite);
            }
        }

    public record HealParticleData(float red, float green, float blue, float alpha) implements ParticleOptions {

            public static final ParticleOptions.Deserializer<HealParticleData> DESERIALIZER = new Deserializer<>() {
                @Override
                public @NotNull HealParticleData fromCommand(@NotNull ParticleType<HealParticleData> particleType, StringReader reader) throws CommandSyntaxException {
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
                public @NotNull HealParticleData fromNetwork(@NotNull ParticleType<HealParticleData> particleType, FriendlyByteBuf buffer) {
                    return new HealParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                            buffer.readFloat());
                }
            };

            public static final Codec<HealParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("red").forGetter(data -> data.red),
                    Codec.FLOAT.fieldOf("green").forGetter(data -> data.green),
                    Codec.FLOAT.fieldOf("blue").forGetter(data -> data.blue),
                    Codec.FLOAT.fieldOf("alpha").forGetter(data -> data.alpha)
            ).apply(instance, HealParticleData::new));

            public HealParticleData(float red, float green, float blue, float alpha) {
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
                return ClientSetup.HEAL_PARTICLE_TYPE.get();
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
