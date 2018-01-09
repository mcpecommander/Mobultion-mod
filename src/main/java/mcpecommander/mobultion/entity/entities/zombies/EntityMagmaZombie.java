package mcpecommander.mobultion.entity.entities.zombies;

import mcpecommander.mobultion.MobsConfig;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.zombiesAI.EntityAIKnightAttackMelee;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.particle.ColoredLavaParticle;
import net.minecraft.client.Minecraft;
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
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityMagmaZombie extends EntityAnimatedZombie{
	
	static {
		EntityMagmaZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "zombie", true);
		EntityMagmaZombie.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "zombie", true);
		EntityMagmaZombie.animHandler.addAnim(Reference.MOD_ID, "knight_slash", "zombie", false);
		EntityMagmaZombie.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
		EntityMagmaZombie.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}

	public EntityMagmaZombie(World worldIn) {
		super(worldIn);
		this.isImmuneToFire = true;
		this.setPathPriority(PathNodeType.WATER, -1.0F);
	}
	
	@Override
	protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIBreakDoor(this));
        this.tasks.addTask(2, new EntityAIKnightAttackMelee(this, 1.0D, false));
        this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(4, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
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
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
    }
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(this.world.isRemote && ticksExisted % 3 == 0 && !this.isWet() && this.deathTime < 1){
	        this.spawnEffect(this.world, this.posX , this.posY + (double)this.height/1.5, this.posZ , 2);

		}
	}
	
	public void spawnEffect(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int num){
		for(int i = 0; i < num; i++){
			ColoredLavaParticle newEffect = new ColoredLavaParticle(worldIn, xCoordIn, yCoordIn, zCoordIn);
			Minecraft.getMinecraft().effectRenderer.addEffect(newEffect);
		}
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.fireSword));
		this.setEnchantmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
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
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = super.attackEntityAsMob(entityIn);
		if(flag){
			if(!this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() 
					&& this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.fireSword){
				entityIn.setFire(3 + this.getRNG().nextInt((int) this.world.getDifficultyForLocation(getPosition()).getAdditionalDifficulty()));
			}
		}
		return flag;
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if(!this.isWorldRemote() && MobsConfig.zombies.magma.lavaMaking){
			if(cause.getTrueSource() instanceof EntityPlayerMP){
				EntityPlayerMP player = (EntityPlayerMP) cause.getTrueSource();
				if(player.getHeldItemMainhand().getItem() == ModItems.fireSword){
					player.getHeldItemMainhand().damageItem(25, player);
					if(this.world.isAirBlock(getPosition()) && this.world.isSideSolid(getPosition().add(0, -1, 0), EnumFacing.UP)){
						this.world.setBlockState(getPosition(), Blocks.FLOWING_LAVA.getDefaultState(), 3);
					}
				}
			}
		}
	}
	
	@Override
    protected void updateAITasks(){
    	if (this.isWet() && MobsConfig.zombies.magma.wetDamage)
        {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }
    	super.updateAITasks();
    }

}
