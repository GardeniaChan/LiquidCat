package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo("JoinGame",description = "//",category = ModuleCategory.MISC)
class JoinGames : Module() {
    private val ModeValue = ListValue("Mode", arrayOf("32V32","None"),"32V23")

    @EventTarget
    fun UpdateEvent(event: UpdateEvent) {
        when(ModeValue.get().toLowerCase()) {
            "32v32" -> mc.thePlayer.sendChatMessage("/game bwxp-32")
        }

        state = false
    }
}