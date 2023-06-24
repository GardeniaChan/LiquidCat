package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.RenderU;
import net.ccbluex.liquidbounce.value.FontValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;

import static org.lwjgl.opengl.GL11.*;

@ElementInfo(name = "QQLogo")
public class QQLogo extends Element {
    private boolean got = false;
    private final FontValue fontvalue = new FontValue("Font",Fonts.SFUI40);


    protected Border draw() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(1, 1, 1, 1);
        try {
            if (!got) {
                mc.getTextureManager().loadTexture(
                        new ResourceLocation("sb"),
                        new DynamicTexture(ImageIO.read(new URL("http://q.qlogo.cn/headimg_dl?bs=qq&dst_uin="+ "2482226023" +"&src_uin=qq.zy7.com&fid=blog&spec=640"))));
                        got = true;
            }
        }
        catch(final Throwable throwable) {

        }
        mc.getTextureManager().bindTexture(new ResourceLocation("sb"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 60, 60, 60, 60);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        final int color = new Color(45,45,45).getRGB();
        for(float i = 26;i <= 42; i += 1) {
            RenderU.drawOutFullCircle(31.5F, 30, i, color, 5);
        }
        RenderU.drawOutFullCircle(31.5F, 30, 27, new Color(53,141,204).getRGB(), 2);
        RenderU.drawGradientSideways(60F, 30F, 180F, 45F, new Color(45,45,45, 255).getRGB(), new Color(45,45,45, 0).getRGB());
        RenderU.drawOutFullCircle(31.5F, 30, 44, new Color(0,230,0).getRGB(), 3,-7F, 320F);
        RenderU.drawGradientSideways(60F, 1F, 200F, 26.5F, new Color(45,45,45, 255).getRGB(), new Color(45,45,45, 0).getRGB());
        fontvalue.get().drawString(mc.getSession().getUsername() + " | " + Math.round(mc.thePlayer.getHealth()) + "hp", (int)80F, (int)13F, new Color(200, 200, 200).getRGB());
        fontvalue.get().drawString(String.valueOf(mc.thePlayer.getFoodStats().getFoodLevel()), 90F, 37F, -1, false);
        fontvalue.get().drawString("Food", 105F, 37F, new Color(236,161,4).getRGB(), false);
        return new Border(0F, 0F, 120F, 30F);
    }


    @Override
    public Border drawElement() {

        return draw();
    }
}