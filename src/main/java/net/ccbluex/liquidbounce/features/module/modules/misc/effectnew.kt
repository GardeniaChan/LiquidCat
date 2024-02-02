package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.ClientUtils

@ModuleInfo("effectshow",category = ModuleCategory.MISC,description = "lllll")
class effectnew : Module(){
    
    private val k = LiquidBounce.moduleManager[KillAura::class.java]!! as KillAura
    private var i = 0
    
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
       val a =  mc.thePlayer.getActivePotionEffects()
        ClientUtils.displayAlert(a.toString())
    }

}