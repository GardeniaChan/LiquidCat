package net.ccbluex.liquidbounce.utils.entity.impl;

import net.ccbluex.liquidbounce.utils.entity.ICheck;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public final class AliveCheck
implements ICheck {
    @Override
    public boolean validate(Entity entity) {
        return entity.isEntityAlive() || Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("mineplex");
    }
}

