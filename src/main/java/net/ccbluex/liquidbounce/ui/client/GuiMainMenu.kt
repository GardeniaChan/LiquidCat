package net.ccbluex.liquidbounce.ui.client

import javafx.scene.paint.Color.rgb
import net.cat.CMain
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color

class GuiMainMenu : GuiScreen(), GuiYesNoCallback {

    var slideX : Float = 0F
    var fade : Float = 0F

    var sliderX : Float = 0F

    var lastAnimTick: Long = 0L
    var alrUpdate = false


    override fun initGui() {
        slideX = 0F
        fade = 0F
        sliderX = 0F

        val defaultHeight = height / 4 + 48


        buttonList.add(GuiButton(1, width / 2 - 250, defaultHeight - 24 * 4, 98, 30, I18n.format("menu.singleplayer")))
        buttonList.add(GuiButton(2, width / 2 - 250, defaultHeight - 24 * 2 - 12, 98, 30, I18n.format("menu.multiplayer")))

        //buttonList.add(GuiButton(108, width / 2 - 100, defaultHeight + 24 * 3, "Contributors"))
        buttonList.add(GuiButton(0, width / 2 - 250, defaultHeight - 24, 98, 30, I18n.format("menu.options")))
        buttonList.add(GuiButton(4, width / 2 - 250, defaultHeight + 12, 98, 30, I18n.format("menu.quit")))

        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (!alrUpdate) {
            lastAnimTick = System.currentTimeMillis()
            alrUpdate = true
        }
        drawBackground(0)
        GL11.glPushMatrix()
       // Fonts.fontBold180.drawCenteredString(CMain.Name, width / 2F, height / 8F - 20F,Color(67,218,253).rgb, false)
        Fonts.font40.drawStringWithShadow("${CMain.Name} build ${CMain.Ver}", 2F, height - 12F, -1)
        //Fonts.font40.drawStringWithShadow(creditInfo, width - 3F - Fonts.font40.getStringWidth(creditInfo), height - 12F, -1)
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
        val staticX = width / 2F - 120F
        val staticY = height / 2F + 20F
        var index: Int = 0
        for (icon in ImageButton.values()) {
            if (isMouseHover(staticX + 240F + 40F * index, 0F, staticX+ 240F + 40F * (index + 1),20F, mouseX, mouseY))
                when (index) {
                    0 -> HttpUtils.browse("https://github.com/GardeniaChan/LiquidCat")
                    1 -> HttpUtils.browse("https://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=X4o2s15STgC9OBzO_954IVcJazKED6XS&authKey=BSueO6oKpHTYP7i3FQy4A2wdqfN%2FToQYkm6BlImFWUd7jGZvDPIvNT1KsX7k5ogJ&noverify=0&group_code=973202790")
                    2 -> HttpUtils.browse("")
                    3 -> HttpUtils.browse("")
                }
            
            index++
        }

        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
            1 -> mc.displayGuiScreen(GuiSelectWorld(this))
            2 -> mc.displayGuiScreen(GuiMultiplayer(this))
            4 -> mc.shutdown()
        }
    }

    fun renderBar(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val staticX = width / 2F - 120F
        val staticY = height / 2F + 20
        var index: Int = 0

        index = 0
        GlStateManager.disableAlpha()
        for (i in ImageButton.values()) {
            RenderUtils.drawImage2(i.texture, staticX + 40F * index + 240F, staticY - 170F, 18, 18)
            index++
        }
        GlStateManager.enableAlpha()
    }

    fun isMouseHover(x: Float, y: Float, x2: Float, y2: Float, mouseX: Int, mouseY: Int): Boolean = mouseX >= x && mouseX < x2 && mouseY >= y && mouseY < y2

    enum class ImageButton(val buttonName: String, val texture: ResourceLocation) {
        GIT("github", ResourceLocation("CAT/menu/git.png")),
        QQ("qqgroup", ResourceLocation("CAT/menu/qq.png")),
        DIS("discord", ResourceLocation("CAT/menu/dis.png")),
        TG("tg", ResourceLocation("CAT/menu/tg.png"))
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}