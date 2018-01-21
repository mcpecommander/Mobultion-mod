package mcpecommander.mobultion.entity.entities.skeletons;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntitySkeletonRemains extends EntityLivingBase implements IAnimated {
	private static final DataParameter<Byte> TYPE = EntityDataManager.<Byte>createKey(EntitySkeletonRemains.class,
			DataSerializers.BYTE);
	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntitySkeletonRemains.class);
	private EntityLivingBase skeletonIn;

	static {
		EntitySkeletonRemains.animHandler.addAnim(Reference.MOD_ID, "remains_rebirth", "skeleton_remains", false);
	}

	public EntitySkeletonRemains(World worldIn) {
		super(worldIn);
		this.setSize(.75f, .52f);
	}

	public EntitySkeletonRemains(World worldIn, EntityLivingBase originalSkeleton) {
		this(worldIn);
		this.skeletonIn = originalSkeleton;
	}

	@Override
	public float getEyeHeight() {
		return .3f;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(TYPE, Byte.valueOf((byte) 0));
	}

	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000.0D);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		return;
	}

	@Override
	protected void collideWithNearbyEntities() {
		return;
	}

	@Override
	public float getCollisionBorderSize() {
		return .35F;
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		if (source == DamageSource.IN_WALL) {
			return true;
		}
		return super.isEntityInvulnerable(source);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.getAnimationHandler().animationsUpdate(this);
		if (this.skeletonIn != null && this.getTYPE() == (byte) 0) {
			this.dataManager.set(TYPE, Byte.valueOf(this.getOriginalCreatorType(skeletonIn)));
			this.dataManager.setDirty(TYPE);
		}

		if (this.isWorldRemote() && this.ticksExisted >= 1185
				&& !this.getAnimationHandler().isHoldAnimationActive("mobultion:remains_rebirth", this)) {
			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "remains_rebirth", 0, this);
		}
		if (!this.isWorldRemote()) {
			if (this.getTYPE() == 7) {
				if (this.ticksExisted >= 1200 && !this.world.isDaytime()) {
					this.setDead();
					this.playSound(ModSounds.entity_respawn, 1f, .5f);
					EntityLiving skele = this.getSkeleton(this.getTYPE());
					skele.onInitialSpawn(this.world.getDifficultyForLocation(getPosition()), null);
					skele.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
					this.world.spawnEntity(skele);
				}
			} else {
				if (ticksExisted >= 1200) {
					this.setDead();
					this.playSound(ModSounds.entity_respawn, 1f, .5f);
					EntityLiving skele = this.getSkeleton(this.getTYPE());
					skele.onInitialSpawn(this.world.getDifficultyForLocation(getPosition()), null);
					skele.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
					this.world.spawnEntity(skele);
				}
			}
		}

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setByte("maker", Byte.valueOf(getTYPE()));
		compound.setInteger("ticks", this.ticksExisted);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.dataManager.set(TYPE, Byte.valueOf(compound.getByte("maker")));
		this.ticksExisted = compound.getInteger("ticks");
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return null;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return this.getTYPE() == 2 ? ModSounds.magma_remains_death : null;
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {

	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.RIGHT;
	}

	public byte getTYPE() {
		return this.dataManager.get(TYPE).byteValue();
	}

	public byte getOriginalCreatorType(EntityLivingBase skeleton) {
		if (skeleton.getClass() == EntityWitheringSkeleton.class) {
			return 1;
		} else if (skeleton.getClass() == EntityMagmaSkeleton.class) {
			return 2;
		} else if (skeleton.getClass() == EntitySniperSkeleton.class) {
			return 3;
		} else if (skeleton.getClass() == EntityShamanSkeleton.class) {
			return 4;
		} else if (skeleton.getClass() == EntityJokerSkeleton.class) {
			return 5;
		} else if (skeleton.getClass() == EntityCorruptedSkeleton.class) {
			return 6;
		} else {
			return 7;
		}
	}

	public EntityLiving getSkeleton(byte type) {
		switch (type) {
		case (byte) 1:
			return new EntityWitheringSkeleton(this.world);
		case (byte) 2:
			return new EntityMagmaSkeleton(this.world);
		case (byte) 3:
			return new EntitySniperSkeleton(this.world);
		case (byte) 4:
			return new EntityShamanSkeleton(this.world);
		case (byte) 5:
			return new EntityJokerSkeleton(this.world);
		case (byte) 6:
			return new EntityCorruptedSkeleton(this.world);
		case (byte) 7:
			return new EntityVampireSkeleton(this.world);
		}
		MobultionMod.logger.log(Level.FATAL, "If this is printed, there is a serious problem in " + Reference.MOD_ID
				+ ":" + this.toString() + " and you need to contact the mod author");
		return new EntitySkeleton(this.world);
	}

	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntitySkeletonRemains.animHandler;
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
