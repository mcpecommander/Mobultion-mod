package mcpecommander.mobultion.entity.entities.spiders;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.animation.AnimationLookAt;
import mcpecommander.mobultion.entity.entityAI.spidersAI.angelAI.EntityAIAngelSpiderHeal;
import mcpecommander.mobultion.entity.entityAI.spidersAI.angelAI.EntityAIAngelSpiderTarget;
import mcpecommander.mobultion.init.ModPotions;
import mcpecommander.mobultion.particle.HealParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityAngelSpider extends EntityAnimatedSpider {

	private static final DataParameter<Integer> TARGET = EntityDataManager.<Integer>createKey(EntityAngelSpider.class,
			DataSerializers.VARINT);
	
	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityAngelSpider.class);

	static {
		EntityAngelSpider.animHandler.addAnim(Reference.MOD_ID, "spider_move", "angel_spider", false);
		EntityAngelSpider.animHandler.addAnim(Reference.MOD_ID, "wings_flap", "angel_spider", true);
		EntityAngelSpider.animHandler.addAnim(Reference.MOD_ID, "lookat", new AnimationLookAt("Head"));
	}

	public EntityAngelSpider(World worldIn) {
		super(worldIn);
		this.setSize(1.4f, 1f);
	}
	
	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityAngelSpider.animHandler;
	}

	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
		super.setAttackTarget(entitylivingbaseIn);
		if (entitylivingbaseIn != null) {
			this.dataManager.set(TARGET, entitylivingbaseIn.getEntityId());
			this.dataManager.setDirty(TARGET);
		} else {
			this.dataManager.set(TARGET, -1);
			this.dataManager.setDirty(TARGET);
		}
	}

	public Entity getTarget() {
		return this.world.getEntityByID(this.dataManager.get(TARGET));
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityPlayer.class, 3.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIAngelSpiderHeal(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIAngelSpiderTarget(this));
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return Reference.LootTables.ENTITYANGELSPIDER;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(TARGET, -1);
	}

	@Override
	public double getMountedYOffset() {
		return this.height * 0.6F;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
	}

	@Override
	public float getEyeHeight() {
		return 0.65F;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.getTarget() != null && ((EntityLivingBase) this.getTarget()).getAttributeMap()
				.getAttributeInstanceByName("generic.blessed").getAttributeValue() > 0.0D) {
			if (((EntityLivingBase) this.getTarget()).getActivePotionEffect(ModPotions.potionBlessed) == null) {
				PotionEffect effect = new PotionEffect(ModPotions.potionBlessed, Integer.MAX_VALUE, 0, false, false);
				((EntityLivingBase) this.getTarget()).addPotionEffect(effect);
			}
		}
		if (this.isWorldRemote()) {
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "lookat", this)) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "lookat", 0, this);
			}

			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "spider_move", this)
					&& this.isMoving()) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "spider_move", 0, this);
			}
			if (!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "wings_flap", this)) {
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "wings_flap", 0, this);
			}
			if (this.getTarget() != null && ticksExisted % 5 == 0) {
				performEffect();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void performEffect(){
		Vec3d vec3d = new Vec3d(this.posX - this.getTarget().posX,
				this.getEntityBoundingBox().minY + this.getEyeHeight()
						- (this.getTarget().posY + this.getTarget().getEyeHeight()),
				this.posZ - this.getTarget().posZ).normalize();
		HealParticle heal = new HealParticle(world, this.posX, this.posY + this.getEyeHeight(), this.posZ,
				this.getRNG().nextFloat(), vec3d,
				this.getTarget().getEntityBoundingBox());
		Minecraft.getMinecraft().effectRenderer.addEffect(heal);
	}
	
	
}
