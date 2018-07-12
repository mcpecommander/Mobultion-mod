package mcpecommander.mobultion.entity.entities.spiders;

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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public abstract class EntityAnimatedSpider extends EntityMob implements IAnimated{

	private static final DataParameter<Boolean> MOVING = EntityDataManager.<Boolean>createKey(EntityAnimatedSpider.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntityAnimatedSpider.class, DataSerializers.BYTE);
	protected static AnimationHandler animHandler = CraftStudioApi.getNewAnimationHandler(EntityAnimatedSpider.class);
	
	public EntityAnimatedSpider(World worldIn) {
		super(worldIn);
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
	
	@Override
	protected abstract void initEntityAI();
    
	@Override
    public abstract double getMountedYOffset();

    @Override
    protected PathNavigate createNavigator(World worldIn)
    {
        return new PathNavigateClimber(this, worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(CLIMBING, Byte.valueOf((byte)0));
        this.dataManager.register(MOVING, Boolean.valueOf(false));
    }
    
	public void setMoving(boolean isMoving){
		this.dataManager.set(MOVING, Boolean.valueOf(isMoving));
		this.dataManager.setDirty(MOVING);
	}
	
	private boolean isMoving(EntityLiving entity){
    	if(!entity.getMoveHelper().isUpdating()){
    		return Math.abs(entity.moveForward) > 0.01f || Math.abs(entity.moveStrafing) > 0.1f || Math.abs(entity.moveVertical) > 0.1f;
    	}return true;
    }
	
	public boolean isMoving(){
		return this.dataManager.get(MOVING).booleanValue();
	}
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.world.isRemote)
        {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_SPIDER;
    }
    
    @Override
    public boolean isOnLadder()
    {
        return this.isBesideClimbableBlock();
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }
    
    public boolean isBesideClimbableBlock()
    {
        return (this.dataManager.get(CLIMBING).byteValue() & 1) != 0;
    }
    
    public void setBesideClimbableBlock(boolean climbing)
    {
        byte b0 = this.dataManager.get(CLIMBING).byteValue();

        if (climbing)
        {
            b0 = (byte)(b0 | 1);
        }
        else
        {
            b0 = (byte)(b0 & -2);
        }

        this.dataManager.set(CLIMBING, Byte.valueOf(b0));
    }

    @Override
    public abstract float getEyeHeight();

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
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public boolean isWorldRemote() {
		return this.world.isRemote;
	}

	@Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!this.world.isRemote){
    		this.setMoving(Boolean.valueOf(this.isMoving(this)));
    	}
        this.getAnimationHandler().animationsUpdate(this);
    }
    
    @Override
    public <T extends IAnimated> AnimationHandler<T> getAnimationHandler() {
        return EntityAnimatedSpider.animHandler;
    }
}
