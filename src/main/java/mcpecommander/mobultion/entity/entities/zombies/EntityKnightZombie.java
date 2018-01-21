package mcpecommander.mobultion.entity.entities.zombies;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIKnightAttackMelee;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIMoveToNearestDoctor;
import mcpecommander.mobultion.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityKnightZombie extends EntityAnimatedZombie{
	
	static {
		EntityKnightZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "zombie", true);
		EntityKnightZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "zombie", true);
		EntityKnightZombie.animHandler.addAnim(Reference.MOD_ID, "knight_slash", "zombie", false);
		EntityKnightZombie.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
		EntityKnightZombie.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}
	
	public EntityKnightZombie(World worldIn) {
		super(worldIn);
        this.setSize(0.7F, 2F);
	}
	
	@Override
	protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIMoveToNearestDoctor(this, 1.2D, 16D));
        this.tasks.addTask(3, new EntityAIKnightAttackMelee(this, 1.0D, false));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
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
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        if(this.getRNG().nextFloat() > 0.7f){
            this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
            if(this.getRNG().nextFloat() > 0.7f){
            	this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.IRON_SWORD));
            }
        }
        this.setEnchantmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = super.attackEntityAsMob(entityIn);

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

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)))
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
    		if(this.getAttacking() && !this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "knight_slash", this) && this.deathTime < 1){
    			this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
        		this.getAnimationHandler().startAnimation(Reference.MOD_ID, "knight_slash", 0, this);
        	}
        	
        	if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this) && this.getMoving() && this.deathTime < 1 && !this.isRiding()){
        			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
        	}
        	
        	if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this) && !this.getAttacking() && this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)){
        		this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "knight_slash", this);
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

}
