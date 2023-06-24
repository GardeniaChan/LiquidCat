package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Flight
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.VecRotation
import net.ccbluex.liquidbounce.utils.block.BlockUtils.Collidable
import net.ccbluex.liquidbounce.utils.block.BlockUtils.collideBlock
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer
import net.ccbluex.liquidbounce.utils.timer.TickTimer
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.block.Block
import net.minecraft.block.BlockLiquid
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemBucket
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.Vec3
import java.util.*
import kotlin.math.ceil


@ModuleInfo(name = "NoFall", description = "Prevents you from taking fall damage.", category = ModuleCategory.PLAYER)
class NoFall : Module() {
    val modeValue = ListValue("Mode", arrayOf("SpoofGround", "NoGround", "Hypixel", "MLG", "AAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "Spartan", "CubeCraft", "AAC4.4.2", "Ghost", "Flag","NewPacket"), "SpoofGround")
    private val minFallDistance = FloatValue("MinMLGHeight", 5f, 2f, 50f)
    private val ghostPacket = ListValue("GhostPacket",arrayOf("Ground","Packet"), "Packet")
    private val spartanTimer = TickTimer()
    private val mlgTimer = TickTimer()
    private var stage = 0
    private var jumped = false
    private var currentMlgRotation: VecRotation? = null
    private var currentMlgItemIndex = 0
    private var currentMlgBlock: BlockPos? = null
    var packetmodify = false
    var packets: MutableList<Packet<*>> = ArrayList()
    var fakelag = false
    override fun onDisable() {
        fakelag = false
        packetmodify = false
        packets.clear()
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val eventState = event.eventState
        if (collideBlock(mc.thePlayer.entityBoundingBox, object : Collidable {
                    override fun collideBlock(block: Block?): Boolean {
                        return block is BlockLiquid
                    }
                }) ||
                collideBlock(AxisAlignedBB(mc.thePlayer.entityBoundingBox.maxX, mc.thePlayer.entityBoundingBox.maxY, mc.thePlayer.entityBoundingBox.maxZ, mc.thePlayer.entityBoundingBox.minX, mc.thePlayer.entityBoundingBox.minY - 0.01, mc.thePlayer.entityBoundingBox.minZ), object : Collidable {
                    override fun collideBlock(block: Block?): Boolean {
                        return block is BlockLiquid
                    }
                })) return
        when(eventState) {
            EventState.PRE -> if(modeValue.get() == "Ghost" && ghostPacket.get() == "Packet" && mc.thePlayer.fallDistance > 0 && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(0.0, -2.0, 0.0).expand(0.0, 2.0, 0.0)).isEmpty())
                mc.netHandler.addToSendQueue(C03PacketPlayer(true))
            EventState.POST -> {
                if(modeValue.get() == "AAC4.4.2") {
                    if (!inVoid()) {
                        if (fakelag) {
                            fakelag = false
                            if (packets.size > 0) {
                                for (packet in packets) {
                                    mc.thePlayer.sendQueue.addToSendQueue(packet)
                                }
                                packets.clear()
                            }
                        }
                        return
                    }
                    if (mc.thePlayer.onGround && fakelag) {
                        fakelag = false
                        if (packets.size > 0) {
                            for (packet in packets) {
                                mc.thePlayer.sendQueue.addToSendQueue(packet)
                            }
                            packets.clear()
                        }
                        return
                    }
                    if (mc.thePlayer.fallDistance > 3 && fakelag) {
                        packetmodify = true
                        mc.thePlayer.fallDistance = 0f
                    }
                    if (inAir(4, 1)) {
                        return
                    }
                    if (!fakelag) {
                        fakelag = true
                    }
                }
            }
        }
    }

    @EventTarget(ignoreCondition = true)
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer.onGround) jumped = false

        if (mc.thePlayer.motionY > 0) jumped = true

        if (!state || LiquidBounce.moduleManager.getModule(FreeCam::class.java)!!.state) return

        if (collideBlock(mc.thePlayer.entityBoundingBox, object : Collidable {
                    override fun collideBlock(block: Block?): Boolean {
                        return block is BlockLiquid
                    }
                }) ||
                collideBlock(AxisAlignedBB(mc.thePlayer.entityBoundingBox.maxX, mc.thePlayer.entityBoundingBox.maxY, mc.thePlayer.entityBoundingBox.maxZ, mc.thePlayer.entityBoundingBox.minX, mc.thePlayer.entityBoundingBox.minY - 0.01, mc.thePlayer.entityBoundingBox.minZ), object : Collidable {
                    override fun collideBlock(block: Block?): Boolean {
                        return block is BlockLiquid
                    }
                })) return
        when (modeValue.get().toLowerCase()) {
            "newpacket" -> {
                if(mc.thePlayer.onGround)
                    mc.thePlayer.fallDistance = 0.5F
                if(!mc.thePlayer.isSpectator && !mc.thePlayer.capabilities.allowFlying && mc.thePlayer.fallDistance > 2) {
                    mc.thePlayer.onGround = false
                    mc.netHandler.addToSendQueue(C03PacketPlayer(true))
                }
            }
            "hypixel" -> if (mc.thePlayer.fallDistance > (2.4F + MovementUtils.getJumpEffect())) {
                if(!mc.thePlayer.isSpectator && !mc.thePlayer.capabilities.allowFlying) {
                    mc.netHandler.addToSendQueue(C03PacketPlayer(true))
                }
            }
            "cubecraft" -> if (mc.thePlayer.fallDistance > 2F) {
                mc.thePlayer.onGround = false
                mc.netHandler.addToSendQueue(C03PacketPlayer(true))
            }
            "aac" -> {
                if (mc.thePlayer.fallDistance > 2F) {
                    mc.netHandler.addToSendQueue(C03PacketPlayer(true))
                    stage = 2
                } else if (stage == 2 && mc.thePlayer.fallDistance < 2) {
                    mc.thePlayer.motionY = 0.1
                    stage = 3
                    return
                }
                when (stage) {
                    3 -> {
                        mc.thePlayer.motionY = 0.1
                        stage = 4
                    }
                    4 -> {
                        mc.thePlayer.motionY = 0.1
                        stage = 5
                    }
                    5 -> {
                        mc.thePlayer.motionY = 0.1
                        stage = 1
                    }
                }
            }
            "laac" -> if (!jumped && mc.thePlayer.onGround && !mc.thePlayer.isOnLadder && !mc.thePlayer.isInWater
                    && !mc.thePlayer.isInWeb) mc.thePlayer.motionY = -6.0
            "aac3.3.11" -> if (mc.thePlayer.fallDistance > 2) {
                mc.thePlayer.motionZ = 0.0
                mc.thePlayer.motionX = mc.thePlayer.motionZ
                mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY - 10E-4, mc.thePlayer.posZ, mc.thePlayer.onGround))
                mc.netHandler.addToSendQueue(C03PacketPlayer(true))
            }
            "aac3.3.15" -> if (mc.thePlayer.fallDistance > 2) {
                if (!mc.isIntegratedServerRunning) mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, 0.0, mc.thePlayer.posZ, false))
                mc.thePlayer.fallDistance = -9999f
            }
            "spartan" -> {
                spartanTimer.update()
                if (mc.thePlayer.fallDistance > 1.5 && spartanTimer.hasTimePassed(10)) {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY + 10, mc.thePlayer.posZ, true))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY - 10, mc.thePlayer.posZ, true))
                    spartanTimer.reset()
                }
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        val mode = modeValue.get()
        if (packet is C03PacketPlayer) {
            when(mode) {
                "AAC4.4.2" -> {
                    if (fakelag && !LiquidBounce.moduleManager.getModule(Flight::class.java)!!.state) {
                        event.cancelEvent()
                        if (packetmodify) {
                            packet.onGround = true
                            packetmodify = false
                        }
                        packets.add(packet)
                    }
                }
                "Flag" -> if (mc.thePlayer.fallDistance > 0) {
                    val height = mc.thePlayer.posY + mc.thePlayer.getEyeHeight()
                    var offset = 0
                    var hasBlock = false
                    while (offset < height) {
                        hasBlock = !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(0.0, -offset.toDouble(), 0.0)).isEmpty()
                        offset += 1
                    }
                    packet.onGround = hasBlock
                }
                "SpoofGround", "NoGround" -> packet.onGround = mode == "SpoofGround"
                "Ghost" -> if(ghostPacket.get() == "Ground" && mc.thePlayer.fallDistance > 0)
                    packet.onGround = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(0.0, -2.0, 0.0).expand(0.0, 2.0, 0.0)).isEmpty()
            }
        }
    }

    private fun inVoid(): Boolean {
        if (mc.thePlayer.posY < 0) {
            return false
        }
        var off = 0.0
        while (off < mc.thePlayer.posY + 2) {
            val bb = AxisAlignedBB(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX, off, mc.thePlayer.posZ)
            if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isNotEmpty()) {
                return true
            }
            off += 2.0
        }
        return false
    }

    private fun inAir(height: Int, plus: Int): Boolean {
        if (mc.thePlayer.posY < 0) return false
        var off = 0
        while (off < height) {
            val bb = AxisAlignedBB(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX, mc.thePlayer.posY - off, mc.thePlayer.posZ)
            if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isNotEmpty()) {
                return true
            }
            off += plus
        }
        return false
    }

    @EventTarget
    fun onMove(event: MoveEvent) {
        if (collideBlock(mc.thePlayer.entityBoundingBox, object : Collidable {
                    override fun collideBlock(block: Block?): Boolean {
                        return block is BlockLiquid
                    }
                }) || collideBlock(AxisAlignedBB(mc.thePlayer.entityBoundingBox.maxX, mc.thePlayer.entityBoundingBox.maxY, mc.thePlayer.entityBoundingBox.maxZ, mc.thePlayer.entityBoundingBox.minX, mc.thePlayer.entityBoundingBox.minY - 0.01, mc.thePlayer.entityBoundingBox.minZ), object : Collidable {
                    override fun collideBlock(block: Block?): Boolean {
                        return block is BlockLiquid
                    }
                })) return
        if (modeValue.get().equals("laac", ignoreCase = true)) {
            if (!jumped && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder && !mc.thePlayer.isInWater && !mc.thePlayer.isInWeb && mc.thePlayer.motionY < 0.0) {
                event.x = 0.0
                event.z = 0.0
            }
        }
    }

    @EventTarget
    private fun onMotionUpdate(event: MotionEvent) {
        if (!modeValue.get().equals("MLG", ignoreCase = true)) return
        if (event.eventState === EventState.PRE) {
            currentMlgRotation = null
            mlgTimer.update()
            if (!mlgTimer.hasTimePassed(10)) return
            if (mc.thePlayer.fallDistance > minFallDistance.get()) {
                val fallingPlayer = FallingPlayer(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY,
                        mc.thePlayer.posZ,
                        mc.thePlayer.motionX,
                        mc.thePlayer.motionY,
                        mc.thePlayer.motionZ,
                        mc.thePlayer.rotationYaw,
                        mc.thePlayer.moveStrafing,
                        mc.thePlayer.moveForward
                )
                val maxDist = mc.playerController.blockReachDistance + 1.5
                val collision = fallingPlayer.findCollision(ceil(1.0 / mc.thePlayer.motionY * -maxDist).toInt())
                        ?: return
                var ok = Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.eyeHeight, mc.thePlayer.posZ).distanceTo(Vec3(collision).addVector(0.5, 0.5, 0.5)) < mc.playerController.blockReachDistance + Math.sqrt(0.75)
                if (mc.thePlayer.motionY < collision.y + 1 - mc.thePlayer.posY) {
                    ok = true
                }
                if (!ok) return
                var index = -1
                for (i in 36..44) {
                    val itemStack = mc.thePlayer.inventoryContainer.getSlot(i).stack
                    if (itemStack != null && (itemStack.item === Items.water_bucket || itemStack.item is ItemBlock && (itemStack.item as ItemBlock).getBlock() === Blocks.web)) {
                        index = i - 36
                        if (mc.thePlayer.inventory.currentItem == index) break
                    }
                }
                if (index == -1) return
                currentMlgItemIndex = index
                currentMlgBlock = collision
                if (mc.thePlayer.inventory.currentItem != index) {
                    mc.thePlayer.sendQueue.addToSendQueue(C09PacketHeldItemChange(index))
                }
                currentMlgRotation = RotationUtils.faceBlock(collision)
            }
        } else if (currentMlgRotation != null) {
            val stack = mc.thePlayer.inventoryContainer.getSlot(currentMlgItemIndex + 36).stack
            if (stack.item is ItemBucket) {
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack)
            } else {
                val dirVec = EnumFacing.UP.directionVec
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, stack, currentMlgBlock, EnumFacing.UP, Vec3(dirVec.x * 0.5, dirVec.y * 0.5, dirVec.z * 0.5).add(Vec3(currentMlgBlock)))) {
                    mlgTimer.reset()
                }
            }
            if (mc.thePlayer.inventory.currentItem != currentMlgItemIndex) mc.thePlayer.sendQueue.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
        }
    }

    @EventTarget(ignoreCondition = true)
    fun onJump(event: JumpEvent) {
        jumped = true
    }

    override val tag: String
        get() = modeValue.get()
}