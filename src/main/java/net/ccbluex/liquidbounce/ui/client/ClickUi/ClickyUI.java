/*
 * Decompiled with CFR 0.136.
 */
package net.ccbluex.liquidbounce.ui.client.ClickUi;


import com.google.common.collect.Lists;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class ClickyUI
        extends GuiScreen {
    public static ArrayList<Window> windows = Lists.newArrayList();
    public double opacity = 0.0;
    public int scrollVelocity;
    public static boolean binding = false;

    public ClickyUI() {
        if (windows.isEmpty()) {
            int x2 = 5;
            ModuleCategory[] arrmoduleType = ModuleCategory.values();
            int n2 = arrmoduleType.length;
            int n22 = 0;
            while (n22 < n2) {
                ModuleCategory c2 = arrmoduleType[n22];
                windows.add(new Window(c2, x2, 5));
                x2 += 95;
                ++n22;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.opacity = this.opacity + 10.0 < 200.0 ? (this.opacity = this.opacity + 10.0) : 200.0;
        GlStateManager.pushMatrix();
        ScaledResolution scaledRes = new ScaledResolution(this.mc);
        windows.forEach(w2 -> w2.render(mouseX, mouseY));
        GlStateManager.popMatrix();
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            this.scrollVelocity = wheel < 0 ? -120 : (wheel > 0 ? 130 : 0);
        }
        windows.forEach(w2 -> w2.mouseScroll(mouseX, mouseY, this.scrollVelocity));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        windows.forEach(w2 -> w2.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 && !binding) {
            this.mc.displayGuiScreen(null);
            return;
        }
        windows.forEach(w2 -> w2.key(typedChar, keyCode));
    }

    public synchronized void sendToFront(Window window) {
        int panelIndex = 0;
        int i2 = 0;
        while (i2 < windows.size()) {
            if (windows.get(i2) == window) {
                panelIndex = i2;
                break;
            }
            ++i2;
        }
        Window t2 = windows.get(windows.size() - 1);
        windows.set(windows.size() - 1, windows.get(panelIndex));
        windows.set(panelIndex, t2);
    }
}

