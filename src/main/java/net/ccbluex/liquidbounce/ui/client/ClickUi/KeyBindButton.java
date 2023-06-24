/*
 * Decompiled with CFR 0.136.
 */
package net.ccbluex.liquidbounce.ui.client.ClickUi;


import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class KeyBindButton
extends ValueButton {
    public Module cheat;
    public double opacity = 0.0;
    public boolean bind;

    public KeyBindButton(Module cheat, int x2, int y2) {
        super(null, x2, y2);
        this.custom = true;
        this.bind = false;
        this.cheat = cheat;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        GameFontRenderer font = Fonts.font35;
        double d2 = mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + Fonts.font35.getStringWidth(this.cheat.getName()) + 5 ? (this.opacity + 10.0 < 200.0 ? (this.opacity = this.opacity + 10.0) : 200.0) : (this.opacity - 6.0 > 0.0 ? (this.opacity = this.opacity - 6.0) : 0.0);
        this.opacity = d2;
        RenderUtils.drawRect(this.x - 9, this.y - 5, this.x - 9 + 88, this.y + font.getStringWidth(this.cheat.getName()) + 5, new Color(120, 120, 120, (int)this.opacity).getRGB());
        font.drawStringWithShadow("Bind", this.x, this.y, -1);
        font.drawStringWithShadow(String.valueOf(String.valueOf(this.bind ? "" : "")) + Keyboard.getKeyName(this.cheat.getKeyBind()), this.x + 75 - font.getStringWidth(Keyboard.getKeyName(this.cheat.getKeyBind())), this.y, -1);
    }

    @Override
    public void key(char typedChar, int keyCode) {
        if (this.bind) {
            this.cheat.setKeyBind(keyCode);
            if (keyCode == 1) {
                this.cheat.setKeyBind(0);
            }
            ClickyUI.binding = false;
            this.bind = false;
        }
        super.key(typedChar, keyCode);
    }

    @Override
    public void click(int mouseX, int mouseY, int button) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + Fonts.font35.getStringWidth(this.cheat.getName()) + 5 && button == 0) {
            this.bind = !this.bind;
            ClickyUI.binding = this.bind;
        }
        super.click(mouseX, mouseY, button);
    }
}

