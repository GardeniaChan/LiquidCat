package net.ccbluex.liquidbounce.utils;
public class Particles {
    public int ticks;

    public Location location;

    public String text;

    public Particles(Location location, String text) {
        this.location = location;
        this.text = text;
        this.ticks = 0;
    }
}