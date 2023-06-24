package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PlayerUtil
import net.ccbluex.liquidbounce.utils.timer.TimerUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.*
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

@ModuleInfo(name = "AntiVoid", description = "Anti fall in the void", category = ModuleCategory.MOVEMENT)
class AntiFall : Module() {
    private val fallStopwatch = TimerUtil()
    private val jumpTimer = TimerUtil()
    private var blinkTicks = 0
    private val mode = ListValue("Mode", arrayOf("Hypixel", "Motion"), "Hypixel")
    private val distance = FloatValue("Distance", 8F, 3F, 15F)
    private val speed = FloatValue("StrafeSpeed", 1.5F, 1F, 5f)
    private val motionBlink = BoolValue("MotionBlink", true)
    private val motionNoDamage = BoolValue("MotionNoDamage", false)
    private val onlyVoid = BoolValue("OnlyVoid", true)
    private var hassetback = false
    private var positionY = 0.0
    private val blinkTimer = TimerUtil()
    override val tag: String
        get() = mode.value

    override fun onEnable() {
        jumpTimer.reset()
        blinkTicks = 0
    }

    @EventTarget
    fun onTick(event: UpdateEvent) {
        if (mode.value == "Hypixel") {
            if (mc.thePlayer.fallDistance > distance.value && fallStopwatch.hasReached(500.0) && (!PlayerUtil.isBlockUnder() && onlyVoid.value)) {
                mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + distance.value + 1.0, mc.thePlayer.posZ, false))
                fallStopwatch.reset()
            }
        }
    }

    private var packetPlayer: C03PacketPlayer? = null
    @EventTarget
    fun onPacket(packetEvent: PacketEvent) {
        val packet = packetEvent.packet
        if (packet is C03PacketPlayer) {
            packetPlayer = packet
        }
    }

    @EventTarget
    fun onMove(event: MoveEvent) {
        val blink = checkNotNull(LiquidBounce.moduleManager.getModule("Blink"))
        if (mode.value == "Hypixel") return
        if (mc.thePlayer.fallDistance > distance.value && !mc.thePlayer.capabilities.isFlying && fallStopwatch.hasReached(250.0) && (!PlayerUtil.isBlockUnder() && onlyVoid.value) && mc.thePlayer.motionY <= 0 && (!hassetback || mc.thePlayer.posY <= positionY)) {
           blink.state = motionBlink.value
            mc.thePlayer.motionY = 1.85
            if (motionNoDamage.value) {
                mc.netHandler.addToSendQueue(C03PacketPlayer(true))
            }
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
            MovementUtils.setSpeed(event, 0.0)
            positionY = mc.thePlayer.posY
            hassetback = true
            fallStopwatch.reset()
            blinkTimer.reset()
        }
        if (hassetback) {
            MovementUtils.setSpeed(event, MovementUtils.getBaseMoveSpeed() * speed.value)
            if(blinkTimer.hasReached(1000.0)) {
                blink.state = false
                blinkTimer.reset()
            }
        }
        if (mc.thePlayer.onGround) {
            positionY = 0.0
            hassetback = false
        }
    }
}