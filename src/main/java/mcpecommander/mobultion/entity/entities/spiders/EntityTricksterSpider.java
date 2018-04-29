package mcpecommander.mobultion.entity.entities.spiders;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.annotation.Nullable;

import mcpecommander.mobultion.entity.entityAI.spidersAI.tricksterAI.EntityAITricksterSpiderConfusionAttack;
import mcpecommander.mobultion.entity.entityAI.spidersAI.tricksterAI.EntityAITricksterSpiderFreezeAttack;
import mcpecommander.mobultion.entity.entityAI.spidersAI.tricksterAI.EntityAITricksterSpiderHeal;
import mcpecommander.mobultion.entity.entityAI.spidersAI.tricksterAI.EntityAITricksterSpiderTeleportAttack;
import mcpecommander.mobultion.particle.ConfuseCloudParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityTricksterSpider extends EntityAnimatedSpider{
	private static final DataParameter<Byte> MODE = EntityDataManager.<Byte>createKey(EntityTricksterSpider.class, DataSerializers.BYTE);
	
	public EntityTricksterSpider(World worldIn) {
		super(worldIn);
		this.setSize(1.4f, 0.9f);
	}

    @Override
	protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAITricksterSpiderFreezeAttack(this));
        this.tasks.addTask(4, new EntityAITricksterSpiderConfusionAttack(this));
        this.tasks.addTask(4, new EntityAITricksterSpiderTeleportAttack(this));
        this.tasks.addTask(4, new EntityAITricksterSpiderHeal(this));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }
    
    @Override
	public double getMountedYOffset()
    {
        return this.height * 0.6F;
    }

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    } 
    
    @Override
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (this.world.rand.nextInt(200) == 0 && !this.world.isRemote)
        {
            EntitySkeleton entityskeleton = new EntitySkeleton(this.world);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData)null);
            this.world.spawnEntity(entityskeleton);
            entityskeleton.startRiding(this);
        }

        return livingdata;
    }

    @Override
	public float getEyeHeight()
    {
        return 0.65F;
    }
    
    @Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(MODE, Byte.valueOf((byte)0));
    }
    
    public void setMode(byte mode){
    	switch(mode){
    	case 0: this.dataManager.set(MODE, Byte.valueOf((byte) 0)); //Turning mode.
    	break;
    	
    	case 1: this.dataManager.set(MODE, Byte.valueOf((byte) 1)); //Teleport Attack mode.
    	break;
    	
    	case 2: this.dataManager.set(MODE, Byte.valueOf((byte) 2)); //Confuse Attack mode.
    	break;
    	
    	case 3: this.dataManager.set(MODE, Byte.valueOf((byte) 3)); //Freeze Attack mode.
    	break;
    	
    	case 4: this.dataManager.set(MODE, Byte.valueOf((byte) 4)); //Healing mode.
    	break;
    	
    	default: this.dataManager.set(MODE, Byte.valueOf((byte) 0)); //Turning mode.
    			 System.out.println("Error," + mode + "is out of range. Mode has been set to default");
    	break;
    	
    	}
    }
    
    public byte getMode(){
    	return this.dataManager.get(MODE).byteValue();
    }
    
    public boolean isHealing(){
    	return this.dataManager.get(MODE).byteValue() == (byte) 4;
    }
    
    public boolean isTeleporting(){
    	return this.dataManager.get(MODE).byteValue() == (byte) 1;
    }
    
    public boolean isFreezing(){
    	return this.dataManager.get(MODE).byteValue() == (byte) 3;
    }
    
    public boolean isConfusing(){
    	return this.dataManager.get(MODE).byteValue() == (byte) 2;
    }
	
	public void spawnEffect(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float scale, int num){
		for(int i = 0; i < num; i++){
			double randx = (this.getRNG().nextDouble()) - .5;
			double randy = (this.getRNG().nextDouble()) - .5;
			double randz = (this.getRNG().nextDouble()) - .5;
			ConfuseCloudParticle newEffect = new ConfuseCloudParticle(worldIn, xCoordIn + randx, yCoordIn + randy, zCoordIn + randz, xSpeedIn, ySpeedIn, zSpeedIn, scale);
			Minecraft.getMinecraft().effectRenderer.addEffect(newEffect);
		}
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(this != null && !this.isDead){
			System.out.println(this.getMode());
			if(this.getMode() == (byte)0){
				byte flag = setAttackMode();
				if(this.getHealth() == this.getMaxHealth() && flag == (byte)4){
					return;	
				}else{
					this.setMode(flag);
				}
			}
		}
	}
	
	private byte setAttackMode(){
		int healingChance = 21;
		int freezingChance = 22;
		int confusingChance = 20;
		int teleportingChance = 40;
		if(this.getHealth() == this.getMaxHealth()){
			healingChance = 0;
		}
		if(this.getHealth() < 10f){
			confusingChance = 31;
			freezingChance = 30;
		}
		if(this.getHealth() < 5F){
			healingChance = 41;
		}
		Map<Byte, Integer> map = new TreeMap<Byte, Integer>();
		map.put((byte) 4, healingChance);
		map.put((byte) 3, freezingChance);
		map.put((byte) 2, confusingChance);
		map.put((byte) 1, teleportingChance);

		return getWeightedRandom(map, this.getRNG()).byteValue();
	}
	
	private static <E> E getWeightedRandom(Map<E, Integer> weights, Random random) {
	    E result = null;
	    double bestValue = Double.MAX_VALUE;

	    for (E element : weights.keySet()) {
	        double value = -Math.log(random.nextDouble()) / weights.get(element);

	        if (value < bestValue) {
	            bestValue = value;
	            result = element;
	        }
	    }
	    return result;
	}
    

}