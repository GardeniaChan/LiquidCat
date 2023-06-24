package net.ccbluex.liquidbounce.utils.entity;

import net.minecraft.entity.Entity;

@FunctionalInterface
public interface ICheck {
    public boolean validate(Entity var1);
}

