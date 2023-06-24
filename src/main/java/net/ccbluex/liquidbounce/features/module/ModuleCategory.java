package net.ccbluex.liquidbounce.features.module;

public enum ModuleCategory {

    COMBAT("Combat"),
    PLAYER("Player"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    WORLD("World"),
    MISC("Misc"),
    EXPLOIT("Exploit");

    public final String namee;
    public final boolean expanded;

    public int posY = 20;



    ModuleCategory(String name) {
        this.namee = name;
        expanded = true;
    }

}
