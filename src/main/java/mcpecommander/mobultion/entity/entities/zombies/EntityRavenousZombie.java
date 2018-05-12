package mcpecommander.mobultion.entity.entities.zombies;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIItemEat;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIItemTarget;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIKnightAttackMelee;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIRavenousTarget;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.init.ModPotions;
import mcpecommander.mobultion.init.ModSounds;
import mcpecommander.mobultion.mobConfigs.ZombiesConfig;
import mcpecommander.mobultion.particle.VomitParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRavenousZombie extends EntityAnimatedZombie {

	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityRavenousZombie.class);
	private EntityItem item;
	private boolean flag = false;
	float base = 1f;

	static {
		EntityRavenousZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "ravenous_zombie", true);
		EntityRavenousZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "ravenous_zombie", true);
		EntityRavenousZombie.animHandler.addAnim(Reference.MOD_ID, "ravenous_eating", "ravenous_zombie", true);
		EntityRavenousZombie.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
		EntityRavenousZombie.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}

	public EntityRavenousZombie(World worldIn) {
		super(worldIn);
		this.setSize(0.7F, 2F);
	}
	
	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityRavenousZombie.animHandler;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIKnightAttackMelee(this, 1D, false));
		this.tasks.addTask(2, new EntityAIItemEat(this, 1.2D));
		this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAIRavenousTarget(this, false, EntityAnimal.class));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false));
		// this.targetTasks.addTask(4, new EntityAICakeTarget(this, 1D));
		this.targetTasks.addTask(5, new EntityAIItemTarget(this));

	}

	public EntityItem getItemTarget() {
		return item;
	}

	public void setItem(EntityItem item) {
		this.item = item;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if (item != null) {
			compound.setInteger("itemTarget", item.getEntityId());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasKey("itemTarget")) {
			this.setItem((EntityItem) this.world.getEntityByID(compound.getInteger("itemTarget")));
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (!this.isWorldRemote()) {
			if (entityIn instanceof EntityItem) {
				EntityItem eItem = (EntityItem) entityIn;
				Item item = eItem.getItem().getItem();
				if (item instanceof ItemFood) {
					ItemFood food = (ItemFood) item;
					this.heal((food.getHealAmount(eItem.getItem()) * eItem.getItem().getCount()) / 2f);
					eItem.setDead();
					this.setItem(null);
					this.playSound(ModSounds.ravenous_eating, this.getSoundVolume(), this.getSoundPitch());
				}
			}
		}
		boolean flag = super.attackEntityAsMob(entityIn);
		if (entityIn instanceof EntityLivingBase && flag) {
			if (((EntityLivingBase) entityIn).getHealth() <= 0f) {
				this.playSound(ModSounds.burp, this.getSoundVolume(), this.getSoundPitch());
			}
			this.heal(2f);
		}

		return flag;
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();
		if (!isWorldRemote() && !flag) {
			flag = true;
			this.playSound(ModSounds.puke, 1.5f, this.getSoundPitch());
		}
		if (isWorldRemote()) {
			base += 0.1f;
			this.renderVomit(base);
		}
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!world.isRemote && ZombiesConfig.zombies.ravenous.explodeOnDeath) {
			Explosion explosion = new Explosion(this.world, this, this.posX, this.posY, this.posZ, 1.2f, false, false);
			explosion.doExplosionA();
			for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(this,
					this.getEntityBoundingBox().grow(3))) {
				if (entity instanceof EntityLivingBase) {
					EntityLivingBase entityIn = (EntityLivingBase) entity;
					entityIn.addPotionEffect(new PotionEffect(ModPotions.potionVomit, 200, 0, false, false));
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void renderVomit(float radius){
		for (int i = 0; i < 360; i += 20) {
			double x = Math.sin(i);
			double z = Math.cos(i);
			VomitParticle newEffect = new VomitParticle(this.world, this.posX + (x * radius), this.posY + (this.getRNG().nextFloat() * .5),
					this.posZ + (z * radius), 0.0d, 0.0d, 0.0d, 1.2f);
			Minecraft.getMinecraft().effectRenderer.addEffect(newEffect);
		}
//		for(int i = 0; i < count; i ++){
//			double yaw = ((this.rotationYawHead + 90) * Math.PI) / 180;
//			double z = Math.sin(yaw);
//			double x = Math.cos(yaw);
//			double y = this.getEyeHeight() - 
//			VomitParticle newEffect = new VomitParticle(this.world, this.posX + x, this.posY + this.getEyeHeight(), this.posZ + z, 0.0d, 0.0d, 0.0d, 1.2f);
//			Minecraft.getMinecraft().effectRenderer.addEffect(newEffect);
//		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.16D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.knife));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.fork));
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.getAttackTarget() == null) {
			this.setAttacking(false);
		} else {
			this.setItem(null);
		}

		// if (!world.isRemote && world.getBlockState(getPosition().add(1, 0,
		// 0)).getBlock() instanceof BlockCake) {
		// BlockCake block = (BlockCake)
		// world.getBlockState(getPosition().add(1, 0, 0)).getBlock();
		// int i =
		// block.getMetaFromState(world.getBlockState(getPosition().add(1, 0,
		// 0)));
		// this.heal(2f);
		// if (i < 6) {
		// world.setBlockState(getPosition().add(1, 0, 0),
		// world.getBlockState(getPosition().add(1, 0,
		// 0)).withProperty(block.BITES, Integer.valueOf(i + 1)), 3);
		// } else {
		// world.setBlockToAir(getPosition().add(1, 0, 0));
		// }
		// }

		if (!this.isWorldRemote()) {
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
		}
		if (this.isWorldRemote()) {
			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& !this.getMoving()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				if (!this.getAttacking()) {
					this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				}
			}
			if (this.getAttacking()
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "ravenous_eating", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "ravenous_eating", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& this.getMoving() && this.deathTime < 1 && !this.isRiding()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this)
					&& !this.getAttacking()
					&& this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "ravenous_eating", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk_hands", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this) && this.deathTime < 1) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", this);
			}
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "riding", this) && this.isRiding()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "riding", this);
			}
		}

	}

}
