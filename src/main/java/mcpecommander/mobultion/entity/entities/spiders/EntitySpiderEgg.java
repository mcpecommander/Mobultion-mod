package mcpecommander.mobultion.entity.entities.spiders;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntitySpiderEgg extends EntityLivingBase implements IAnimated, IMob{
	
	private static final DataParameter<Byte> PREGNANT = EntityDataManager.<Byte>createKey(EntitySpiderEgg.class, DataSerializers.BYTE);
	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntitySpiderEgg.class);
    private AbstractAttributeMap attributeMap;
    private String name;
    private short coolDown = 0;
    private int meta = 0;
    private int revengeTimer;
    private EntityLivingBase target;
    static{
    	EntitySpiderEgg.animHandler.addAnim(Reference.MOD_ID, "egg_hatch", "egg", false);
    }

	public EntitySpiderEgg(World worldIn) {
		super(worldIn);
		this.setSize(.25f, .3f);
		this.setHealth(1f);
		this.setEntityInvulnerable(true);
	}
	
	public EntitySpiderEgg(World world, String playerHead) {
		this(world);
		this.name = playerHead;
	}
	
	public EntitySpiderEgg(World world, int meta) {
		this(world);
		this.meta = meta;
	}
	
	@Override
	protected void collideWithEntity(Entity entityIn) {
		
	}
	
	@Override
	public float getEyeHeight() {
		return 0.15f + this.getHealth()/130f;
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		EntitySpiderEgg.animHandler.animationsUpdate(this);
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000.0D);
	}
	
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(PREGNANT, Byte.valueOf((byte) 0));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if(compound.hasKey("meta")){
			this.meta = compound.getInteger("meta");
		}
		if(compound.hasKey("name")){
			this.name = compound.getString("name");
		}
		if(compound.hasKey("target")){
			this.target = this.world.getPlayerEntityByUUID(compound.getUniqueId("target"));
		}
		this.revengeTimer = compound.getInteger("HurtByTimestamp");
		
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("meta", this.meta);
		compound.setInteger("HurtByTimestamp", this.revengeTimer);
		if(this.name != null){
			compound.setString("name", this.name);
		}
		if(this.target != null && this.target instanceof EntityPlayer){
			compound.setUniqueId("target", this.target.getUniqueID());
		}
	}

	@Override
	public boolean canBeCollidedWith()
    {
        return true;
    }

	@Override
    public float getCollisionBorderSize()
    {
        return .35F;
    }
	
	public void setPregnant(byte n){
		this.dataManager.set(PREGNANT, Byte.valueOf(n));
		this.dataManager.setDirty(PREGNANT);
	}
	
	public void increase(byte n){
		this.dataManager.set(PREGNANT, (byte)(this.dataManager.get(PREGNANT).byteValue() + Byte.valueOf(n)));
		this.dataManager.setDirty(PREGNANT);
	}
	
	public boolean isCoolingDown(){
		return this.dataManager.get(PREGNANT).byteValue() == (byte)-1;
	}
	
	public boolean isPregnant(){
		return this.dataManager.get(PREGNANT).byteValue() >= (byte)1;
	}
	
	public boolean isHatching(){
		return this.dataManager.get(PREGNANT).byteValue() >= (byte)6;
	}
	
	@Override
	public void setRevengeTarget(EntityLivingBase livingBase) {
		super.setRevengeTarget(livingBase);
		if(livingBase instanceof EntityPlayer){
			this.target = livingBase;
		}
		this.revengeTimer += 5;
	}
	
	@Override
	public int getRevengeTimer() {
		return this.revengeTimer;
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		this.setSize(.25f + this.getHealth()/100f, .3f + this.getHealth()/80f);
		if (!this.isWorldRemote()) {
			if(this.revengeTimer > 0){
				this.revengeTimer -= 1;
			}
			
			if (this.getRevengeTimer() <= 0 && this.deathTime <= 0) {
				this.setHealth(this.getHealth() + 0.3125f);
			}
			
			if (this.getHealth() >= 40f) {
				this.setEntityInvulnerable(false);
				if(this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10)).size() <= 10){
					this.coolDown--;
				}
				if (this.coolDown <= 0 && this.isCoolingDown()) {
					this.setPregnant((byte) 0);
				}
				if (!this.isCoolingDown() && this.isHatching()) {
					EntityMob spider = this.getRNG().nextInt(6) != 0 ? new EntitySpider(this.world) : new EntityMiniSpider(this.world);
					BlockPos pos = new BlockPos(posX + (this.getRNG().nextBoolean() ? ((this.getRNG().nextFloat() + 1) * 2)
							: (-(this.getRNG().nextFloat() + 1) * 2)),
					posY, posZ + (this.getRNG().nextBoolean() ? ((this.getRNG().nextFloat() + 1) * 2)
							: (-(this.getRNG().nextFloat() + 1) * 2)));
					while (!isBlockAccessabile(pos) && !isBlockAccessabile(pos.add(1, 0, 0))
							&& !isBlockAccessabile(pos.add(0, 0, 1)) && !isBlockAccessabile(pos.add(-1, 0, 0))
							&& !isBlockAccessabile(pos.add(0, 0, -1))) {
						if (this.getRNG().nextBoolean()) {
							pos = pos.add(0, 1, 0);
						} else {
							pos = pos.add(1, 0, 1);
						}

					}
					spider.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(),	rotationYaw, rotationPitch);
					this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 1f, .5f);
					WorldServer world = (WorldServer) this.world;
					world.spawnParticle(EnumParticleTypes.CLOUD, this.posX, this.posY + 1.5d, this.posZ,
							3, 0, 0, 0, 0.01);
					if(spider instanceof EntitySpider)spider.setAttackTarget(getRevengeTarget());
					this.world.spawnEntity(spider);
					this.coolDown = (short) SpidersConfig.spiders.mother.eggHatchingTime;
					this.setPregnant((byte) -1);
				}
				if (!this.isCoolingDown()) {
					this.increase((byte) 1);
				}

			}
		}
		if(this.isWorldRemote() && this.getHealth() >= 40f){
//			if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "growth", this)){
//				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "growth", 0, this);
//			}
			if(!this.getAnimationHandler().isAnimationActive(Reference.MOD_ID, "egg_hatch", this) && this.isPregnant()){
				this.getAnimationHandler().startAnimation(Reference.MOD_ID, "egg_hatch", 0, this);
			}
		}
//		if(this.ticksExisted >= 1200 && !this.world.isRemote){
//			this.setDead();
//			this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 1f, .5f);
//			EntityMiniSpider mini = new EntityMiniSpider(this.world);
//			if(this.name != null){
//				ItemStack skull = new ItemStack(Items.SKULL, 1, 3);
//				NBTTagCompound tag = new NBTTagCompound();
//				skull.setTagCompound(tag);
//				skull.getTagCompound().setString("SkullOwner", this.name);
//				mini.setItemStackToSlot(EntityEquipmentSlot.HEAD, skull);
//				mini.setDropChance(EntityEquipmentSlot.HEAD, 0.0f);
//			}else if(this.meta != 0){
//				ItemStack skull = new ItemStack(Items.SKULL, 1, this.meta);
//				mini.setItemStackToSlot(EntityEquipmentSlot.HEAD, skull);
//				mini.setDropChance(EntityEquipmentSlot.HEAD, 0.0f);
//			}
//            mini.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
//            this.world.spawnEntity(mini);
//		}
		
	}
	
	private boolean isBlockAccessabile(BlockPos pos) {
		return world.getBlockState(pos).getMaterial() == Material.AIR
				|| (world.getBlockState(pos).getMaterial() == Material.GRASS
						&& !world.getBlockState(pos).causesSuffocation());
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(this.getIsInvulnerable()){
			if(source.getTrueSource() instanceof EntityPlayer){
				this.target = (EntityLivingBase) source.getTrueSource();
			}
			return false;
		}else{
			return super.attackEntityFrom(source, amount);
		}
	}
	

	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntitySpiderEgg.animHandler;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public double getX() {
		return this.posX;
	}

	@Override
	public double getY() {
		return this.posY;
	}

	@Override
	public double getZ() {
		return this.posZ;
	}

	@Override
	public boolean isWorldRemote() {
		return this.world.isRemote;
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		
	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.RIGHT;
	}
	
	
}
