package mcpecommander.mobultion.entity.entities.endermen;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationCape;
import mcpecommander.mobultion.entity.animation.AnimationLookAtEnderman;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.endermenAI.EntityAIEndermanLightningAttack;
import mcpecommander.mobultion.init.ModItems;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWanderingEnderman extends EntityAnimatedEnderman{
	
	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityWanderingEnderman.class);
	//A dataParameter that auto-syncs to the client (not backwards). This is used to define if this should attack.
	private static final DataParameter<Boolean> SPELLCASTING = EntityDataManager.<Boolean>createKey(EntityWanderingEnderman.class, DataSerializers.BOOLEAN);
    public double prevChasingPosX, prevChasingPosY, prevChasingPosZ, chasingPosX, chasingPosY, chasingPosZ;

	static {
		EntityWanderingEnderman.animHandler.addAnim(Reference.MOD_ID, "scream", "enderman", true);
		EntityWanderingEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "enderman", true);
		EntityWanderingEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "enderman", true);
		EntityWanderingEnderman.animHandler.addAnim(Reference.MOD_ID, "lookatenderman", new AnimationLookAtEnderman("Head", "Jaw"));
		EntityWanderingEnderman.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
		EntityWanderingEnderman.animHandler.addAnim(Reference.MOD_ID, "cape", new AnimationCape("Cape"));
	}
	
	public EntityWanderingEnderman(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 2.9F);
		this.stepHeight = 1.0F;
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.isImmuneToFire = true;
	}
	
	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityWanderingEnderman.animHandler;
	}
	
	//This is normally set to true but I changed it to only allow this to despawn in day time.
	@Override
	protected boolean canDespawn() {
		return this.world.isDaytime();
	}
	
	//This a really complicated vanilla method that I added an extra weather check to.
	@Override
	public boolean getCanSpawnHere() {
		return this.world.getWorldInfo().isThundering() && super.getCanSpawnHere();
	}
	
	@Override
	public void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIEndermanLightningAttack(this, 1.2d));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true, false));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));

	}
	
	//Every data parameter must be registered in this method if you do not want the game to crash.
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(SPELLCASTING, Boolean.valueOf(false));
	}
	
	//A setter that sets the parameter and set it dirty (as in minecraft need to sync this to the client). SetDirty is not required but is used for faster sync rates.
	public void setCasting(boolean casting){
		this.dataManager.set(SPELLCASTING, Boolean.valueOf(casting));
		this.dataManager.setDirty(SPELLCASTING);
	}
	
	public boolean getCasting(){
		return this.dataManager.get(SPELLCASTING).booleanValue();
	}
	
	//Set all the default items in this method. I am setting every left handed entity to right handed because I do not want to fix the item renderer in the left hand.
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		if(this.isLeftHanded()){
			this.setLeftHanded(false);
		}
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.thunderWand));
		this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0f);
		return data;
	}
	
	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		updateCape();
	}
	
	@Override
	protected void updateAITasks() {
		if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600) {
			float f = this.getBrightness();

			if (f > 0.5F && this.world.canSeeSky(new BlockPos(this))
					&& this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
				this.setAttackTarget((EntityLivingBase) null);
			}
		}

		if (this.isWet()) {
			this.attackEntityFrom(DamageSource.DROWN, 1.0F);
		}

		super.updateAITasks();
	}
	
	//Cancel the effect since this entity is a master of lightning.
	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		return;
	}
	
	//Do not, I repeat do not try to understand what is happening here. It is related to the animation sequence and when to play them but it is highly unoptimized.
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.isWorldRemote()) {
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
		}
		if (isWorldRemote()) {
			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "scream", this) && !this.isScreaming()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "scream", this);
			}
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "cape", this)) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "cape", 0, this);
			}

			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& !this.getMoving()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				if (!this.isScreaming()) {
					this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				}
			}
			if (this.isScreaming() && !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "scream", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookatenderman", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "scream", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& this.getMoving() && this.deathTime < 1 && !this.isRiding()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this)
					&& !this.isScreaming()
					&& this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "scream", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk_hands", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookatenderman", this)
					&& !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "scream", this)
					&& this.deathTime < 1) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookatenderman", this);
			}
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "riding", this) && this.isRiding()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "riding", this);
			}
			if(this.ticksExisted % 20 == 0 && this.getRNG().nextBoolean()){
				this.spawnParticles(world, this);
			}
		}
	}
	
	private void updateCape()
    {
        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        double d0 = this.posX - this.chasingPosX;
        double d1 = this.posY - this.chasingPosY;
        double d2 = this.posZ - this.chasingPosZ;
        double d3 = 10.0D;

        if (d0 > 10.0D)
        {
            this.chasingPosX = this.posX;
            this.prevChasingPosX = this.chasingPosX;
        }

        if (d2 > 10.0D)
        {
            this.chasingPosZ = this.posZ;
            this.prevChasingPosZ = this.chasingPosZ;
        }

        if (d1 > 10.0D)
        {
            this.chasingPosY = this.posY;
            this.prevChasingPosY = this.chasingPosY;
        }

        if (d0 < -10.0D)
        {
            this.chasingPosX = this.posX;
            this.prevChasingPosX = this.chasingPosX;
        }

        if (d2 < -10.0D)
        {
            this.chasingPosZ = this.posZ;
            this.prevChasingPosZ = this.chasingPosZ;
        }

        if (d1 < -10.0D)
        {
            this.chasingPosY = this.posY;
            this.prevChasingPosY = this.chasingPosY;
        }

        this.chasingPosX += d0 * 0.25D;
        this.chasingPosZ += d2 * 0.25D;
        this.chasingPosY += d1 * 0.25D;
    }
	
	@SideOnly(Side.CLIENT)
	private void spawnParticles(World world, EntityLiving entity){
		world.spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, entity.posX + (this.getRNG().nextDouble() - 0.5), entity.posY + entity.height, entity.posZ + (this.getRNG().nextDouble() - 0.5), (this.getRNG().nextGaussian() - 0.5d)/100d, 0.05, (this.getRNG().nextGaussian() - 0.5d)/100d);
	}

	//Does not matter since we are not using the FindAIPlayer
	@Override
	public boolean shouldAttackPlayer(EntityPlayer player) {
		return false;
	}

}
