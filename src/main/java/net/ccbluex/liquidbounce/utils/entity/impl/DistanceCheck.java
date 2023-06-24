package net.ccbluex.liquidbounce.utils.entity.impl;

import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.utils.entity.ICheck;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public final class DistanceCheck
implements ICheck {
    private final FloatValue distance;

    public DistanceCheck(FloatValue distance) {
        this.distance = distance;
    }

    @Override
    public boolean validate(Entity entity) {
        return Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= this.distance.get();
    }
}

