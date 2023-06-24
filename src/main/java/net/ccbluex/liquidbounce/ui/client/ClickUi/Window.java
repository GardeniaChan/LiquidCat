package net.ccbluex.liquidbounce.ui.client.ClickUi;

import com.google.common.collect.Lists;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class Window {
    public ModuleCategory category;
    public ArrayList<Button> buttons = Lists.newArrayList();
    public boolean drag;
    public boolean extended;
    public int x;
    public int y;
    public int expand;
    public int dragX;
    public int dragY;
    public int max;
    public int scroll;
    public int scrollTo;
    public double angel;

    public Window(ModuleCategory category, int x2, int y2) {
        this.category = category;
        this.x = x2;
        this.y = y2;
        this.max = 120;
        int y22 = y2 + 22;
        for (Module c2 : LiquidBounce.moduleManager.getModules()) {
            if (c2.getCategory() != category) continue;
            this.buttons.add(new Button(c2, x2 + 5, y22));
            y22 += 15;
        }
        for (Button b2 : this.buttons) {
            b2.setParent(this);
        }
    }

    public void render(int mouseX, int mouseY) {
        GameFontRenderer mfont =  Fonts.font35;
        int current = 0;
        for (Button b3 : this.buttons) {
            if (b3.expand) {
                for (ValueButton v2 : b3.buttons) {
                    current += 15;
                }
            }
            current += 15;
        }
        int height = 15 + current;
        if (this.extended) {
            this.expand = this.expand + 5 < height ? (this.expand = this.expand + 5) : height;
            this.angel = this.angel + 20.0 < 180.0 ? (this.angel = this.angel + 20.0) : 180.0;
        } else {
            this.expand = this.expand - 5 > 0 ? (this.expand = this.expand - 5) : 0;
            this.angel = this.angel - 20.0 > 0.0 ? (this.angel = this.angel - 20.0) : 0.0;
        }
        Gui.drawRect(this.x, this.y + 16, this.x + 90, this.y +1 + this.expand,  new Color(0,0,0,100).getRGB());
        Gui.drawRect(this.x - 2, this.y, this.x + 92, this.y + 16, -8350465);
        mfont.drawString(this.category.name(), this.x +43- mfont.getStringWidth(this.category.name())/2	, this.y + 6, new Color(25, 25, 25).getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.x + 90 - 10, this.y + 5, 0.0f);
        GlStateManager.rotate((float)this.angel, 0.0f, 0.0f, -1.0f);
        GlStateManager.translate(-this.x + 90 - 10, -this.y + 5, 0.0f);
        GlStateManager.popMatrix();
        if (this.expand > 0) {
            GlStateManager.pushMatrix();
            this.buttons.forEach(b2 -> b2.render(mouseX, mouseY));
            RenderUtils.post();
            GlStateManager.popMatrix();
        }
        if (this.drag) {
            if (!Mouse.isButtonDown(0)) {
                this.drag = false;
            }
            this.x = mouseX - this.dragX;
            this.y = mouseY - this.dragY;
            this.buttons.get((int)0).y = this.y + 22 - this.scroll;
            for (Button b4 : this.buttons) {
                b4.x = this.x + 5;
            }
        }
    }

    public void key(char typedChar, int keyCode) {
        this.buttons.forEach(b2 -> b2.key(typedChar, keyCode));
    }

    public void mouseScroll(int mouseX, int mouseY, int amount) {
        if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17 + this.expand) {
            this.scrollTo = (int)((float)this.scrollTo - (float)(amount / 120 * 28));
        }
    }

    public void click(int mouseX, int mouseY, int button) {
        if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17) {
            if (button == 1) {
                this.extended = !this.extended;
            }
            if (button == 0) {
                this.drag = true;
                this.dragX = mouseX - this.x;
                this.dragY = mouseY - this.y;
            }
        }
        if (this.extended) {
            this.buttons.stream().filter(b2 -> b2.y < this.y + this.expand).forEach(b2 -> b2.click(mouseX, mouseY, button));
        }
    }
}



