package mcpecommander.luggagemod.entity.entities.skeletons;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.entity.animation.AnimationLookAt;
import mcpecommander.luggagemod.entity.animation.AnimationRiding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityCorruptedSkeleton extends EntityAnimatedSkeleton{
	private boolean flag = false;
    
    static {
    	EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_death", "forest_skeleton", false);
    	EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "forest_skeleton", true);
    	EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "forest_skeleton", true);
    	EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "skeleton_holding_bow", "forest_skeleton", true);
    	EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
    	EntityWitheringSkeleton.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
    }
    
	public EntityCorruptedSkeleton(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.99F);
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.2d, false));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
	}
	
	@Override
	public double getYOffset() {
		return -0.43D;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	@Override
	public float getEyeHeight() {
		return 1.74F;
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(this.getEquip()));
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
    }
    
    @Override
    protected void onDeathUpdate() {
    	super.onDeathUpdate();
    	this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
    	if (this.isWorldRemote() && !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_death", this) && !flag){
    		this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
    		this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
    		this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_holding_bow", this);
    		this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "lookat", this);
            this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_death", 0, this);
            flag = true;
    	}
    }
    
    @Override
    public void onLivingUpdate() {
    	super.onLivingUpdate();
    	if(!this.world.isRemote){
    		this.setMoving(Boolean.valueOf(this.isMoving(this)));
    		if(this.isDead){
    			EntitySkeletonRemains grave = new EntitySkeletonRemains(this.world, this);
				grave.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            	this.world.spawnEntity(grave);
    		}
		}
    	if(this.isWorldRemote()){
    		if(this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this) && !this.getMoving()){
    			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
    			if(!this.isSwingingArms()){
    				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
    			}
    		}
    		if(this.isSwingingArms() && !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_holding_bow", this) && this.deathTime < 1){
    			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
        		this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_holding_bow", 0, this);
        	}
        	
        	if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this) && this.getMoving() && this.deathTime < 1 && !this.isRiding()){
        			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
        	}
        	
        	if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this) && !this.isSwingingArms() && this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)){
        		this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_holding_bow", this);
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk_hands", 0, this);
    		}

        	if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this) && this.deathTime < 1){
                this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", this);
        	}
        	if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "riding", this) && this.isRiding()){
        		this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
        		this.getAnimationHandler().startAnimation(Reference.MOD_ID, "riding", this);  
        	}
    	}
    	
    }
    
    @Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = super.attackEntityAsMob(entityIn);

		if (flag) {
			float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();

			if (this.rand.nextFloat() < f * 0.6F) {
				PotionEffect potioneffect = new PotionEffect(MobEffects.MINING_FATIGUE, 100, 0);
				((EntityLivingBase) entityIn).addPotionEffect(potioneffect);
				PotionEffect potioneffect2 = new PotionEffect(MobEffects.SLOWNESS, 100, 0);
				((EntityLivingBase) entityIn).addPotionEffect(potioneffect2);
			}
		}

		return flag;
	}
    
    @Override
	protected EntityArrow getArrow(float distanceFactor) {
		return null;
	}
	
	@Override
	public Item getEquip() {
		return Items.BONE;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public boolean isWorldRemote() {
		return this.world.isRemote;
	}

}
