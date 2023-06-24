/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.Translate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.ccbluex.liquidbounce.LiquidBounce;

import static net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends GuiScreen {
    @Shadow
    private int xSize;
    @Shadow
    private int ySize;
    @Shadow
    private int guiLeft;
    @Shadow
    private int guiTop;

    private long guiOpenTime=-1;
    private boolean translated=false;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        guiOpenTime=System.currentTimeMillis();
        if(!this.mc.isIntegratedServerRunning())
            this.buttonList.add(new GuiButton(11110, 5, 10, 100, 20, "Disable KillAura"));
        this.buttonList.add(new GuiButton(11120,5, 34, 100, 20,"Disable InvManager"));
        this.buttonList.add(new GuiButton(11130, 5, 58, 100, 20, "Disable ChestStealer"));
        this.translate = new Translate(0, 0);
    }

    Translate translate;

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void mouseClicked(int mouseX, int mouseY, int mouseButton,CallbackInfo callbackInfo) {
        for (Object aButtonList : this.buttonList) {
            GuiButton var52 = (GuiButton) aButtonList;
            if (var52.mousePressed(this.mc, mouseX, mouseY) && var52.id == 11110) {
                LiquidBounce.moduleManager.getModule(KillAura.class).setState(false);
            }
            if (var52.mousePressed(this.mc, mouseX, mouseY) && var52.id == 11130) {
                LiquidBounce.moduleManager.getModule(ChestStealer.class).setState(false);
            }
        }
    }


    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void drawScreenHead(CallbackInfo callbackInfo){
        ChestStealer chestStealer=(ChestStealer) LiquidBounce.moduleManager.getModule(ChestStealer.class);
        try {
            Minecraft mc=Minecraft.getMinecraft();
            GuiScreen guiScreen=mc.currentScreen;
            if(chestStealer.getState()&&chestStealer.getSilentValue().get()&&guiScreen instanceof GuiChest){
                //mouse focus
                mc.setIngameFocus();
                mc.currentScreen=guiScreen;
                //hide GUI
                if(chestStealer.getSilenceTitleValue().get()) {
                    String tipString = "STEALING CHEST";
                    mc.fontRendererObj.drawString(tipString,
                            (width / 2) - (mc.fontRendererObj.getStringWidth(tipString) / 2),
                            (height / 2) + 30, 0xffffffff);
                }
                callbackInfo.cancel();
            }else{
                mc.currentScreen.drawWorldBackground(0);

                Animations animations = (Animations) LiquidBounce.moduleManager.getModule(Animations.class);
                if(animations != null) {
                    float pct = Math.max(animations.getTimeValue().get() - (System.currentTimeMillis() - guiOpenTime), 0) / ((float) animations.getTimeValue().get());
                    if (pct != 0) {
                        GL11.glPushMatrix();

                        switch (animations.getModeValue().get()){
                            case "Slide":{
                                pct=(float)EaseUtils.easeInBack(pct);
                                GL11.glTranslatef(0F, -(guiTop + ySize) * pct, 0F);
                                break;
                            }
                            case "Zoom":{
                                float scale=1-pct;
                                GL11.glScalef(scale,scale,scale);
                                GL11.glTranslatef(((guiLeft+(xSize*0.5F*pct))/scale)-guiLeft,((guiTop+(ySize*0.5F*pct))/scale)-guiTop, 0F);
                            }
                        }

                        translated = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void drawScreenReturn(CallbackInfo callbackInfo){

        if(translated){
            GL11.glPopMatrix();
            translated=false;
        }
    }
}