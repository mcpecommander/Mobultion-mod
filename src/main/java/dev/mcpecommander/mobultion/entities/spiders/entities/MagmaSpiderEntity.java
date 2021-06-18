package dev.mcpecommander.mobultion.entities.spiders.entities;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/* Created by McpeCommander on 2021/06/18 */
public class MagmaSpiderEntity extends MobultionSpiderEntity{

    private final AnimationFactory factory = new AnimationFactory(this);
    private int flameParticleTick = 0;

    public MagmaSpiderEntity(EntityType<? extends MonsterEntity> mob, World world) {
        super(mob, world);
        this.maxDeathTimer = 35;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 26.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        if(this.isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
        }else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void setRot(float p_70101_1_, float p_70101_2_) {
        if(isDeadOrDying()) return;
        super.setRot(p_70101_1_, p_70101_2_);
    }

    @Override
    public float rotate(Rotation p_184229_1_) {
        if(isDeadOrDying()) return 0;
        return super.rotate(p_184229_1_);
    }

    @Override
    public void setYBodyRot(float p_181013_1_) {
        if(isDeadOrDying()) return;
        super.setYBodyRot(p_181013_1_);
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.registerParticleListener(this::particleListener);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level.isClientSide && flameParticleTick > 0){
            for(int i = 0; i < 10; ++i) {
                double d0 = this.random.nextGaussian() * 0.05D;
                double d1 = this.random.nextGaussian() * 0.05D;
                double d2 = this.random.nextGaussian() * 0.05D;
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D)
                        , d0, d1, d2);
            }
            flameParticleTick --;
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    private <T extends IAnimatable> void particleListener(ParticleKeyFrameEvent<T> event) {
        switch (event.locator){
            case "all":
                flameParticleTick = 2;
                return;
            case "leg1":
                Vector3d leg1 = new Vector3d( (-16.4f/16f), (2.9d/16d), (9.3f/16f));
                leg1 = leg1.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg1.x, leg1.y, leg1.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg1.x, leg1.y + 0.1f, leg1.z, 0, 0.1f, 0);
                return;
            case "leg2":
                Vector3d leg2 = new Vector3d( (-16.5f/16f), (2.9d/16d), (4f/16f));
                leg2 = leg2.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg2.x, leg2.y, leg2.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg2.x, leg2.y + 0.1f, leg2.z, 0, 0.1f, 0);
                return;
            case "leg3":
                Vector3d leg3 = new Vector3d( (-16.5f/16f), (2.9d/16d), (-3.3f/16f));
                leg3 = leg3.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg3.x, leg3.y, leg3.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg3.x, leg3.y + 0.1f, leg3.z, 0, 0.1f, 0);
                return;
            case "leg4":
                Vector3d leg4 = new Vector3d( (-16.4f/16f), (2.9d/16d), (-9.3f/16f));
                leg4 = leg4.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg4.x, leg4.y, leg4.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg4.x, leg4.y + 0.1f, leg4.z, 0, 0.1f, 0);
                return;
            case "leg5":
                Vector3d leg5 = new Vector3d( (16.4f/16f), (2.9d/16d), (9.3f/16f));
                leg5 = leg5.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg5.x, leg5.y, leg5.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg5.x, leg5.y + 0.1f, leg5.z, 0, 0.1f, 0);
                return;
            case "leg6":
                Vector3d leg6 = new Vector3d( (16.5f/16f), (2.9d/16d), (4f/16f));
                leg6 = leg6.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg6.x, leg6.y, leg6.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg6.x, leg6.y + 0.1f, leg6.z, 0, 0.1f, 0);
                return;
            case "leg7":
                Vector3d leg7 = new Vector3d( (16.5f/16f), (2.9d/16d), (-3.3f/16f));
                leg7 = leg7.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg7.x, leg7.y, leg7.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg7.x, leg7.y + 0.1f, leg7.z, 0, 0.1f, 0);
                return;
            case "leg8":
                Vector3d leg8 = new Vector3d( (16.4f/16f), (2.9d/16d), (-9.3f/16f));
                leg8 = leg8.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg8.x, leg8.y, leg8.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg8.x, leg8.y + 0.1f, leg8.z, 0, 0.1f, 0);
                return;
            default:
        }
    }

    @Override
    protected void tickDeath() {
        if(this.deathTimer++ == maxDeathTimer){
            this.remove();
            BlockPos pos = new BlockPos(this.getX(), this.getY(), this.getZ());
            if(AbstractFireBlock.canBePlacedAt(this.level, pos, Direction.UP)){
                BlockState state = AbstractFireBlock.getState(this.level, pos);
                this.level.setBlock(pos, state, Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
        }
    }
}
