package mcpecommander.mobultion.entity.entityAI.spidersAI.sorcererAI;

import java.util.ArrayList;
import java.util.List;

import mcpecommander.mobultion.entity.entities.spiders.EntityHypnoSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntityMagmaSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntitySorcererSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntitySpeedySpider;
import mcpecommander.mobultion.entity.entities.spiders.EntityWitherSpider;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAISorcererSpiderSpellAttack extends EntityAIBase {

	World world;
	private int spellCoolDown = 0;
	private int spellActiveTime;
	private int dSpellActiveTime;
	protected EntitySorcererSpider attacker;

	public EntityAISorcererSpiderSpellAttack(EntitySorcererSpider entity, int spellActiveTime) {
		this.attacker = entity;
		this.world = entity.world;
		this.spellActiveTime = spellActiveTime;
		this.dSpellActiveTime = spellActiveTime;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (this.attacker != null && !this.attacker.isDead) {
			if (this.attacker.getAttackTarget() != null && !this.attacker.getAttackTarget().isDead) {
				return !this.attacker.isSpellcasting() && this.spellCoolDown-- <= 0;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.attacker != null && !this.attacker.isDead) {
			if (this.attacker.getAttackTarget() != null && !this.attacker.getAttackTarget().isDead) {
				return this.attacker.isSpellcasting();
			}
		}
		return false;
	}

	@Override
	public void updateTask() {
		if (this.attacker.ticksExisted % 4 == 0) {
			this.attacker.playSound(SoundEvents.ENTITY_ILLAGER_PREPARE_MIRROR, .5F, .5F);
		}
		if (this.spellActiveTime-- <= 0) {
			this.attacker.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0F, 1.0F);
			this.summonSpiders();
			resetTask();
		}
	}

	public void summonSpiders() {
		int numberOfspiders = this.attacker.getRNG().nextInt(3) + (2
				* (int) (this.world.getDifficultyForLocation(this.attacker.getPosition()).getAdditionalDifficulty()));
		List<EntityLiving> list = getSpidersList(numberOfspiders, this.world);
		for (int i = 0; i < numberOfspiders; i++) {
			EntityLiving spider = list.get(i);
			if (this.attacker.getAttackTarget() != null) {
				double yaw = ((this.attacker.rotationYawHead + 90) * Math.PI) / 180;
				double z = Math.sin(yaw);
				double x = Math.cos(yaw);
				BlockPos pos = new BlockPos(this.attacker.posX + (x * 2) + (3 * this.attacker.getRNG().nextDouble()),
						this.attacker.posY, this.attacker.posZ + (z * 2) + (3 * this.attacker.getRNG().nextDouble()));
				while (!isBlockAccessabile(pos) && !isBlockAccessabile(pos.add(1, 0, 0))
						&& !isBlockAccessabile(pos.add(0, 0, 1)) && !isBlockAccessabile(pos.add(-1, 0, 0))
						&& !isBlockAccessabile(pos.add(0, 0, -1))) {
					if (this.attacker.getRNG().nextBoolean()) {
						pos = pos.add(0, 1, 0);
					} else {
						pos = pos.add(1, 0, 1);
					}

				}
				spider.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
				spider.setAttackTarget(this.attacker.getAttackTarget());
				this.world.spawnEntity(spider);
			}
		}
	}

	private boolean isBlockAccessabile(BlockPos pos) {
		return world.getBlockState(pos).getMaterial() == Material.AIR
				|| (world.getBlockState(pos).getMaterial() == Material.GRASS
						&& !world.getBlockState(pos).causesSuffocation());
	}

	private ArrayList<EntityLiving> getSpidersList(int num, World world) {
		ArrayList<EntityLiving> list = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			int random = this.attacker.getRNG().nextInt(SpidersConfig.spiders.sorcerer.mobsDiffculty);
			switch (random) {
			case 1:
				list.add(new EntitySpider(world));
				break;

			case 2:
				list.add(new EntityCaveSpider(world));
				break;

			case 3:
				list.add(new EntitySpeedySpider(world));
				break;

			case 4:
				list.add(new EntityMagmaSpider(world));
				break;

			case 5:
				list.add(new EntityHypnoSpider(world));
				break;

			case 6:
				list.add(new EntityWitherSpider(world));
				break;

			default:
				list.add(new EntitySpider(world));
				break;
			}
		}
		return list;
	}

	@Override
	public void resetTask() {
		this.attacker.setSpellcasting(false);
		this.spellCoolDown = 100;
		this.spellActiveTime = this.dSpellActiveTime;
	}

	@Override
	public void startExecuting() {
		this.attacker.setSpellcasting(true);
	}

}
