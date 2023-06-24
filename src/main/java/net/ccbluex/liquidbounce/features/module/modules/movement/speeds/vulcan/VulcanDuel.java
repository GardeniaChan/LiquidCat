package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.vulcan;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class VulcanDuel extends SpeedMode {


    public void onMotion() {
        if (MovementUtils.isMoving() && !VulcanDuel.mc.thePlayer.movementInput.jump) {
            if (VulcanDuel.mc.thePlayer.onGround) {
                MovementUtils.strafe(1.1F);
                VulcanDuel.mc.thePlayer.motionY = 0.15D;
            } else {
                MovementUtils.strafe();
            }
        }
    }
    public void onMove(MoveEvent moveevent) {}

    public VulcanDuel() {
        super("VulcanDuel");
    }

    public void onUpdate() {}
}
