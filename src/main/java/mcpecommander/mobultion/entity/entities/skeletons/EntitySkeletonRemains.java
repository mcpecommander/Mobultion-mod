package mcpecommander.mobultion.entity.entities.skeletons;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import io.netty.util.internal.MathUtil;
import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModSounds;
import mcpecommander.mobultion.mobConfigs.SkeletonsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.IMob;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class EntitySkeletonRemains extends EntityLivingBase implements IAnimated, IMob {
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
	
	@Nullable
    protected ResourceLocation getLootTable()
    {
        return new ResourceLocation(Reference.MOD_ID, "skeletons/skeleton_remains");
    }
	
	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        ResourceLocation resourcelocation = this.getLootTable();
        if (resourcelocation != null)
        {
            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(resourcelocation);
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer)this.world)).withLootedEntity(this).withDamageSource(source);

            if (wasRecentlyHit && this.attackingPlayer != null)
            {
                lootcontext$builder = lootcontext$builder.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
            }

            for (ItemStack itemstack : loottable.generateLootForPools(this.rand, lootcontext$builder.build()))
            {
                this.entityDropItem(itemstack, 0.0F);
            }

            this.dropEquipment(wasRecentlyHit, lootingModifier);
        }
        else
        {
        	MobultionMod.logger.log(Level.ERROR, this.toString() + ": has no loot table or errored loot table.");
            return;
        }
    }
	
	@Override
	protected void onDeathUpdate() {
		++this.deathTime;

        if (this.deathTime == 2)
        {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
            {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }

            this.setDead();

            for (int k = 0; k < 20; ++k)
            {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, d0, d1);
            }
        }
	}
	
	@Override
	public String toString() {
		return super.toString() + this.getSkeleton(getTYPE()).toString();
	}

	public EntitySkeletonRemains(World worldIn, EntityLivingBase originalSkeleton) {
		this(worldIn);
		this.skeletonIn = originalSkeleton;
	}

	@Override
	public float getEyeHeight() {
		return .3f;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(TYPE, Byte.valueOf((byte) 0));
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		//Total world time in ticks
		Long total = this.world.getTotalWorldTime();
		double health = SkeletonsConfig.remains.health;
		if(total < Double.MAX_VALUE) {
			//A formula to increase its health over a configurable amount of days.
			health = health/SkeletonsConfig.remains.days * (MathHelper.clamp(total, 0, 24000 * SkeletonsConfig.remains.days) / 24000);
		}
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
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
		if (source == DamageSource.IN_WALL || source == DamageSource.DROWN) {
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

//		if (this.isWorldRemote() && this.ticksExisted >= 1185
//				&& !this.getAnimationHandler().isHoldAnimationActive("mobultion:remains_rebirth", this)) {
//			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "remains_rebirth", 0, this);
//		}
		if (!this.isWorldRemote()) {
			if(this.ticksExisted >= (SkeletonsConfig.remains.respawnTime * 20) - 15 && !this.getAnimationHandler().isHoldAnimationActive("mobultion:remains_rebirth", this)) {
				this.getAnimationHandler().networkStartAnimation(Reference.MOD_ID, "remains_rebirth", 0, this, false);
			}
			if (this.getTYPE() == 7) {
				if (this.ticksExisted >= SkeletonsConfig.remains.respawnTime * 20 && !this.world.isDaytime()) {
					this.setDead();
					this.playSound(ModSounds.entity_respawn, 1f, .5f);
					EntityLiving skele = this.getSkeleton(this.getTYPE());
					skele.onInitialSpawn(this.world.getDifficultyForLocation(getPosition()), null);
					skele.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
					this.world.spawnEntity(skele);
				}
			} else {
				if (ticksExisted >= SkeletonsConfig.remains.respawnTime * 20) {
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
