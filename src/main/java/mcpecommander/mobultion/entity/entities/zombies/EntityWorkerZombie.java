package mcpecommander.mobultion.entity.entities.zombies;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIKnightAttackMelee;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIMoveToNearestDoctor;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.mobConfigs.ZombiesConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityWorkerZombie extends EntityAnimatedZombie{
	
	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityWorkerZombie.class);
	static {
		EntityWorkerZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "zombie", true);
		EntityWorkerZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "zombie", true);
		EntityWorkerZombie.animHandler.addAnim(Reference.MOD_ID, "worker_hammering", "zombie", false);
		EntityWorkerZombie.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
		EntityWorkerZombie.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}
	
	public EntityWorkerZombie(World worldIn) {
		super(worldIn);
        this.setSize(0.7F, 2F);
	}
	
	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityWorkerZombie.animHandler;
	}
	
	@Override
	protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIMoveToNearestDoctor(this, 1.2D, 16D));
        this.tasks.addTask(2, new EntityAIBreakDoor(this));
        this.tasks.addTask(3, new EntityAIKnightAttackMelee(this, 1.0D, false));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0] ));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.hammer));
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.01f);
        this.setEnchantmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = super.attackEntityAsMob(entityIn);
        if(entityIn instanceof EntityPlayerMP ){
        	if(ZombiesConfig.zombies.worker.hammerAttack){
	        	EntityPlayerMP player = (EntityPlayerMP) entityIn;
	        	player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), 50);
	        	player.connection.setPlayerLocation(player.posX, player.posY - 1.1, player.posZ, player.rotationYaw, player.rotationPitch );
        	}else{
        		EntityPlayerMP player = (EntityPlayerMP) entityIn;
        		player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 5, 1));
        	}
        }

        if (flag)
        {
            float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();

            if (this.isBurning() && this.rand.nextFloat() < f * 0.3F)
            {
                entityIn.setFire(2 * (int)f);
            }
        }

        return flag;
    }
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.world.isDaytime() && !this.world.isRemote && !this.isChild())
        {
            float f = this.getBrightness();

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + this.getEyeHeight(), this.posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                if (!itemstack.isEmpty())
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
                        {
                            this.renderBrokenItemStack(itemstack);
                            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    this.setFire(8);
                }
            }
        }
		if(!this.isWorldRemote()){
    		this.setMoving(Boolean.valueOf(this.isMoving(this)));
		}
		if(this.isWorldRemote()){
    		if(this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this) && !this.getMoving()){
    			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
    			if(!this.getAttacking()){
    				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
    			}
    		}
    		if(this.getAttacking() && !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "worker_hammering", this) && this.deathTime < 1){
    			this.getAnimationHandler().stopStartAnimation(Reference.MOD_ID, "skeleton_walk_hands", Reference.MOD_ID, "worker_hammering", 0, this);
        	}
        	
        	if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this) && this.getMoving() && this.deathTime < 1 && !this.isRiding()){
        			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
        	}
        	
        	if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this) && !this.getAttacking() && this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)){
        		this.getAnimationHandler().stopStartAnimation(Reference.MOD_ID, "worker_hammering", Reference.MOD_ID, "skeleton_walk_hands", 0, this);
    		}

        	if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this) && this.deathTime < 1){
                this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", this);
        	}
        	if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "riding", this) && this.isRiding()){
        		this.getAnimationHandler().stopStartAnimation(Reference.MOD_ID, "skeleton_walk", Reference.MOD_ID, "riding", 0, this);
        	}
    	}
		
	}

}
