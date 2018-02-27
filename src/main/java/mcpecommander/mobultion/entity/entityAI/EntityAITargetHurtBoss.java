package mcpecommander.mobultion.entity.entityAI;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import mcpecommander.mobultion.entity.entityAI.spidersAI.sorcererAI.EntityAISorcererSpiderTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

/**
 * This will make the mob loyal and attack those how attack the boss defined by a predicate.
 */
public class EntityAITargetHurtBoss extends EntityAIBase{
	
	private EntityMob taskOwner;
	private EntityPlayer target;
	World world;
	private Predicate<EntityPlayer> predicate;

	public EntityAITargetHurtBoss(EntityMob taskOwner, Predicate<EntityPlayer> filter) {
		this.taskOwner = taskOwner;
		this.predicate = filter;
		this.world = taskOwner.world;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if(this.taskOwner != null && !this.taskOwner.isDead){
			List<EntityPlayer> players = this.world.getEntitiesWithinAABB(EntityPlayerMP.class, this.taskOwner.getEntityBoundingBox().grow(10) ,this.predicate);
			if(players.isEmpty()){
				return false;
			}else{
				players.sort(new EntityAISorcererSpiderTarget.Sorter(this.taskOwner));
				this.target = players.get(0);
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateTask() {
		if(this.target != null && !this.target.isDead){
			EntityLivingBase danger = this.target.getRevengeTarget();
			if(danger != null){
				if(!(danger instanceof EntityPigZombie)){
					this.taskOwner.setAttackTarget(danger);
				}
			}
		}
	}
}
