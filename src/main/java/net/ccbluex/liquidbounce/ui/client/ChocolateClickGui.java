package net.ccbluex.liquidbounce.ui.client;

import net.cat.CMain;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.clickgui.Module.fonts.api.FontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.clickgui.Module.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

public class ChocolateClickGui extends GuiScreen {
    public static ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public static ModuleCategory currentModuleType = ModuleCategory.COMBAT;
    public static Module currentModule = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(0);
    public static float startX = sr.getScaledWidth() / 2 - 350 / 2, startY = sr.getScaledHeight() / 2 - 250 / 2;
    public static int moduleStart = 0;
    public static int valueStart = 0;
    boolean previousmouse = true;
    boolean mouse;
    public float moveX = 0, moveY = 0;
    boolean bind = false;
    float hue;
    public static int alpha;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawImage(new ResourceLocation( "liquidbounce/custom_hud_icon.png"), 9, height - 41, 32, 32);
        if (Mouse.isButtonDown(0) && mouseX >= 5 && mouseX <= 50 && mouseY <= height - 5 && mouseY >= height - 50)
            mc.displayGuiScreen(new GuiHudDesigner()); //进入自定义HUD界面
        sr = new ScaledResolution(mc);
        if(alpha < 255) {
            alpha += 5;
        }
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float h = this.hue;
        float h2 = this.hue + 85.0f;
        float h3 = this.hue + 170.0f;
        if (h > 255.0f) {
            h = 0.0f;
        }
        if (h2 > 255.0f) {
            h2 -= 255.0f;
        }
        if (h3 > 255.0f) {
            h3 -= 255.0f;
        }
        Color color33 = Color.getHSBColor((float)(h / 255.0f), (float)0.9f, (float)1.0f);
        Color color332 = Color.getHSBColor((float)(h2 / 255.0f), (float)0.9f, (float)1.0f);
        Color color333 = Color.getHSBColor((float)(h3 / 255.0f), (float)0.9f, (float)1.0f);
        int color1 = color33.getRGB();
        int color2 = color332.getRGB();
        int color3 = color333.getRGB();
        int color4 = new Color(ClickGUI.colorRedValue.get(), ClickGUI.colorGreenValue.get(),ClickGUI.colorBlueValue.get(), alpha).getRGB();
        this.hue = (float)((double)this.hue + 0.1);
        //bg
        RenderUtils.rectangleBordered((double)startX, (double)startY, (double)(startX + (float)450), (double)(startY + (float)250), (double)0.5, (int) Colors.getColor((int)90, alpha), (int)Colors.getColor((int)0, alpha));
        //none
        RenderUtils.rectangleBordered((double)(startX + 1.0f), (double)(startY + 1.0f), (double)(startX + (float)450 - 1.0f), (double)(startY + (float)150 - 1.0f), (double)1.0, (int)Colors.getColor((int)90, alpha), (int)Colors.getColor((int)61, alpha));
        //黑线(也许吧）
        RenderUtils.rectangleBordered((double)((double)startX + 2.5), (double)((double)startY + 1.5), (double)((double)(startX + (float)450) - 2.5), (double)((double)(startY + (float)350) - 2.5), (double)0.5, (int)Colors.getColor((int)61, alpha), (int)Colors.getColor(0, alpha));
        //none
        RenderUtils.rectangleBordered((double)(startX + 3.0f), (double)(startY + 3.0f), (double)(startX + (float)450 - 4.0f), (double)(startY + (float)250 - 4.0f), (double)0.5, (int)Colors.getColor((int)27, alpha), (int)Colors.getColor((int)61, alpha));
        if(alpha >= 55) {
            RenderUtils.drawGradientSideways((double)(startX + 3.0f), (double)(startY + 3.0f), (double)(startX + (float)(450 / 2)), (double)((double)startY + 5.6), (int)color1, (int)color2);
            RenderUtils.drawGradientSideways((double)(startX + (float)(450 / 2)), (double)(startY + 3.0f), (double)(startX + (float)450 - 3.0f), (double)((double)startY + 5.6), (int)color2, (int)color3);
        }

        //RenderUtils.drawRect(startX + 98, startY + 48, startX + 432, startY + 418, new Color(30,30,30, alpha).getRGB());

        RenderUtils.drawRect(startX + 100, startY + 50, startX + 430, startY + 180, new Color(35,35,35, alpha).getRGB());
        //功能选项背景旁边的黑线
        RenderUtils.drawRect(startX + 200, startY + 50, startX + 430, startY + 220, new Color(37,37,37, alpha).getRGB());
        //功能选项bg
        RenderUtils.drawRect(startX + 202, startY + 50, startX + 430, startY + 220, new Color(40,40,40, alpha).getRGB());

        Fonts.TenacityBold.TenacityBold40.TenacityBold40.drawCenteredString("LiquidCat", startX + 50, startY + 17, new Color(255,255,255, alpha).getRGB());
        Fonts.TenacityBold.TenacityBold40.TenacityBold40.drawCenteredString(String.valueOf(CMain.Ver), startX + 50, startY + 30, color4);
        Fonts.TenacityBold.TenacityBold40.TenacityBold40.drawString("Hello, " + mc.thePlayer.getName() + " !", startX + 450 - 50 - Fonts.TenacityBold.TenacityBold40.TenacityBold40.stringWidth("Hello, " + mc.thePlayer.getName() + " !"), startY + 22, new Color(200,200,200, alpha).getRGB());
        //Fonts.font35.drawString(LiquidBounce.CLIENT_NAME +  LiquidBounce.CLIENT_VERSION, startX + 15, startY + 330, new Color(180,180,180, alpha).getRGB());
        int m = Mouse.getDWheel();
        //鼠标滚轮生效区域
        if (this.isCategoryHovered(startX + 100, startY + 40, startX + 200, startY + 220, mouseX, mouseY)) {
            if (m < 0 && moduleStart < LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size() - 1) {
                moduleStart++;
            }
            if (m > 0 && moduleStart > 0) {
                moduleStart--;
            }
        }
        if (this.isCategoryHovered(startX + 200, startY + 50, startX + 430, startY + 220, mouseX, mouseY)) {
            if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
                valueStart++;
            }
            if (m > 0 && valueStart > 0) {
                valueStart--;
            }
        }
        float mY = startY + 12;
        for (int i = 0; i < LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size(); i++) {
            Module module = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(i);
            if (mY > startY + 250)
                break;
            if (i < moduleStart) {
                continue;
            }

            if (!module.getState()) {
                RenderUtils.drawRect(startX + 100, mY + 45, startX + 200, mY + 70, isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? new Color (60,60,60, alpha).getRGB() : new Color(35, 35, 35, alpha).getRGB());
                RenderUtils.drawFilledCircle(startX + (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 112 : 110), mY + 58, 3, new Color(70, 70, 70, alpha).getRGB(), 5);
                Fonts.TenacityBold.TenacityBold35.TenacityBold35.drawString(module.getName(), startX + (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 122 : 120), mY + 55,
                        new Color(175, 175, 175, alpha).getRGB());
            } else {
                RenderUtils.drawRect(startX + 100, mY + 45, startX + 200, mY + 70, isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? new Color (60,60,60, alpha).getRGB() : new Color(35, 35, 35, alpha).getRGB());
                RenderUtils.drawFilledCircle(startX + (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 112 : 110), mY + 58, 3, new Color(100,255,100, alpha).getRGB(), 5);
                Fonts.TenacityBold.TenacityBold35.TenacityBold35.drawString(module.getName(), startX + (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 122 : 120), mY + 55,
                        new Color(255, 255, 255, alpha).getRGB());
            }

            if (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY)) {
                if (!this.previousmouse && Mouse.isButtonDown(0)) {
                    if (module.getState()) {
                        module.setState(false);
                    } else {
                        module.setState(true);
                    }
                    previousmouse = true;
                }
                if (!this.previousmouse && Mouse.isButtonDown(1)) {
                    previousmouse = true;
                }
            }

            if (!Mouse.isButtonDown(0)) {
                this.previousmouse = false;
            }
            if (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY)
                    && Mouse.isButtonDown(1)) {
                for (int j = 0; j < currentModule.getValues().size(); j++) {
                    Value value = currentModule.getValues().get(j);
                    if (value instanceof BoolValue) {
                        ((BoolValue) value).setAnim(55);
                    }
                }
                currentModule = module;
                valueStart = 0;
            }
            mY += 25;
        }
        mY = startY + 12;
        FontRenderer font = Fonts.TenacityBold.TenacityBold35.TenacityBold35;
        for (int i = 0; i < currentModule.getValues().size(); i++) {
            if (mY > startY + 250)
                break;
            if (i < valueStart) {
                continue;
            }
            Value value = currentModule.getValues().get(i);
            if(value instanceof TextValue) {
                TextValue textValue = (TextValue) value;
                font.drawStringWithShadow(textValue.getName() + ": " + textValue.get(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                mY += 20;
            }
            if(value instanceof BlockValue) {
                BlockValue blockValue = (BlockValue) value;
                font.drawStringWithShadow(blockValue.getName() + ": " + blockValue.get(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                mY += 20;
            }
            if(value instanceof FontValue) {
                FontValue fontValue = (FontValue) value;
                font.drawStringWithShadow(fontValue.getName() + ": " + fontValue.get(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                mY += 20;
            }
            if (value instanceof IntegerValue) {
                IntegerValue floatValue = (IntegerValue) value;
                float x = startX + 320;
                double render = (double) (68.0F
                        * (floatValue.getValue() - floatValue.getMinimum())
                        / ((floatValue).getMaximum()
                        - floatValue.getMinimum()));
                RenderUtils.drawRect((float) x + 2, mY + 52, (float) ((double) x + 75), mY + 53,
                        isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY) && Mouse.isButtonDown((int) 0) ? new Color (80,80,80, alpha).getRGB() :(new Color(30, 30, 30, alpha)).getRGB());
                RenderUtils.drawRect((float) x + 2, mY + 52, (float) ((double) x + render + 6.5D), mY + 53, new Color(35,35,255, alpha).getRGB());
                RenderUtils.drawFilledCircle((float) ((double) x + render + 2D) + 3, mY + 52.25, 1.5, new Color(35,35,255, alpha).getRGB(), 5);
                font.drawStringWithShadow(floatValue.getName(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                font.drawStringWithShadow(floatValue.getValue().toString(), startX + 320 - font.stringWidth(value.getValue().toString()), mY + 50,
                        new Color(255, 255, 255, alpha).getRGB());
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY)
                        && Mouse.isButtonDown(0)) {
                    if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
                        render = (floatValue).getMinimum();
                        double max = (floatValue).getMaximum();
                        double inc = 1;
                        double valAbs = (double) mouseX - ((double) x + 1.0D);
                        double perc = valAbs / 68.0D;
                        perc = Math.min(Math.max(0.0D, perc), 1.0D);
                        double valRel = (max - render) * perc;
                        float val = (float) (render + valRel);
                        val = (float) (Math.round(val * (1.0D / inc)) / (1.0D / inc));
                        floatValue.setValue((int) val);
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
                mY += 20;
            }
            if (value instanceof FloatValue) {
                FloatValue floatValue = (FloatValue) value;
                float x = startX + 320;
                double render = (double) (68.0F
                        * (floatValue.getValue() - floatValue.getMinimum())
                        / ((floatValue).getMaximum()
                        - floatValue.getMinimum()));
                RenderUtils.drawRect((float) x + 2, mY + 52, (float) ((double) x + 75), mY + 53,
                        isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY) && Mouse.isButtonDown((int) 0) ? new Color (80,80,80, alpha).getRGB() :(new Color(30, 30, 30, alpha)).getRGB());
                RenderUtils.drawRect((float) x + 2, mY + 52, (float) ((double) x + render + 6.5D), mY + 53, new Color(35,35,255, alpha).getRGB());
                RenderUtils.drawFilledCircle((float) ((double) x + render + 2D) + 3, mY + 52.25, 1.5, new Color(35,35,255, alpha).getRGB(), 5);
                font.drawStringWithShadow(floatValue.getName(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                font.drawStringWithShadow(floatValue.getValue().toString(), startX + 320 - font.stringWidth(value.getValue().toString()), mY + 50,
                        new Color(255, 255, 255, alpha).getRGB());
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY)
                        && Mouse.isButtonDown(0)) {
                    if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
                        render = (floatValue).getMinimum();
                        double max = (floatValue).getMaximum();
                        double inc = 0.5F;
                        double valAbs = (double) mouseX - ((double) x + 1.0D);
                        double perc = valAbs / 68.0D;
                        perc = Math.min(Math.max(0.0D, perc), 1.0D);
                        double valRel = (max - render) * perc;
                        float val = (float) (render + valRel);
                        val = (float) (Math.round(val * (1.0D / inc)) / (1.0D / inc));
                        floatValue.setValue(val);
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
                mY += 20;
            }
            if (value instanceof BoolValue) {
                BoolValue boolValue = (BoolValue) value;
                float x = startX + 320;
                int xx = 50;
                int x2x = 65;
                font.drawStringWithShadow(boolValue.getName(), startX + 220, mY + 50,
                        new Color(175, 175, 175, alpha).getRGB());
                if (boolValue.getValue()) {
                    RenderUtils.drawRect(x + xx, mY + 50, x + x2x, mY + 59, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(20, 20, 20, alpha).getRGB());
                    RenderUtils.drawFilledCircle(x + xx, mY + 54.5, 4.5, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + x2x, mY + 54.5, 4.5, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + x2x, mY + 54.5, 5, new Color(35,35,255, alpha).getRGB(), 10);
                } else {
                    RenderUtils.drawRect(x + xx, mY + 50, x + x2x, mY + 59, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(20, 20, 20, alpha).getRGB());
                    RenderUtils.drawFilledCircle(x + xx, mY + 54.5, 4.5, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + x2x, mY + 54.5, 4.5, isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + xx, mY + 54.5, 5,
                            new Color(56, 56, 56, alpha).getRGB(), 10);
                }
                if (this.isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
                        this.previousmouse = true;
                        this.mouse = true;
                    }

                    if (this.mouse) {
                        boolValue.setValue(!boolValue.getValue());
                        this.mouse = false;
                    }
                }
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                mY += 20;
            }
            if (value instanceof ListValue) {
                ListValue listValue = (ListValue) value;
                float x = startX + 320;
                font.drawStringWithShadow(listValue.getName(), startX + 220, mY + 52,
                        new Color(175, 175, 175, alpha).getRGB());
                RenderUtils.drawRect(x + 5, mY + 45, x + 75, mY + 65, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB());
                RenderUtils.drawRect(x + 2, mY + 48, x + 78, mY + 62, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB());
                RenderUtils.drawFilledCircle(x + 5, mY + 48, 3, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB(), 5);
                RenderUtils.drawFilledCircle(x + 5, mY + 62, 3, isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB(), 5);
                RenderUtils.drawFilledCircle(x + 75, mY + 48, 3,isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
                RenderUtils.drawFilledCircle(x + 75, mY + 62, 3,isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
                font.drawStringWithShadow(listValue.get(),
                        (float) (x + 40 - font.stringWidth(listValue.get()) / 2), mY + 53, new Color (255,255,255,alpha).getRGB());
                if (this.isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)) {
                    if (Mouse.isButtonDown(0) && !this.previousmouse) {
                        if (listValue.getValues().length <= listValue.getModeListNumber(listValue.get()) + 1) {
                            listValue.set(listValue.getValues()[0]);
                        } else {
                            listValue.set(listValue.getValues()[listValue.getModeListNumber(listValue.get()) + 1]);
                        }
                        this.previousmouse = true;
                    }

                }
                mY += 25;
            }
        }
        float x = startX + 320;
        float yyy = startY + 240;
        font.drawStringWithShadow("Bind", startX + 220, yyy + 50, new Color(170, 170, 170, alpha).getRGB());
        RenderUtils.drawRect(x + 5, yyy + 45, x + 75, yyy + 65, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB());
        RenderUtils.drawRect(x + 2, yyy + 48, x + 78, yyy + 62, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB());
        RenderUtils.drawFilledCircle(x + 5, yyy + 48, 3, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB(), 5);
        RenderUtils.drawFilledCircle(x + 5, yyy + 62, 3, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB(), 5);
        RenderUtils.drawFilledCircle(x + 75, yyy + 48, 3, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB(), 5);
        RenderUtils.drawFilledCircle(x + 75, yyy + 62, 3, isHovered(x + 2, yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color (80,80,80, alpha).getRGB() :new Color(56, 56, 56, alpha).getRGB(), 5);
        font.drawStringWithShadow(Keyboard.getKeyName(currentModule.getKeyBind()),
                (x + 40 - font.stringWidth(Keyboard.getKeyName(currentModule.getKeyBind())) / 2),
                yyy + 53, new Color(255,255,255,alpha).getRGB());

        if ((isHovered(startX, startY, startX + 450, startY + 50, mouseX, mouseY) || isHovered(startX,startY + 315,startX + 450, startY + 350, mouseX,mouseY) || isHovered(startX + 430,startY,startX + 450, startY + 350, mouseX,mouseY)) && Mouse.isButtonDown(0)) {
            if (moveX == 0 && moveY == 0) {
                moveX = mouseX - startX;
                moveY = mouseY - startY;
            } else {
                startX = mouseX - moveX;
                startY = mouseY - moveY;
            }
            this.previousmouse = true;
        } else if (moveX != 0 || moveY != 0) {
            moveX = 0;
            moveY = 0;
        }
        if (isHovered(sr.getScaledWidth() / 2 - 40, 0, sr.getScaledWidth() / 2 + 40, 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            startX = sr.getScaledWidth() / 2 - 450 / 2;
            startY = sr.getScaledHeight() / 2 - 350 / 2;
            alpha = 0;
        }
        RenderUtils.drawRect(sr.getScaledWidth() / 2 - 39, 0, sr.getScaledWidth() / 2 + 39, 19, new Color(0,0,0,alpha / 2).getRGB());
        RenderUtils.drawRect(sr.getScaledWidth() / 2 - 40, 0, sr.getScaledWidth() / 2 + 40, 20, new Color(0,0,0,alpha / 2).getRGB());
        Fonts.TenacityBold.TenacityBold40.TenacityBold40.drawStringWithShadow("Reset ClickGui", sr.getScaledWidth() / 2 - 10, 6, new Color(255,255,255, alpha).getRGB());

        int j = 60;
        int l = 45;
        float k =  startY + 10;
        float xx =  startX + 5;
        for (int i = 0; i < ModuleCategory.values().length; i++) {
            ModuleCategory[] iterator = ModuleCategory.values();
            if (iterator[i] == currentModuleType) {
                RenderUtils.drawRect(xx + 8, k + 12 + j + i * l, xx + 30, k + 13 + j + i * l, color4);
            }
            float y2 = +k + 20 + j + i * l;
            Fonts.TenacityBold.TenacityBold40.TenacityBold40.drawString(iterator[i].toString(), xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l, mouseX, mouseY) ? 27 :25), k + 56 + l * i,
                    new Color(255, 255, 255, alpha).getRGB());
//            if (iterator[i].toString().equals("Combat")) {
//                FontLoaders.icon24.drawString("1", xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l, mouseX, mouseY) ? 10 : 8), k + 56 + l * i,
//                        new Color(255, 255, 255, alpha).getRGB());
//            }else if (iterator[i].toString().equals("Visual")) {
//                FontLoaders.icon24.drawString("0", xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l, mouseX, mouseY) ? 10 : 8), k + 56 + l * i,
//                        new Color(255, 255, 255, alpha).getRGB());
//            }else if (iterator[i].toString().equals("Movement")) {
//                FontLoaders.icon24.drawString("5", xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l, mouseX, mouseY) ? 10 : 8), k + 56 + l * i,
//                        new Color(255, 255, 255, alpha).getRGB());
//            }else if (iterator[i].toString().equals("Player")) {
//                FontLoaders.icon24.drawString("6", xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l, mouseX, mouseY) ? 10 : 8), k + 56 + l * i,
//                        new Color(255, 255, 255, alpha).getRGB());
//            }else if (iterator[i].toString().equals("World")) {
//                FontLoaders.icon24.drawString("3", xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l, mouseX, mouseY) ? 10 : 8), k + 56 + l * i,
//                        new Color(255, 255, 255, alpha).getRGB());
//            }
            try {
                if (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l, mouseX, mouseY)
                        && Mouse.isButtonDown((int) 0)) {
                    currentModuleType = iterator[i];
                    currentModule = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(0);
                    moduleStart = 0;
                    valueStart = 0;
                    for (int x1 = 0; x1 < currentModule.getValues().size(); x1++) {
                        Value value = currentModule.getValues().get(x1);
                        if (value instanceof BoolValue) {
                            ((BoolValue) value).setAnim(55);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public void initGui() {
        for (int i = 0; i < currentModule.getValues().size(); i++) {
            Value value = currentModule.getValues().get(i);
            if (value instanceof BoolValue) {
                ((BoolValue) value).setAnim(55);
            }
        }

        super.initGui();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.bind) {
            this.currentModule.setKeyBind(keyCode);
            if (keyCode == 1) {
                this.currentModule.setKeyBind(0);
            }
            this.bind = false;
        } else if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
            LiquidBounce.moduleManager.getModule(ClickGUI.class).setState(false);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        float x = startX + 220;
        float mY = startY + 30;
        for (int i = 0; i < currentModule.getValues().size(); i++) {
            if (mY > startY + 350)
                break;
            if (i < valueStart) {
                continue;
            }
            Value value = currentModule.getValues().get(i);
            if (value instanceof FloatValue) {
                mY += 20;
            }
            if (value instanceof BoolValue) {

                mY += 20;
            }
            if (value instanceof ListValue) {

                mY += 25;
            }
        }
        float x1 = startX + 320;
        float yyy = startY + 240;
        if (isHovered(x1 + 2, yyy + 45, x1 + 78, yyy + 65, mouseX, mouseY)) {
            this.bind = true;
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
            return true;
        }

        return false;
    }

    public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void onGuiClosed() {
        alpha = 0;
    }
}
