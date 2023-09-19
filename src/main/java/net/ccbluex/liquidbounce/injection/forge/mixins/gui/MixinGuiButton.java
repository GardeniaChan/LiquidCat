/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;


@Mixin(GuiButton.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiButton extends Gui {

   @Shadow
   public boolean visible;

   @Shadow
   public int xPosition;

   @Shadow
   public int yPosition;

   @Shadow
   public int width;

   @Shadow
   public int height;

   @Shadow
   protected boolean hovered;

   @Shadow
   public boolean enabled;

   @Shadow
   protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);

   @Shadow
   public String displayString;

   @Shadow
   @Final
   protected static ResourceLocation buttonTextures;
   private float cut;
   private float alpha;
   private float moveX = 0F;

   /**
    * @author CCBlueX
    */

  @Overwrite
   public boolean mousePressed(Minecraft p_mousePressed_1_, int p_mousePressed_2_, int p_mousePressed_3_) {
      return this.enabled && this.visible && p_mousePressed_2_ >= this.xPosition && p_mousePressed_3_ >= this.yPosition && p_mousePressed_2_ < this.xPosition + this.width && p_mousePressed_3_ < this.yPosition + this.height;
   }

   @Overwrite
   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if (visible) {
         final FontRenderer fontRenderer =
            mc.getLanguageManager().isCurrentLocaleUnicode() ? Fonts.misans35 : Fonts.misans35;
         hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition &&
                    mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);

         final int delta = RenderUtils.deltaTime;
         final float speedDelta = 0.01F * delta;

         if (enabled && hovered) {
            cut += 0.05F * delta;

            if (cut >= 4) cut = 4;

            alpha += 0.3F * delta;

            if (alpha >= 210) alpha = 210;
            moveX = AnimationUtils.animate(this.width, moveX, speedDelta);
         } else {
            cut -= 0.05F * delta;

            if (cut <= 0) cut = 0;

            alpha -= 0.3F * delta;

            if (alpha <= 120) alpha = 120;

            moveX = AnimationUtils.animate(0F, moveX, speedDelta);
         }

         float roundCorner = (float) Math.max(0F, 2.4F + moveX - (this.width - 2.4F));
         RenderUtils.drawRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 2.4F, new Color(0, 0, 0, 150).getRGB());
         RenderUtils.customRounded(this.xPosition, this.yPosition, this.xPosition + moveX, this.yPosition + this.height, 0F, roundCorner, roundCorner, 0F, (this.enabled ? new Color(0, 111, 255) : new Color(71, 71, 71)).getRGB());

         mc.getTextureManager().bindTexture(buttonTextures);
         mouseDragged(mc, mouseX, mouseY);

         AWTFontRenderer.Companion.setAssumeNonVolatile(true);

         fontRenderer.drawStringWithShadow(displayString,
                 (float) ((this.xPosition + this.width / 2) -
                         fontRenderer.getStringWidth(displayString) / 2),
                 this.yPosition + (this.height - 5) / 2F, 14737632);

         AWTFontRenderer.Companion.setAssumeNonVolatile(false);

         GlStateManager.resetColor();
      }
   }
}