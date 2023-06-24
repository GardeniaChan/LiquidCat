package net.ccbluex.liquidbounce.ui.client.ClickUi;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ColorValueButton
        extends ValueButton {
    private float[] hue = new float[]{0.0f};
    private int position;
    private int color = new Color(125, 125, 125).getRGB();

    public ColorValueButton(int x, int y) {
        super(null, x, y);
        this.custom = true;
        this.position = -1111;
    }

    public void render(int mouseX, int mouseY) {
        float[] huee = new float[]{this.hue[0]};
        Gui.drawRect((int)(this.x - 10), (int)(this.y - 4), (int)(this.x + 80), (int)(this.y + 11), (int)new Color(0, 0, 0, 100).getRGB());
        for (int i = this.x - 7; i < this.x + 79; ++i) {
            Color color = Color.getHSBColor((float)(huee[0] / 255.0f), (float)0.7f, (float)1.0f);
            if (mouseX > i - 1 && mouseX < i + 1 && mouseY > this.y - 6 && mouseY < this.y + 12 && Mouse.isButtonDown((int)0)) {
                this.color = color.getRGB();
                this.position = i;
            }
            if (this.color == color.getRGB()) {
                this.position = i;
            }
            Gui.drawRect((int)(i - 1), (int)this.y, (int)i, (int)(this.y + 8), (int)color.getRGB());
            float[] arrf = huee;
            arrf[0] = arrf[0] + 4.0f;
            if (!(huee[0] > 255.0f)) continue;
            huee[0] = huee[0] - 255.0f;
        }
        Gui.drawRect((int)this.position, (int)this.y, (int)(this.position + 1), (int)(this.y + 8), (int)-1);
        if (this.hue[0] > 255.0f) {
            this.hue[0] = this.hue[0] - 255.0f;
        }
    }

    public void key(char typedChar, int keyCode) {
    }

    public void click(int mouseX, int mouseY, int button) {
    }
}
