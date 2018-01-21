package mcpecommander.mobultion.entity.entities.skeletons;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.animation.AnimationRaiseHands;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.skeletonsAI.EntityAIBatMorph;
import mcpecommander.mobultion.entity.entityAI.skeletonsAI.EntityAIVampireMeleeAttack;
import mcpecommander.mobultion.init.ModSounds;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityVampireSkeleton extends EntityAnimatedSkeleton {
	private static final DataParameter<Byte> MORPHING = EntityDataManager.<Byte>createKey(EntityVampireSkeleton.class, DataSerializers.BYTE);
	
	private boolean flag = false;

	static {
		EntityVampireSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_death", "forest_skeleton", false);
		EntityVampireSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "forest_skeleton", true);
		EntityVampireSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "forest_skeleton", true);
		EntityVampireSkeleton.animHandler.addAnim(Reference.MOD_ID, "bat_morph", "forest_skeleton", true);
		EntityVampireSkeleton.animHandler.addAnim(Reference.MOD_ID, "raise_hands", new AnimationRaiseHands());
		EntityVampireSkeleton.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
		EntityVampireSkeleton.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}

	public EntityVampireSkeleton(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.99F);
	}
	
	public void setMorphing(byte isMorphing){
		this.dataManager.set(MORPHING, isMorphing);
		this.dataManager.setDirty(MORPHING);
	}
	
	public byte getMorphing(){
		return this.dataManager.get(MORPHING).byteValue();
	}
	
	public boolean isMorphing(){
		return this.dataManager.get(MORPHING).byteValue() > 0;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAIBatMorph(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIVampireMeleeAttack(this, 1.2d, true));
		this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(MORPHING, (byte)0);
	}

	@Override
	public double getYOffset() {
		return -0.43D;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20f);

	}

	@Override
	public float getEyeHeight() {
		return 1.74F;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
		if (this.isWorldRemote()
				&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_death", this) && !flag) {
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "raise_hands", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this);
			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "bat_morph", this);
			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_death", 0, this);
			flag = true;
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
//		if(this.ticksExisted % 20 == 0){
//			System.out.println(this.getHealth());
//		}
		if (!this.world.isRemote) {
			if (this.world.isDaytime() && !this.isChild()) {
				float f = this.getBrightness();
				if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world
						.canSeeSky(new BlockPos(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ))) {
					boolean flag = true;
					ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

					if (!itemstack.isEmpty()) {
						if (itemstack.isItemStackDamageable()) {
							itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

							if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
								this.renderBrokenItemStack(itemstack);
								this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
							}
						}

						flag = false;
					}

					if (flag) {
						this.setHealth(0f);
					}
				}
			}
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
			if (this.isDead) {
				EntitySkeletonRemains grave = new EntitySkeletonRemains(this.world, this);
				grave.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				this.world.spawnEntity(grave);
			}
			
			if(this.getMorphing() == 2){
				EntityBat bat = new EntityBat(world);
				bat.setLocationAndAngles(this.posX, this.posY + this.getEyeHeight(), this.posZ, this.rotationYaw, 0.0f);
				bat.getActivePotionEffects().addAll(this.getActivePotionEffects());
				bat.setHealth(this.getHealth());
				this.world.spawnEntity(bat);
				this.setDead();
			}
		}
		if (this.isWorldRemote() && !this.isMorphing()) {
			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& !this.getMoving()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				if (!this.isSwingingArms()) {
					this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				}
			}
			if (this.isSwingingArms()
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "raise_hands", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "raise_hands", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& this.getMoving() && this.deathTime < 1 && !this.isRiding()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this)
					&& !this.isSwingingArms()
					&& this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "raise_hands", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk_hands", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this) && this.deathTime < 1) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", this);
			}
			if(this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "raise_hands", this) && !this.isSwingingArms()){
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "raise_hands", this);
			}
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "riding", this) && this.isRiding()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "riding", this);
			}
			
		}
		if(this.isWorldRemote()){
			if(!this.getAnimationHandler().isHoldAnimationActive("mobultion:bat_morph", this) && this.getMorphing() == 1){
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "raise_hands", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "bat_morph", this);
			}
		}

	}

	@Override
	protected EntityArrow getArrow(float distanceFactor) {
		return null;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = super.attackEntityAsMob(entityIn);
		if (flag) {
			this.heal(2f);
			this.playSound(ModSounds.vampire_bite, this.getSoundVolume(), this.getSoundPitch());
		}
		return flag;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.getTrueSource() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) source.getTrueSource();
			if (player.isCreative() || source.isFireDamage()) {
				return super.attackEntityFrom(source, amount);
			}
			if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().isItemEnchanted()) {
				if (EnchantmentHelper.getEnchantments(player.getHeldItemMainhand()).keySet()
						.contains(Enchantments.SWEEPING)
						|| EnchantmentHelper.getEnchantments(player.getHeldItemMainhand()).keySet()
								.contains(Enchantments.FIRE_ASPECT)) {
					return super.attackEntityFrom(source, amount);
				}
			}
		} else if (DamageSource.IN_FIRE.equals(source) || DamageSource.ON_FIRE.equals(source)
				|| DamageSource.LAVA.equals(source) || source.isFireDamage()) {
			return super.attackEntityFrom(source, amount);
		}
		this.playSound(ModSounds.shield_block, this.getSoundVolume(), this.getSoundPitch());
		return false;
	}

}
