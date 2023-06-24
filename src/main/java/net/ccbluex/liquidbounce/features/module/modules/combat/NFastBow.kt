package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.item.ItemBow
import net.minecraft.network.play.client.C03PacketPlayer


@ModuleInfo(name = "NewFastBow", description = "LLL", category = ModuleCategory.COMBAT)
class NFastBow : Module() {

    private val speedValue = IntegerValue("speed", 3, 1, 20)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().item is ItemBow) {
            repeat(speedValue.get()){
                mc.netHandler.addToSendQueue(C03PacketPlayer.C05PacketPlayerLook())
            }
            mc.thePlayer.itemInUseCount = mc.thePlayer.inventory.getCurrentItem().maxItemUseDuration - speedValue.get()
        }
    }


}