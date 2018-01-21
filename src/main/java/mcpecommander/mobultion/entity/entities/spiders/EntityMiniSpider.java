package mcpecommander.mobultion.entity.entities.spiders;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.entityAI.spidersAI.miniAI.EntityAIMiniSpiderAttack;
import mcpecommander.mobultion.entity.entityAI.spidersAI.miniAI.EntityAIMiniSpiderTarget;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMiniSpider extends EntityAnimatedSpider{
	
	static {
    	EntityMiniSpider.animHandler.addAnim(Reference.MOD_ID, "spider_move", "mini_spider", false);
    	EntityMiniSpider.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
	}
	
	public EntityMiniSpider(World worldIn) {
		super(worldIn);
		this.setSize(0.8f, 0.7f);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(4, new EntityAIMiniSpiderAttack(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(2, new EntityAIMiniSpiderTarget(this, EntityMagmaSpider.class, true));
		this.targetTasks.addTask(2, new EntityAIMiniSpiderTarget(this, EntityWitherSpider.class, true));
		this.targetTasks.addTask(2, new EntityAIMiniSpiderTarget(this, EntityHypnoSpider.class, true));
		this.targetTasks.addTask(2, new EntityAIMiniSpiderTarget(this, EntitySorcererSpider.class, true));
		this.targetTasks.addTask(2, new EntityAIMiniSpiderTarget(this, EntitySpeedySpider.class, true));
		this.targetTasks.addTask(2, new EntityAIMiniSpiderTarget(this, EntitySpider.class, true));
		this.targetTasks.addTask(2, new EntityAIMiniSpiderTarget(this, EntityCaveSpider.class, true));
	}

	@Override
	public double getMountedYOffset() {
		return (double) (this.height * 0.6F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(SpidersConfig.spiders.mini.speed);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
	}

	@Override
	public float getEyeHeight() {
		return 0.65F;
	}

//	@Override
//	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
//    	ItemStack skull = new ItemStack(Items.SKULL, 1, 3);
//    	NBTTagCompound tag = new NBTTagCompound();
//    	skull.setTagCompound(tag);
//    	skull.getTagCompound().setString("SkullOwner", "Zealock");
//    	this.setItemStackToSlot(EntityEquipmentSlot.HEAD, skull);
//		return super.onInitialSpawn(difficulty, livingdata);
//	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (!this.isRiding()) {
			this.setLastAttackedEntity(entityIn);
			if (this.getAttackTarget() != null && !this.getAttackTarget().isDead && this != null
					&& !(this.getAttackTarget() instanceof EntityPlayer)) {
				if (!this.getAttackTarget().isBeingRidden()) {
					this.startRiding(getAttackTarget());
				}
			}
			return false;
		} else return super.attackEntityAsMob(entityIn);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
//		if(this.ticksExisted % 20 == 0){
//			System.out.println(this.getAttackTarget());
//		}
		if (this.isRiding()) {
			PotionEffect invis = new PotionEffect(MobEffects.INVISIBILITY, 10, 0, false, false);
			this.addPotionEffect(invis);
			PotionEffect potionEffect = new PotionEffect(MobEffects.STRENGTH, 10, 0, false, true);
			((EntityLivingBase) this.getRidingEntity()).addPotionEffect(potionEffect);
		}
		if (this.world.getDifficulty() == EnumDifficulty.HARD) {
			if (this.isRiding() && !(this.getAttackTarget() instanceof EntityPlayer)) {
				this.setAttackTarget(this.world.getNearestAttackablePlayer(getPosition(),
						getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue(),
						getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()));
			}
		}
		
		if(this.isWorldRemote()){
			if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this)){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", 0, this);
    		}

    		if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "spider_move", this) && this.isMoving()){
    			this.getAnimationHandler().startAnimation(Reference.MOD_ID, "spider_move", 0, this);
    		}
		}
	}

}
