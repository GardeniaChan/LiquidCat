package net.ccbluex.liquidbounce.ui.client.ClickUi;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ValueButton {
   public Value value;
   public String name;
   public boolean custom = false;
   public boolean change;
   public int x;
   public int y;
   public static int valuebackcolor;
   public double opacity = 0.0D;

   public ValueButton(Value value, int x, int y) {
      this.value = value;
      this.x = x;
      this.y = y;
      this.name = "";
      if(this.value instanceof BoolValue) {
         this.change = ((BoolValue) this.value).get();
      } else if(this.value instanceof ListValue) {
         this.name = "" + ((ListValue)(this.value)).get();
      } else if(value instanceof IntegerValue) {
         IntegerValue v = (IntegerValue)value;
         this.name = this.name + v.get();
      } else if(value instanceof FloatValue) {
         FloatValue v = (FloatValue) value;
         this.name = this.name + v.get();
      }

      this.opacity = 0.0D;
   }

   public void render(int mouseX, int mouseY) {
      GameFontRenderer font =  Fonts.font35;
      GameFontRenderer mfont = Fonts.font35;
      if (!this.custom) {
         IntegerValue v;
         this.opacity = mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + font.getStringWidth(this.value.getName()) + 5 ? (this.opacity + 10.0 < 200.0 ? (this.opacity += 10.0) : 200.0) : (this.opacity - 6.0 > 0.0 ? (this.opacity -= 6.0) : 0.0);
         RenderUtils.drawRect((this.x - 9), (this.y - 4), (this.x - 9 + 88), (this.y + mfont.getStringWidth(this.value.getName()) + 5), new Color(0, 0, 0, (int)this.opacity).getRGB());
         if (this.change) {
            RenderUtils.drawRect((this.x - 10), (this.y - 4), (this.x + 80), (this.y + mfont.getStringWidth(this.value.getName()) + 5), new Color(255, 255, 255,100).getRGB());
         }
         if (this.value instanceof BoolValue) {
            this.change = ((BoolValue)this.value).get();
         } else if (this.value instanceof ListValue) {
            this.name = "" + ((ListValue)this.value).getValues();
         } else if (this.value instanceof IntegerValue) {
            v = (IntegerValue) this.value;
            this.name = "" + v.get();
            if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + mfont.getStringWidth(this.value.getName()) + 5 && Mouse.isButtonDown(0)) {
               double min = v.getMinimum();
               double max = v.getMaximum();
               double inc = 1;
               double valAbs = mouseX - (this.x + 1.0);
               double perc = valAbs / 68.0;
               perc = Math.min(Math.max(0.0, perc), 1.0);
               double valRel = (max - min) * perc;
               double val = min + valRel;
               val = Math.round(val * (1.0 / inc)) / (1.0 / inc);
               v.set((int)val);
            }
         } else if(this.value instanceof FloatValue) {
            FloatValue floatValue = (FloatValue) this.value;
            this.name = "" + floatValue.get();
            if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + mfont.getStringWidth(this.value.getName()) + 5 && Mouse.isButtonDown(0)) {
               double min = floatValue.getMinimum();
               double max = floatValue.getMaximum();
               double inc = 1;
               double valAbs = mouseX - (this.x + 1.0);
               double perc = valAbs / 68.0;
               perc = Math.min(Math.max(0.0, perc), 1.0);
               double valRel = (max - min) * perc;
               double val = min + valRel;
               val = Math.round(val * (1.0 / inc)) / (1.0 / inc);
               floatValue.set((int)val);
            }
         }
         if (this.value instanceof IntegerValue) {
            IntegerValue integerValue = (IntegerValue) this.value;
            double render = 68.0f * (integerValue.get() - integerValue.getMinimum()) / (integerValue.getMaximum() - integerValue.getMinimum());
            RenderUtils.drawRect((float)this.x, (float)(this.y + mfont.getStringWidth(this.value.getName()) + 2), (float)((float)(this.x + render + 1.0)), (float)(this.y + mfont.getStringWidth(this.value.getName()) + 3), (int)new Color(255, 255, 255,250).getRGB());
         }
         if (this.value instanceof FloatValue) {
            FloatValue integerValue = (FloatValue) this.value;
            double render = 68.0f * (integerValue.get() - integerValue.getMinimum()) / (integerValue.getMaximum() - integerValue.getMinimum());
            RenderUtils.drawRect((float)this.x, (float)(this.y + mfont.getStringWidth(this.value.getName()) + 2), (float)((float)(this.x + render + 1.0)), (float)(this.y + mfont.getStringWidth(this.value.getName()) + 3), (int)new Color(255, 255, 255,250).getRGB());
         }
         mfont.drawString(this.value.getName(), (float)(this.x - 5), (float)(this.y + 1), new Color(255, 255, 255).getRGB());
         mfont.drawString(this.name, (float)(this.x + 76 - mfont.getStringWidth(this.name)), (float)(this.y + 1), new Color(255, 255, 255).getRGB());
      }
   }
   public void key(char typedChar, int keyCode) {
   }

   public void click(int mouseX, int mouseY, int button) {
      if(!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y +  Fonts.font35.getStringWidth(this.value.getName()) + 5) {
         if(this.value instanceof BoolValue) {
            BoolValue m1 = (BoolValue)this.value;
            m1.set(!m1.get());
            return;
         }

         if(this.value instanceof ListValue) {
            ListValue m = (ListValue)this.value;
            String current = m.get();
            this.value.set(m.getValues()[m.getModeListNumber(current) + 1 >= m.getValues().length?0:m.getModeListNumber(current) + 1]);
         }
      }
   }
}
