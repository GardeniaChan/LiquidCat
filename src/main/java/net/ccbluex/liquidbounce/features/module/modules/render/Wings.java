package net.ccbluex.liquidbounce.features.module.modules.render;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.RenderWings;
@ModuleInfo(name = "Wings", description = "Show the wings", category = ModuleCategory.RENDER)
public class Wings extends Module {
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        RenderWings renderWings = new RenderWings();
        renderWings.renderWings(event.getPartialTicks());
    }
}

