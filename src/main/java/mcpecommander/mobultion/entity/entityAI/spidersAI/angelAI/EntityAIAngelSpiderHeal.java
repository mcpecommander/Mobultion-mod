package mcpecommander.mobultion.entity.entityAI.spidersAI.angelAI;

import java.util.UUID;

import mcpecommander.mobultion.entity.entities.spiders.EntityAngelSpider;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAIAngelSpiderHeal extends EntityAIBase {

	World world;
	private EntityAngelSpider attacker;
	private int healCoolDown;
	private static final UUID BLESSED_ID = UUID.fromString("020E0DFB-87AE-4653-9556-561210E291A0");
	private static final AttributeModifier BLESSED = (new AttributeModifier(BLESSED_ID, "blessed", 0.1D, 0))
			.setSaved(true);

	public EntityAIAngelSpiderHeal(EntityAngelSpider entity) {
		this.attacker = entity;
		this.world = entity.world;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (this.attacker == null || this.attacker.isDead) {
			return false;
		} else if (this.attacker.getAttackTarget() != null) {
			if (this.attacker.getAttackTarget().getHealth() < this.attacker.getAttackTarget().getMaxHealth()) {
				return this.attacker.getHealth() > 4f;
			} else {
				this.attacker.setAttackTarget(null);
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		if (this.attacker != null && !this.attacker.isDead) {
			if (this.attacker.getAttackTarget() != null && !this.attacker.getAttackTarget().isDead) {
				this.healCoolDown = 100;
			}
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.attacker == null || this.attacker.isDead) {
			return false;
		} else {
			return this.healCoolDown != 0;
		}
	}

	@Override
	public void updateTask() {
		this.healCoolDown = Math.max(this.healCoolDown - 1, 0);
		if (this.attacker.getAttackTarget() != null && !this.attacker.getAttackTarget().isDead) {
			EntityLivingBase entity = this.attacker.getAttackTarget();
			this.attacker.getLookHelper().setLookPositionWithEntity(entity, 30f, 30f);
			if (this.healCoolDown < 10) {
				entity.heal(SpidersConfig.spiders.angel.healAmount);
				if (this.attacker.isServerWorld()) {
					WorldServer temp = (WorldServer) this.attacker.getEntityWorld();
					temp.spawnParticle(EnumParticleTypes.HEART, true, this.attacker.getAttackTarget().posX,
							this.attacker.getAttackTarget().posY + this.attacker.getAttackTarget().getEyeHeight(),
							this.attacker.getAttackTarget().posZ, 2, 0.0f, 0.0f, 0.0f, 0.01f);

					IAttributeInstance iattributeinstance = entity.getAttributeMap()
							.getAttributeInstanceByName("generic.blessed");
					if (!iattributeinstance.hasModifier(BLESSED) && iattributeinstance.getAttributeValue() == 0.0D) {
						iattributeinstance.applyModifier(BLESSED);
					}
				}
				this.healCoolDown = 0;
			}
		}
	}

}
