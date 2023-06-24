package net.ccbluex.liquidbounce.ui.client.ClickUi;

import com.google.common.collect.Lists;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class Button {
    public Module cheat;
    GameFontRenderer font = Fonts.font35;
    public Window parent;
    public int x;
    public int y;
    public int index;
    public int remander;
    public double opacity = 0.0;
    public ArrayList<ValueButton> buttons = Lists.newArrayList();
    public boolean expand;

    public Button(Module cheat, int x, int y) {
        this.cheat = cheat;
        this.x = x;
        this.y = y;
        int y2 = y + 14;
        for (Value v : cheat.getValues()) {
            this.buttons.add(new ValueButton(v, x + 5, y2));
            y2 += 12;
        }
    }

    public void render(int mouseX, int mouseY) {
        if (this.index != 0) {
            Button b2 = (Button)this.parent.buttons.get(this.index - 1);
            this.y = b2.y + 15 + (b2.expand ? 15 * b2.buttons.size() : 0);
        }
        for (int i = 0; i < this.buttons.size(); ++i) {
            ((ValueButton)this.buttons.get((int)i)).y = this.y + 14 + 15 * i;
            ((ValueButton)this.buttons.get((int)i)).x = this.x + 5;
        }
        Gui.drawRect((int)(this.x - 5), (int)(this.y - 5), (int)(this.x + 85), (int)(this.y + 4 + this.font.FONT_HEIGHT), (int)new Color(40, 40, 40, 255).getRGB());
        if (this.cheat.getState()) {
            Gui.drawRect((int)(this.x - 4), (int)(this.y - 5), (int)(this.x + 84), (int)(this.y + 3 + this.font.FONT_HEIGHT), (int)(this.cheat.getCategory() == ModuleCategory.COMBAT ? new Color(250, 100, 110, 255).getRGB() : (this.cheat.getCategory() == ModuleCategory.RENDER ? new Color(133, 119, 252, 255).getRGB() : (this.cheat.getCategory() == ModuleCategory.MOVEMENT ? new Color(200, 167, 200, 255).getRGB() : (this.cheat.getCategory() == ModuleCategory.PLAYER ? new Color(50, 200, 200, 255).getRGB() : (this.cheat.getCategory() == ModuleCategory.MISC ? new Color(50, 50, 150, 255).getRGB() : new Color(0, 0, 0, 0).getRGB()))))));
        }
        if (this.cheat.getCategory() == ModuleCategory.COMBAT) {
            ValueButton.valuebackcolor = new Color(250, 100, 110, 255).getRGB();
        }
        if (this.cheat.getCategory() == ModuleCategory.RENDER) {
            ValueButton.valuebackcolor = new Color(133, 119, 252, 255).getRGB();
        }
        if (this.cheat.getCategory() == ModuleCategory.MOVEMENT) {
            ValueButton.valuebackcolor = new Color(200, 167, 200, 255).getRGB();
        }
        if (this.cheat.getCategory() == ModuleCategory.PLAYER) {
            ValueButton.valuebackcolor = new Color(50, 200, 200, 255).getRGB();
        }
        if (this.cheat.getCategory() == ModuleCategory.MISC) {
            ValueButton.valuebackcolor = new Color(50, 50, 150, 255).getRGB();
        }
        this.font.drawStringWithShadow(this.cheat.getName() + (this.cheat.getKeyBind() == 0 ? "" : " \u00a77[" + Keyboard.getKeyName((int)this.cheat.getKeyBind()) + "]"), (float)(this.x - 2), (float)this.y, new Color(255, 255, 255, 250).getRGB());
        if (this.cheat.getValues().size() > 0 && !this.expand) {
            Fonts.font35.drawString("<", this.x + 75, this.y, -1);
        }
        if (this.cheat.getValues().size() > 0 && this.expand) {
            Fonts.font35.drawString(">", this.x + 76, this.y, -1);
        }
        if (this.expand) {
            this.buttons.forEach(b -> b.render(mouseX, mouseY));
        }
    }

    public void key(char typedChar, int keyCode) {
        this.buttons.forEach(b -> b.key(typedChar, keyCode));
    }

    public void click(int mouseX, int mouseY, int button) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + this.font.FONT_HEIGHT) {
            if (button == 0) {
                this.cheat.setState(!this.cheat.getState());
            }
            if (button == 1 && !this.buttons.isEmpty()) {
                this.expand = !this.expand;
                boolean bl = this.expand;
            }
        }
        if (this.expand) {
            this.buttons.forEach(b -> b.click(mouseX, mouseY, button));
        }
    }

    public void setParent(Window parent) {
        this.parent = parent;
        for (int i = 0; i < this.parent.buttons.size(); ++i) {
            if (this.parent.buttons.get(i) != this) continue;
            this.index = i;
            this.remander = this.parent.buttons.size() - i;
            break;
        }
    }
}
