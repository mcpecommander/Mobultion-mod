package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

/* McpeCommander created on 31/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entityGoals */
public class MobultionSpiderMoveControl extends MoveControl {

    public MobultionSpiderMoveControl(Mob mob) {
        super(mob);
    }

    public void strafe(float forward, float strafe, float speedModifier) {
        this.operation = MoveControl.Operation.STRAFE;
        this.strafeForwards = forward;
        this.strafeRight = strafe;
        this.speedModifier = speedModifier;
    }
}
