package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.Recorder
import net.ccbluex.liquidbounce.utils.render.BlurUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.handshake.client.C00Handshake
import net.minecraft.network.play.server.S02PacketChat


import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*

@ModuleInfo(name = "SessionInfo", category = ModuleCategory.RENDER, description="About game info",array = false)
class SessionInfo : Module() {
    //private val blurValue = BoolValue("blur", true)
    private val xValue = FloatValue("xValue", 100f, 0f, 500f)
    private val yValue = FloatValue("yValue", 100f, 0f, 500f)
    private val blurValue = BoolValue("Blue",false)
    private val r = IntegerValue("R", 0, 0, 255)
    private val g = IntegerValue("G", 0, 0, 255)
    private val b = IntegerValue("B", 0, 0, 255)
    private val a = IntegerValue("alpha", 50, 0, 255)
    private val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
    var syncEntity: EntityLivingBase? = null
    var killCounts = 0
    var totalPlayed = 0
    var win = 0
    var startTime = System.currentTimeMillis()
    @EventTarget
    private fun onRender2D(event: Render2DEvent){
        pushMatrix()
        if(blurValue.get()) {
            BlurUtils.blurArea(xValue.get(),yValue.get()+10F, xValue.get()+120F, yValue.get()+75F, 20F)}
        RenderUtils.drawCircleRect(xValue.get(),yValue.get()+10F, xValue.get()+150F, yValue.get()+90F, 7f,Color(r.get(),g.get(),b.get(),a.get()).rgb)
        Fonts.flux.drawString("F", xValue.get()+5F, yValue.get()+19F, -1)
        Fonts.font40.drawString("Play Time: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}", xValue.get()+20F, yValue.get()+18F, -1)
        Fonts.flux.drawString("G", xValue.get()+5F, yValue.get()+39F, -1)
        Fonts.font40.drawString("Kills: $killCounts", xValue.get()+20F, yValue.get()+38F, -1)
        Fonts.flux.drawString("H", xValue.get()+5F, yValue.get()+59F, -1)
        Fonts.font40.drawString("Win / Total: $win / $totalPlayed", xValue.get()+20F, yValue.get()+58F, -1)
        Fonts.flux.drawString("I", xValue.get()+5F, yValue.get()+79F, -1)
        Fonts.font40.drawString("Speed:"+MovementUtils.getBlockSpeed(mc.thePlayer)+"/bps", xValue.get()+20F, yValue.get()+78F, -1)
        popMatrix()
    }
    @EventTarget
    private fun onAttack(event: AttackEvent) { syncEntity = event.targetEntity as EntityLivingBase?
    }

    @EventTarget
    private fun onUpdate(event: UpdateEvent) {
        if(syncEntity != null && syncEntity!!.isDead) {

            killCounts++

            syncEntity = null
        }
    }
    @EventTarget(ignoreCondition = true)
    private fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (event.packet is C00Handshake) startTime = System.currentTimeMillis()

        if (packet is S02PacketChat) {
            val text = packet.chatComponent.unformattedText
            if(text.contains("GG", true)){
                win++
            }
            if(text.contains("      喜欢      一般      不喜欢", true)){
                totalPlayed++
            }

            }
        }
    }

