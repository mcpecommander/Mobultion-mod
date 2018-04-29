package mcpecommander.mobultion.entity.entityAI;

import java.util.List;

import com.google.common.base.Predicate;

import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathBoots;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathHelmet;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathLeggings;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathTunic;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EntityAIFollowPlayerWithPigsheath extends EntityAIBase {

	EntityOcelot childAnimal;
	EntityPlayer player;
	double moveSpeed;
	private int delayCounter;

	public EntityAIFollowPlayerWithPigsheath(EntityOcelot animal, double speed) {
		this.childAnimal = animal;
		this.moveSpeed = speed;
	}

	@Override
	public boolean shouldExecute() {
		if(childAnimal.isTamed()){
			return false;
		}
		List<EntityPlayer> list = this.childAnimal.world.getEntitiesWithinAABB(EntityPlayer.class, this.childAnimal.getEntityBoundingBox().grow(10), new Predicate<EntityPlayer>() {
			@Override
			public boolean apply(EntityPlayer input) {
				if ( (!input.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() && input.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemPigsheathHelmet) || 
						(!input.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() && input.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemPigsheathTunic) ||
						(!input.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() && input.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemPigsheathLeggings) ||
						(!input.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty() && input.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemPigsheathBoots) ) {
					return true;
				}
				else return false;
			};
		});

		if (list.isEmpty()) {
			return false;
		} else {
			player = list.get(0);
			return true;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.childAnimal == null || this.childAnimal.isDead || this.player == null || !this.player.isEntityAlive()) {
			return false;
		} else if ((player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() || !(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemPigsheathHelmet)) && 
				(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() || !(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemPigsheathTunic)) && 
				(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() || !(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemPigsheathLeggings)) && 
				(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty() || !(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemPigsheathBoots))) {
			return false;
		} else {
			double d0 = this.childAnimal.getDistanceSq(this.player);
			return d0 >= 9.0D && d0 <= 256.0D;
		}
	}

	@Override
	public void startExecuting() {
		this.delayCounter = 0;
	}

	@Override
	public void resetTask() {
		this.player = null;
	}

	@Override
	public void updateTask() {
		if (--this.delayCounter <= 0) {
			this.delayCounter = 10;
			this.childAnimal.getNavigator().tryMoveToEntityLiving(this.player, this.moveSpeed);
		}
	}

}
