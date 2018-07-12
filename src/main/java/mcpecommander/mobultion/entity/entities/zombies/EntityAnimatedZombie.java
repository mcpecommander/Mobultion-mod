package mcpecommander.mobultion.entity.entities.zombies;

import java.util.UUID;

import javax.annotation.Nullable;

import com.leviathanstudio.craftstudio.CraftStudioApi;
import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.integration.JEI;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public abstract class EntityAnimatedZombie extends EntityMob implements IAnimated{
	
	private static final DataParameter<Boolean> ATTACKING = EntityDataManager.<Boolean>createKey(EntityAnimatedZombie.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> MOVING = EntityDataManager.<Boolean>createKey(EntityAnimatedZombie.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.<Boolean>createKey(EntityAnimatedZombie.class, DataSerializers.BOOLEAN);
	private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, 1);
	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityAnimatedZombie.class);

	public EntityAnimatedZombie(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(IS_CHILD, Boolean.valueOf(false));
        this.getDataManager().register(MOVING, Boolean.valueOf(false));
        this.getDataManager().register(ATTACKING, Boolean.valueOf(false));
    }
	
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if(this.world.isRemote) {
			if(player instanceof EntityPlayerSP && hand.equals(EnumHand.MAIN_HAND) && player.getHeldItemMainhand().isEmpty() && player.isCreative() && GuiScreen.isShiftKeyDown()){
				ItemStack stack = new ItemStack(Items.SPAWN_EGG, 1);
				for(EntityEntry entry : ForgeRegistries.ENTITIES.getValuesCollection()){
					if(entry.getEntityClass() == this.getClass()){				
						ItemMonsterPlacer.applyEntityIdToItemStack(stack, entry.getRegistryName());
					}
				}
				if(stack.getTagCompound().hasKey("EntityTag") && Loader.isModLoaded("jei")){
					JEI.JEIShowGUI(stack);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean getMoving(){
    	return this.dataManager.get(MOVING).booleanValue();
    }

	public void setMoving(boolean isMoving){
		this.dataManager.set(MOVING, Boolean.valueOf(isMoving));
		this.dataManager.setDirty(MOVING);
	}
	
	public boolean isMoving(EntityLiving entity){
    	if(!entity.getMoveHelper().isUpdating()){
    		return Math.abs(entity.moveForward) > 0.01f || Math.abs(entity.moveStrafing) > 0.1f || Math.abs(entity.moveVertical) > 0.1f;
    	}return true;
    }
	
	public void setAttacking(boolean attacking){
		if(this.dataManager.get(ATTACKING).booleanValue() != attacking){
			this.dataManager.set(ATTACKING, Boolean.valueOf(attacking));
			this.dataManager.setDirty(ATTACKING);
		}
	}
	
	public boolean getAttacking(){
		return this.dataManager.get(ATTACKING).booleanValue();
	}
	
	@Override
	public boolean isChild()
    {
        return this.getDataManager().get(IS_CHILD).booleanValue();
    }
	
	public void setChild(boolean childZombie)
    {
        this.getDataManager().set(IS_CHILD, Boolean.valueOf(childZombie));

        if (this.world != null && !this.world.isRemote)
        {
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(BABY_SPEED_BOOST);

            if (childZombie)
            {
                iattributeinstance.applyModifier(BABY_SPEED_BOOST);
            }
        }

        this.setChildSize(childZombie);
    }
	
	@Override
	public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (IS_CHILD.equals(key))
        {
            this.setChildSize(this.isChild());
        }

        super.notifyDataManagerChange(key);
    }
	
	@Override
	protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

	@Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

	@Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_ZOMBIE;
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (this.isChild())
        {
            compound.setBoolean("IsBaby", true);
        }

    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.getBoolean("IsBaby"))
        {
            this.setChild(true);
        }

    }
    
    @Override
    public float getEyeHeight()
    {
        float f = 1.74F;

        if (this.isChild())
        {
            f = (float)(f - 0.81D);
        }

        return f;
    }
    
    @Override
    public void onLivingUpdate() {
    	super.onLivingUpdate();
    	this.getAnimationHandler().animationsUpdate(this);
    }
    
    public void setChildSize(boolean isChild)
    {
        this.multiplySize(isChild ? 0.8F : 1F);
    }
    
    @Override
    protected final void setSize(float width, float height)
    {
        boolean flag = this.width > 0.0F && this.height > 0.0F;

        if (!flag)
        {
            this.multiplySize(1.0F);
        }
    }
    
    protected final void multiplySize(float size)
    {
        super.setSize(this.width * size, this.height * size);
    }
    
    @Override
    public double getYOffset()
    {
        return this.isChild() ? 0.0D : -0.45D;
    }
    
    @Override
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        float f = difficulty.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);

        if (livingdata == null)
        {
            livingdata = new EntityAnimatedZombie.GroupData(this.world.rand.nextFloat() < 0.09f);
        }

        if (livingdata instanceof EntityAnimatedZombie.GroupData)
        {
        	EntityAnimatedZombie.GroupData data = (EntityAnimatedZombie.GroupData)livingdata;

            if (data.isChild)
            {
                this.setChild(true);
            }
        }

//        this.setEquipmentBasedOnDifficulty(difficulty);
//        this.setEnchantmentBasedOnDifficulty(difficulty);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
        double d0 = this.rand.nextDouble() * 1.5D * f;

        if (d0 > 1.0D)
        {
            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }

        return livingdata;
    }

	@Override
	public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
		return EntityAnimatedZombie.animHandler;
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
	
	class GroupData implements IEntityLivingData
    {
        public boolean isChild;

        private GroupData(boolean isChild)
        {
            this.isChild = isChild;
        }
    }

}
