package mcpecommander.mobultion.entity.entities.endermen;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.animation.AnimationRiding;
import mcpecommander.mobultion.entity.entityAI.endermenAI.EntityAIGardenerMoveToFlower;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.mobConfigs.EndermenConfig;
import mcpecommander.mobultion.mobConfigs.EndermenConfig.Endermen.Gardener;
import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityGardenerEnderman extends EntityAnimatedEnderman{
	
	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityGardenerEnderman.class);
	
	static {
		EntityGardenerEnderman.animHandler.addAnim(Reference.MOD_ID, "gardener_water", "gardener_enderman", false);
		EntityGardenerEnderman.animHandler.addAnim(Reference.MOD_ID, "gardener_garden", "gardener_enderman", false);
		EntityGardenerEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk", "gardener_enderman", true);
		EntityGardenerEnderman.animHandler.addAnim(Reference.MOD_ID, "skeleton_walk_hands", "gardener_enderman", true);
		EntityGardenerEnderman.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
		EntityGardenerEnderman.animHandler.addAnim(Reference.MOD_ID, "riding", new AnimationRiding());
	}

	public EntityGardenerEnderman(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 2.9F);
		this.stepHeight = 1.0F;
		
	}
	
	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityGardenerEnderman.animHandler;
	}
	
	private Gardener getConfig() {
		return EndermenConfig.endermen.gardener;
	}
	
	@Override
	public void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityPlayer.class, 4.5F, 1.0D, 1.2D));
		this.tasks.addTask(2, new EntityAIGardenerMoveToFlower(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D, 10));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));

	}
	//Set all the default items in this method. I am setting every left handed entity to right handed because I do not want to fix the item renderer in the left hand.
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		if(this.isLeftHanded()){
			this.setLeftHanded(false);
		}
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.hayHat));
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.WATER_BUCKET));
		this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0f);
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.DYE, 1, 15));
		this.setDropChance(EntityEquipmentSlot.OFFHAND, 0.0f);
		return data;
	}
	
	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
	}

	//A peaceful yet mob(too lazy to fix some major integration issues of not being a mob)
	@Override
	public boolean shouldAttackPlayer(EntityPlayer player) {
		return false;
	}
	
	//A gardener should not trample things.
	@Override
	public boolean canTrample(World world, Block block, BlockPos pos, float fallDistance) {
		return false;
	}
	
	//Do not, I repeat do not try to understand what is happening here. It is related to the animation sequence and when to play them but it is highly unoptimized.
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.isWorldRemote()) {
			this.setMoving(Boolean.valueOf(this.isMoving(this)));
		}
		if (isWorldRemote()) {
//			if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "gardener_garden", this) && !this.getMoving()){
//				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "gardener_garden", 0, this);
//			}

			if (this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& !this.getMoving()) {
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk", this);
				this.getAnimationHandler().stopAnimation(Reference.MOD_ID, "skeleton_walk_hands", this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)
					&& this.getMoving() && this.deathTime < 1 && !this.isRiding()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "skeleton_walk", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk_hands", this)
					&& this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "skeleton_walk", this)) {
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
