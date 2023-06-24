package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.util.ResourceLocation

@ElementInfo("CAT")
class Cat : Element() {
    private val image = ListValue("image", arrayOf("cat","cat1","cat2","cat3"),"cat1")

    override fun drawElement(): Border {
        val p = ResourceLocation("liquidbounce/bigpng/" + image.get() + ".png")
        RenderUtils.drawImage(p,355,212,100,100)

        return Border(355F,205F,455F,312F)
    }

}