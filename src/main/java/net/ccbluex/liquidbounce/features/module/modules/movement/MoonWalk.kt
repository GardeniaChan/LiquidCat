package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.Rotation
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.settings.GameSettings

@ModuleInfo(name = "MoonWalk", description = "Moonwalk over and kill your mother.", category = ModuleCategory.MOVEMENT)
class MoonWalk : Module() {
    private val yaw = FloatValue("yaw",0F,-180F,180F)
    private val pitch = FloatValue("pitch",81F,-90F,90F)
    private val safewalk = BoolValue("SafeWalk",true)
    private val kill = MSTimer()
    private var mother = true

    override fun onDisable() {
        mc.gameSettings.keyBindRight.pressed = false
        mc.gameSettings.keyBindLeft.pressed = false
    }

    @EventTarget
    fun onMove(event: MoveEvent){
        if (safewalk.get()) {
            event.isSafeWalk = true
        }
    }

    @EventTarget
    fun onUpdata(event: UpdateEvent){
        mc.thePlayer.rotationYaw = yaw.get()
        mc.thePlayer.rotationPitch = pitch.get()
            if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight))
                mc.gameSettings.keyBindRight.pressed = false;

            if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft))
                mc.gameSettings.keyBindLeft.pressed = false;

            if (kill.hasTimePassed(100)) {
                mother = !mother;
                kill.reset();
            }

            if (mother) {
                mc.gameSettings.keyBindRight.pressed = true
                mc.gameSettings.keyBindLeft.pressed = false
            } else {
                mc.gameSettings.keyBindRight.pressed = false
                mc.gameSettings.keyBindLeft.pressed = true
            }
    }
}