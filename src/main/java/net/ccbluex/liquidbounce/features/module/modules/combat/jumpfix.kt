package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox

@ModuleInfo(name = "jumpfix", category = ModuleCategory.COMBAT, description="safe aura")
class jumpfix : Module() {

    @EventTarget
    fun onJump(event: JumpEvent) {
        LiquidBounce.moduleManager[KillAura::class.java]!!.state = false
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        for(entity in mc.theWorld.loadedEntityList) {
            if (mc.thePlayer.getDistanceToEntityBox(entity) < 7 && EntityUtils.isSelected(entity, true)) {
                LiquidBounce.moduleManager[KillAura::class.java]!!.state = true
            }
        }

    }
    

}