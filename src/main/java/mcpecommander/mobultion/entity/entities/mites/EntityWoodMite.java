package mcpecommander.mobultion.entity.entities.mites;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.mobConfigs.MitesConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityWoodMite extends EntityMob implements IAnimated {

	EntityPlayer ridingPlayer;
	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityWoodMite.class);
	private static final DataParameter<Optional<UUID>> PLAYER = EntityDataManager
			.<Optional<UUID>>createKey(EntityWoodMite.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	static {
		EntityWoodMite.animHandler.addAnim(Reference.MOD_ID, "woodmite_walk", "woodmite", true);
	}

	public EntityWoodMite(World worldIn) {
		super(worldIn);
		this.setSize(0.4F, 0.3F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D, 10));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(PLAYER, Optional.absent());
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MitesConfig.mites.wood.health);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = super.attackEntityAsMob(entityIn);
		if (flag && !this.isRiding() && !this.isBeingRidden() && this.getHealth() < 4f) {
			if (entityIn instanceof EntityPlayer) {
				this.startRiding(entityIn);
			}
		}
		if(flag && this.isRiding() && ((EntityLivingBase) entityIn).getHealth() <= 0f){
			this.ridingPlayer = null;
			this.setUUID(null);
		}
		
		return flag;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(source.equals(DamageSource.ON_FIRE) && MitesConfig.mites.wood.doubleBurn){
			return super.attackEntityFrom(source, amount * 1.5f);
		}
		return super.attackEntityFrom(source, amount);
	}

	public UUID getID() {
		return (UUID) ((Optional) this.dataManager.get(PLAYER)).orNull();
	}

	public void setUUID(UUID id) {
		this.dataManager.set(PLAYER, Optional.fromNullable(id));
		this.dataManager.setDirty(PLAYER);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if(this.getID() != null){
			compound.setUniqueId("riding", this.getID());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasUniqueId("riding")) {
			this.setUUID(compound.getUniqueId("riding"));
		}
	}

	@Override
	public boolean startRiding(Entity entityIn) {
		if (!this.isRiding() && entityIn instanceof EntityPlayer) {
			EntityPlayer entity = (EntityPlayer) entityIn;
			this.dataManager.set(PLAYER, Optional.of(entity.getUniqueID()));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isRiding() {
		return ridingPlayer != null;
	}
	
	@Nullable
	@Override
	public Entity getRidingEntity() {
		return this.ridingPlayer;
	}
	
	@Override
	public void dismountEntity(Entity entityIn) {
		this.ridingPlayer = null;
		this.setUUID(null);
	}

	@Override
	public void onUpdate() {
		if (!this.isRiding() && this.getID() != null) {
			ridingPlayer = this.world.getPlayerEntityByUUID(getID());
		}
		
		if(this.getID() == null){
			this.ridingPlayer = null;
		}
		this.renderYawOffset = this.rotationYaw;
		if (this.isRiding()) {
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
			
		}
		super.onUpdate();
		if (this.isRiding()) {
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
			double yaw = ((ridingPlayer.rotationYawHead + 90) * Math.PI) / 180;
			double z = Math.sin(yaw);
			double x = Math.cos(yaw);
			this.setPosition(ridingPlayer.posX + (x/2.5), ridingPlayer.posY + ridingPlayer.getEyeHeight(), ridingPlayer.posZ + (z/2.5));
			this.getLookHelper().setLookPositionWithEntity(ridingPlayer, 30, 30);
		}

	}

	@Override
	protected boolean isValidLightLevel() {
		return true;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	@Override
	public void setRenderYawOffset(float offset) {
		this.rotationYaw = offset;
		super.setRenderYawOffset(offset);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SILVERFISH_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SILVERFISH_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
	}

	@Override
	public double getYOffset() {
		return 0.1D;
	}

	@Override
	public float getEyeHeight() {
		return 0.1F;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.animHandler.animationsUpdate(this);
		if (!this.isWorldRemote() && this.isRiding()) {
			if(((EntityPlayerMP) this.ridingPlayer).motionY > 0.1d && this.ticksExisted % 5 == 0){
				this.attackEntityFrom(DamageSource.causeMobDamage(this.ridingPlayer), 0.5f);
			}
			if(this.ticksExisted % 20 == 0){
				this.attackEntityAsMob(ridingPlayer);//tityFrom(DamageSource.causeMobDamage(this), (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
			}
		}
		if (this.isWorldRemote()) {
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "woodmite_walk", this)) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "woodmite_walk", 0, this);
			}
		}
	}

	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityWoodMite.animHandler;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public double getX() {
		return this.posX;
	}

	@Override
	public double getY() {
		return this.posY;
	}

	@Override
	public double getZ() {
		return this.posZ;
	}

	@Override
	public boolean isWorldRemote() {
		return this.world.isRemote;
	}

}
