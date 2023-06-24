/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.ui.client

import net.cat.CMain
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraft.world.demo.DemoWorldServer
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.io.IOException

class GuiMainMenu : GuiScreen(), GuiYesNoCallback {

    var slideX : Float = 0F
    var fade : Float = 0F

    var sliderX : Float = 0F

    var lastAnimTick: Long = 0L
    var alrUpdate = false

    var lastXPos = 0F

    companion object {
        var useParallax = true
    }

    override fun initGui() {
        slideX = 0F
        fade = 0F
        sliderX = 0F
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (!alrUpdate) {
            lastAnimTick = System.currentTimeMillis()
            alrUpdate = true
        }
        val creditInfo = "Copyright Mojang AB. Do not distribute!"
        drawBackground(0)
        GL11.glPushMatrix()
        renderSwitchButton()
        Fonts.font40.drawStringWithShadow("${CMain.Name} build ${CMain.Ver}", 2F, height - 12F, -1)
        //Fonts.font40.drawStringWithShadow(creditInfo, width - 3F - Fonts.font40.getStringWidth(creditInfo), height - 12F, -1)
        if (useParallax) moveMouseEffect(mouseX, mouseY, 10F)
        GlStateManager.disableAlpha()
        //RenderUtils.drawImage2(bigLogo, width / 2F - 50F, height / 2F - 90F, 100, 100)
        GlStateManager.enableAlpha()
        renderBar(mouseX, mouseY, partialTicks)
        GL11.glPopMatrix()
        super.drawScreen(mouseX, mouseY, partialTicks)
        
        if (!LiquidBounce.mainMenuPrep) {
            val animProgress = ((System.currentTimeMillis() - lastAnimTick).toFloat() / 2000F).coerceIn(0F, 1F)
            RenderUtils.drawRect(0F, 0F, width.toFloat(), height.toFloat(), Color(0F, 0F, 0F, 1F - animProgress))
            if (animProgress >= 1F)
                LiquidBounce.mainMenuPrep = true    
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (!LiquidBounce.mainMenuPrep || mouseButton != 0) return

        if (isMouseHover(2F, height - 22F, 28F, height - 12F, mouseX, mouseY))
            useParallax = !useParallax

        val staticX = width / 2F - 120F
        val staticY = height / 2F + 20F
        var index: Int = 0
        for (icon in ImageButton.values()) {
            if (isMouseHover(staticX + 40F * index, staticY, staticX + 40F * (index + 1), staticY + 20F, mouseX, mouseY))
                when (index) {
                    0 -> mc.displayGuiScreen(GuiSelectWorld(this))
                    1 -> mc.displayGuiScreen(GuiMultiplayer(this))
                    2 -> mc.displayGuiScreen(GuiAltManager(this))
                    3 -> mc.displayGuiScreen(GuiOptions(this, this.mc.gameSettings))
                    4 -> mc.displayGuiScreen(GuiModsMenu(this))
                    5 -> mc.shutdown()
                }
            
            index++
        }

        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun moveMouseEffect(mouseX: Int, mouseY: Int, strength: Float) {
        val mX = mouseX - width / 2
        val mY = mouseY - height / 2
        val xDelta = mX.toFloat() / (width / 2).toFloat()
        val yDelta = mY.toFloat() / (height / 2).toFloat()
        
        GL11.glTranslatef(xDelta * strength, yDelta * strength, 0F)
    }

    fun renderSwitchButton() {
        sliderX += if (useParallax) 2F else -2F
        if (sliderX > 12F) sliderX = 12F
        else if (sliderX < 0F) sliderX = 0F
        Fonts.font40.drawStringWithShadow("Parallax", 28F, height - 25F, -1)
        RenderUtils.drawRoundedRect(4F, height - 24F, 22F, height - 18F, 3F, if (useParallax) Color(0, 111, 255, 255).rgb else Color(140, 140, 140, 255).rgb)
        RenderUtils.drawRoundedRect(2F + sliderX, height - 26F, 12F + sliderX, height - 16F, 5F, Color.white.rgb)
    }


    fun renderBar(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val staticX = width / 2F - 120F
        val staticY = height / 2F + 20
        //按钮背景
       RenderUtils.drawRect(staticX, staticY-20F, staticX + 240F, staticY + 20F, Color(255, 255, 255, 100).rgb)
       RenderUtils.drawRect(staticX,staticY - 1F,staticX + 240F,staticY,Color(65,197,255,255))
        var misans35 = Fonts.regular35
        misans35.drawString(CMain.Name,staticX + 2F,staticY-16F,Color(255,255,255).rgb)
        var s = "Player: " + mc.session.username
        misans35.drawString(s,staticX + 240F - misans35.getStringWidth(s),staticY - 16F,Color(255,255,255).rgb)

        var index: Int = 0
        var shouldAnimate = false
        var displayString: String? = null
        var moveX = 0F
        for (icon in ImageButton.values()) {
            if (isMouseHover(staticX + 40F * index, staticY, staticX + 40F * (index + 1), staticY + 20F, mouseX, mouseY)) {
                shouldAnimate = true
                displayString = icon.buttonName
                moveX = staticX + 40F * index
            }
            index++
        }

        if (displayString != null)
            Fonts.font35.drawCenteredString(displayString!!, width / 2F, staticY + 30F, -1)

        if (shouldAnimate) {
            if (fade == 0F)
                slideX = moveX
            else


            lastXPos = moveX

            fade += 10F
            if (fade >= 100F) fade = 100F
        } else {
            fade -= 10F
            if (fade <= 0F) fade = 0F


        }

        if (fade != 0F)
            RenderUtils.drawRect(slideX, staticY, slideX + 40F, staticY + 20F, Color(1F, 1F, 1F, fade / 100F * 0.6F).rgb)

        index = 0
        GlStateManager.disableAlpha()
        for (i in ImageButton.values()) {
            RenderUtils.drawImage2(i.texture, staticX + 40F * index + 11F, staticY + 1F, 18, 18)
            index++
        }
        GlStateManager.enableAlpha()
    }

    fun isMouseHover(x: Float, y: Float, x2: Float, y2: Float, mouseX: Int, mouseY: Int): Boolean = mouseX >= x && mouseX < x2 && mouseY >= y && mouseY < y2

    enum class ImageButton(val buttonName: String, val texture: ResourceLocation) {
        Single("Singleplayer", ResourceLocation("CAT/menu/singleplayer.png")),
        Multi("Multiplayer", ResourceLocation("CAT/menu/multiplayer.png")),
        Alts("Alts", ResourceLocation("CAT/menu/alt.png")),
        Settings("Settings", ResourceLocation("CAT/menu/settings.png")),
        Mods("Mods/Customize", ResourceLocation("CAT/menu/mods.png")),
        Exit("Exit", ResourceLocation("CAT/menu/exit.png"))
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}